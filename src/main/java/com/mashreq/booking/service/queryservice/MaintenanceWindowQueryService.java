package com.mashreq.booking.service.queryservice;


import com.mashreq.booking.domain.*;
import com.mashreq.booking.dto.ConferenceRoomDTO;
import com.mashreq.booking.dto.MaintenanceWindowDTO;
import com.mashreq.booking.mapper.MaintenanceWindowMapper;
import com.mashreq.booking.repository.MaintenanceWindowRepository;
import com.mashreq.booking.service.criteria.ConferenceRoomCriteria;
import com.mashreq.booking.service.criteria.MaintenanceWindowCriteria;
import com.mashreq.booking.service.helper.QueryService;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class MaintenanceWindowQueryService extends QueryService<MaintenanceWindow> {

    private final Logger log = LoggerFactory.getLogger(MaintenanceWindowQueryService.class.getName());

    private final MaintenanceWindowMapper maintenanceWindowMapper;

    private final MaintenanceWindowRepository maintenanceWindowRepository;

    public MaintenanceWindowQueryService(MaintenanceWindowMapper maintenanceWindowMapper, MaintenanceWindowRepository maintenanceWindowRepository) {
        this.maintenanceWindowMapper = maintenanceWindowMapper;
        this.maintenanceWindowRepository = maintenanceWindowRepository;
    }

    /**
     * Return a {@link Optional} of {@link MaintenanceWindowDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Optional<MaintenanceWindowDTO> findOneCriteria(MaintenanceWindowCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MaintenanceWindow> specification = createSpecification(criteria);
        return maintenanceWindowRepository.findOne(specification).map(maintenanceWindowMapper::toDto);
    }

    /**
     * Return a {@link Page} of {@link MaintenanceWindowDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaintenanceWindowDTO> findByCriteria(MaintenanceWindowCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaintenanceWindow> specification = createSpecification(criteria);
        return maintenanceWindowRepository.findAll(specification, page)
                .map(maintenanceWindowMapper::toDto);
    }

    /**
     * Return a {@link List} of {@link MaintenanceWindowDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MaintenanceWindowDTO> findByCriteria(MaintenanceWindowCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MaintenanceWindow> specification = createSpecification(criteria);
        return maintenanceWindowMapper.toDto(maintenanceWindowRepository.findAll(specification));
    }


    /**
     * Function to convert MaintenanceWindowCriteria to a {@link Specification}
     */
    private Specification<MaintenanceWindow> createSpecification(MaintenanceWindowCriteria criteria) {
        Specification<MaintenanceWindow> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), MaintenanceWindow_.id));
            }

            if(criteria.getConferenceRoomId() != null){
                specification = specification.and(buildSpecification(criteria.getConferenceRoomId(),
                        root -> root.join(MaintenanceWindow_.conferenceRoom, JoinType.LEFT).get(ConferenceRoom_.id)));
            }
            

        }
        return specification;
    }
}
