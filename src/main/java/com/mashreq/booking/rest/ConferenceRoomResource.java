package com.mashreq.booking.rest;

import com.mashreq.booking.dto.ConferenceRoomDTO;
import com.mashreq.booking.rest.errors.BadRequestAlertException;
import com.mashreq.booking.rest.vm.AvailableRoomsVM;
import com.mashreq.booking.service.ConferenceRoomService;
import com.mashreq.booking.service.criteria.ConferenceRoomCriteria;
import com.mashreq.booking.service.queryservice.ConferenceRoomQueryService;
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
public class ConferenceRoomResource {
    private final Logger log = LoggerFactory.getLogger(ConferenceRoomResource.class);
    
    private final ConferenceRoomService conferenceRoomService;

    private static final String ENTITY_NAME = "ConferenceRoom";
    
    private final ConferenceRoomQueryService conferenceRoomQueryService;


    public ConferenceRoomResource(ConferenceRoomService conferenceRoomService, ConferenceRoomQueryService conferenceRoomQueryService) {
        this.conferenceRoomService = conferenceRoomService;
        this.conferenceRoomQueryService = conferenceRoomQueryService;
    }

    @PostMapping("/conference-rooms")
    public ResponseEntity<ConferenceRoomDTO> createConferenceRoom(@RequestBody ConferenceRoomDTO conferenceRoomDTO) {
        log.debug("REST request to save conferenceRoomDTO : {}", conferenceRoomDTO);
        if (conferenceRoomDTO.getId() != null) {
            throw new BadRequestAlertException("A new booking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConferenceRoomDTO result = conferenceRoomService.save(conferenceRoomDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/conference-rooms/{id}")
    public ResponseEntity<ConferenceRoomDTO> getConferenceRoom(@PathVariable Long id){
        Optional<ConferenceRoomDTO> booking = conferenceRoomQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booking);
    }

    @GetMapping("/conference-rooms")
    public ResponseEntity<List<ConferenceRoomDTO>> getAllConferenceRooms(ConferenceRoomCriteria bookingCriteria, Pageable pageable) {
        log.debug("REST request to get ConferenceRooms by criteria: {}", bookingCriteria);
        Page<ConferenceRoomDTO> page = conferenceRoomQueryService.findByCriteria(bookingCriteria, pageable);
        return ResponseEntity.ok().body(page.getContent());

    }

    @PostMapping("/conference-rooms/available")
    public ResponseEntity<List<ConferenceRoomDTO>> getAllAvailableConferenceRooms(@RequestBody AvailableRoomsVM availableRoomsVM) {
        log.debug("REST request to get Available ConferenceRooms: {}", availableRoomsVM);
        List<ConferenceRoomDTO> conferenceRoomDTOS = conferenceRoomQueryService.availableConferenceRooms(availableRoomsVM);
        return ResponseEntity.ok().body(conferenceRoomDTOS);

    }
}
