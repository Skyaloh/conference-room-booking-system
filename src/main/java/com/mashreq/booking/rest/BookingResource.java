package com.mashreq.booking.rest;


import com.mashreq.booking.dto.BookingDTO;
import com.mashreq.booking.rest.errors.BadRequestAlertException;
import com.mashreq.booking.rest.vm.BookingVM;
import com.mashreq.booking.service.BookingService;
import com.mashreq.booking.service.criteria.BookingCriteria;
import com.mashreq.booking.service.queryservice.BookingQueryService;
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
public class BookingResource {
    private final Logger log = LoggerFactory.getLogger(BookingResource.class);

    private static final String ENTITY_NAME = "booking";
    private final BookingService bookingService;

    private final BookingQueryService bookingQueryService;

    public BookingResource(BookingService bookingService, BookingQueryService bookingQueryService) {
        this.bookingService = bookingService;
        this.bookingQueryService = bookingQueryService;
    }

    @PostMapping("/bookings")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingVM bookingVM) {
        log.debug("REST request to save bookingDTO : {}", bookingVM);

        BookingDTO result = bookingService.bookRoom(bookingVM);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id){
        Optional<BookingDTO> booking = bookingQueryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(booking);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings(BookingCriteria bookingCriteria, Pageable pageable) {
        log.debug("REST request to get Benefits by criteria: {}", bookingCriteria);
        Page<BookingDTO> page = bookingQueryService.findByCriteria(bookingCriteria, pageable);
        return ResponseEntity.ok().body(page.getContent());

    }

}
