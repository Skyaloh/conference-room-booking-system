package com.mashreq.booking.rest.vm;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class AvailableRoomsVM implements Serializable {
    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "AvailableRoomsVM{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
