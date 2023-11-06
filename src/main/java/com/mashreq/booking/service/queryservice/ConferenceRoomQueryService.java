package com.mashreq.booking.service.queryservice;

import com.mashreq.booking.domain.ConferenceRoom;
import com.mashreq.booking.domain.ConferenceRoom_;
import com.mashreq.booking.dto.BookingDTO;
import com.mashreq.booking.dto.ConferenceRoomDTO;
import com.mashreq.booking.mapper.ConferenceRoomMapper;
import com.mashreq.booking.repository.ConferenceRoomRepository;
import com.mashreq.booking.rest.vm.AvailableRoomsVM;
import com.mashreq.booking.service.criteria.BookingCriteria;
import com.mashreq.booking.service.criteria.ConferenceRoomCriteria;
import com.mashreq.booking.service.helper.QueryService;
import com.mashreq.booking.service.helper.filter.InstantFilter;
import com.mashreq.booking.service.helper.filter.LongFilter;
import com.mashreq.booking.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ConferenceRoomQueryService extends QueryService<ConferenceRoom> {

    private final Logger log = LoggerFactory.getLogger(ConferenceRoomQueryService.class.getName());

    private final ConferenceRoomMapper conferenceRoomMapper;

    private final ConferenceRoomRepository conferenceRoomRepository;

    private final BookingQueryService bookingQueryService;

    public ConferenceRoomQueryService(ConferenceRoomMapper conferenceRoomMapper, ConferenceRoomRepository conferenceRoomRepository, BookingQueryService bookingQueryService) {
        this.conferenceRoomMapper = conferenceRoomMapper;
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.bookingQueryService = bookingQueryService;
    }


    /**
     * Return a {@link List} of {@link ConferenceRoomDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConferenceRoomDTO> findByCriteria(ConferenceRoomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConferenceRoom> specification = createSpecification(criteria);
        return conferenceRoomMapper.toDto(conferenceRoomRepository.findAll(specification));
    }

    /**
     * Return a {@link Optional} of {@link ConferenceRoomDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Optional<ConferenceRoomDTO> findOneCriteria(ConferenceRoomCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ConferenceRoom> specification = createSpecification(criteria);
        return conferenceRoomRepository.findOne(specification).map(conferenceRoomMapper::toDto);
    }

    /**
     * Return a {@link Page} of {@link ConferenceRoomDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConferenceRoomDTO> findByCriteria(ConferenceRoomCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConferenceRoom> specification = createSpecification(criteria);
        return conferenceRoomRepository.findAll(specification, page)
                .map(conferenceRoomMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConferenceRoomCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConferenceRoom> specification = createSpecification(criteria);
        return conferenceRoomRepository.count(specification);
    }

    /**
     * Function to convert ConferenceRoomCriteria to a {@link Specification}
     */
    private Specification<ConferenceRoom> createSpecification(ConferenceRoomCriteria criteria) {
        Specification<ConferenceRoom> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ConferenceRoom_.id));
            }

            if (criteria.getCapacity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapacity(), ConferenceRoom_.capacity));
            }

        }
        return specification;
    }

    /**
     * Get one conferenceRoom by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ConferenceRoomDTO> findOne(Long id) {
        log.debug("Request to get ConferenceRoom : {}", id);
        return conferenceRoomRepository.findById(id).map(conferenceRoomMapper::toDto);
    }

    /**
     * Get all the available conferenceRooms.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ConferenceRoomDTO> availableConferenceRooms(AvailableRoomsVM availableRoomsVM) {
        log.debug("Request to get all available conferenceRooms");

        List<ConferenceRoomDTO> availableConferenceRooms = new ArrayList<>();

        Instant startTime = TimeFormatter.parseDateTime(availableRoomsVM.getStartTime());
        Instant endTime = TimeFormatter.parseDateTime(availableRoomsVM.getEndTime());

        conferenceRoomRepository.findAll().forEach(conferenceRoom -> {
            BookingCriteria bookingCriteria = new BookingCriteria();
            bookingCriteria.setConferenceRoomId(new LongFilter());
            bookingCriteria.getConferenceRoomId().setEquals(conferenceRoom.getId());

            InstantFilter endTimeFilter = new InstantFilter();
            endTimeFilter.setLessThanOrEqual(endTime);
            endTimeFilter.setGreaterThanOrEqual(startTime);

            InstantFilter startTimeFilter = new InstantFilter();
            startTimeFilter.setLessThanOrEqual(endTime);
            startTimeFilter.setGreaterThanOrEqual(startTime);

            bookingCriteria.setEndTime(endTimeFilter);
            bookingCriteria.setStartTime(startTimeFilter);

            if (bookingQueryService.countByCriteria(bookingCriteria) == 0) {
                availableConferenceRooms.add(conferenceRoomMapper.toDto(conferenceRoom));
            }

        });
        return availableConferenceRooms;
    }
}
