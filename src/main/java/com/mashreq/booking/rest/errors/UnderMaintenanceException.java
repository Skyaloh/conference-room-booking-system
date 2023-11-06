package com.mashreq.booking.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.io.Serial;

public class UnderMaintenanceException extends AbstractThrowableProblem {
    @Serial
    private static final long serialVersionUID = 1L;
    public UnderMaintenanceException(String bookedTime) {
        super(ErrorConstants.ENTITY_NOT_FOUND_TYPE,String.format("The room is under maintenance for the given time: %s", bookedTime), Status.BAD_REQUEST);
    }

}
