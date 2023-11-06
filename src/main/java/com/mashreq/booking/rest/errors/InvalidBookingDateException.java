package com.mashreq.booking.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.io.Serial;

public class InvalidBookingDateException extends AbstractThrowableProblem {
    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidBookingDateException(String bookedTime) {
            super(ErrorConstants.ENTITY_NOT_FOUND_TYPE,String.format("Booking can only be done for the current date: %s", bookedTime), Status.BAD_REQUEST);
    }

}
