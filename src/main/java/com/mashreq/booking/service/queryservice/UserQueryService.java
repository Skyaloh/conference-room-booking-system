package com.mashreq.booking.service.queryservice;

import com.mashreq.booking.domain.User;
import com.mashreq.booking.domain.User_;
import com.mashreq.booking.dto.UserDTO;
import com.mashreq.booking.mapper.UserMapper;
import com.mashreq.booking.repository.UserRepository;
import com.mashreq.booking.service.criteria.UserCriteria;
import com.mashreq.booking.service.helper.QueryService;
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
public class UserQueryService extends QueryService<User> {

    private final Logger log = LoggerFactory.getLogger(UserQueryService.class.getName());

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public UserQueryService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }


    /**
     * Return a {@link Page} of {@link UserDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findByCriteria(UserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<User> specification = createSpecification(criteria);
        return userRepository.findAll(specification, page)
                .map(userMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<User> specification = createSpecification(criteria);
        return userRepository.count(specification);
    }

    /**
     * Function to convert UserCriteria to a {@link Specification}
     */
    private Specification<User> createSpecification(UserCriteria criteria) {
        Specification<User> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), User_.id));
            }

        }
        return specification;
    }
    /**
     * Get one user by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> findOne(Long id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findById(id).map(userMapper::toDto);
    }

    /**
     * Get all the users.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all users");
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }
}
