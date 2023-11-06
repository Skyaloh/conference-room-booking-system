package com.mashreq.booking.rest;

import com.mashreq.booking.dto.UserDTO;
import com.mashreq.booking.rest.errors.BadRequestAlertException;
import com.mashreq.booking.service.UserService;
import com.mashreq.booking.service.criteria.UserCriteria;
import com.mashreq.booking.service.queryservice.UserQueryService;
import com.mashreq.booking.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserQueryService urserQueryService;

    private static final String ENTITY_NAME = "user";

    private final UserService userService;

    public UserResource(UserQueryService urserQueryService, UserService userService) {
        this.urserQueryService = urserQueryService;
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        log.debug("REST request to save userDTO : {}", userDTO);
        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDTO result = userService.save(userDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        Optional<UserDTO> user = urserQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(UserCriteria userCriteria, Pageable pageable) {
        log.debug("REST request to get Benefits by criteria: {}", userCriteria);
        Page<UserDTO> page = urserQueryService.findByCriteria(userCriteria, pageable);
        return ResponseEntity.ok().body(page.getContent());

    }
}
