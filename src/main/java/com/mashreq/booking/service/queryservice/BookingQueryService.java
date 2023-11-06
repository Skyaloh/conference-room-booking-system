package com.mashreq.booking.service.queryservice;


import com.mashreq.booking.domain.Booking;
import com.mashreq.booking.domain.Booking_;
import com.mashreq.booking.domain.ConferenceRoom_;
import com.mashreq.booking.dto.BookingDTO;
import com.mashreq.booking.mapper.BookingMapper;
import com.mashreq.booking.repository.BookingRepository;
import com.mashreq.booking.service.criteria.BookingCriteria;
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
public class BookingQueryService extends QueryService<Booking> {

    private final Logger log = LoggerFactory.getLogger(BookingQueryService.class.getName());

    private final BookingMapper bookingMapper;

    private final BookingRepository bookingRepository;

    public BookingQueryService(BookingMapper bookingMapper, BookingRepository bookingRepository) {
        this.bookingMapper = bookingMapper;
        this.bookingRepository = bookingRepository;
    }


    /**
     * Return a {@link List} of {@link BookingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> findByCriteria(BookingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingMapper.toDto(bookingRepository.findAll(specification));
    }

    /**
     * Return a {@link Optional} of {@link BookingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOneCriteria(BookingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.findOne(specification).map(bookingMapper::toDto);
    }

    /**
     * Return a {@link Page} of {@link BookingDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingDTO> findByCriteria(BookingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.findAll(specification, page)
                .map(bookingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.count(specification);
    }

    /**
     * Function to convert BookingCriteria to a {@link Specification}
     */
    private Specification<Booking> createSpecification(BookingCriteria criteria) {
        Specification<Booking> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Booking_.id));
            }

            if(criteria.getStartTime() != null){
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Booking_.startTime));
            }

            if(criteria.getEndTime() != null){
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Booking_.endTime));
            }

            if(criteria.getStatus() != null){
                specification = specification.and(buildSpecification(criteria.getStatus(), Booking_.status));
            }

            if(criteria.getNumberOfPeople() != null){
                specification = specification.and(buildSpecification(criteria.getNumberOfPeople(), Booking_.numberOfPeople));
            }

            if(criteria.getConferenceRoomId() != null){
                specification = specification.and(buildSpecification(criteria.getConferenceRoomId(),
                        root -> root.join(Booking_.conferenceRoom, JoinType.LEFT).get(ConferenceRoom_.id)));
            }

        }
        return specification;
    }
    /**
     * Get one booking by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOne(Long id) {
        log.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id).map(bookingMapper::toDto);
    }

    /**
     * Get all the users.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BookingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all users");
        return bookingRepository.findAll(pageable)
                .map(bookingMapper::toDto);
    }
}
