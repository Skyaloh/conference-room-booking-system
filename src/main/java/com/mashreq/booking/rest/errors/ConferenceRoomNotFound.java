package com.mashreq.booking.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.io.Serial;

public class ConferenceRoomNotFound extends AbstractThrowableProblem {
    @Serial
    private static final long serialVersionUID = 1L;
    public ConferenceRoomNotFound(String defaultMessage) {
        super(ErrorConstants.ENTITY_NOT_FOUND_TYPE, defaultMessage, Status.NOT_FOUND);
    }
}
