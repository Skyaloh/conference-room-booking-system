package com.mashreq.booking.rest.vm;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class BookingVM implements Serializable {
    @NotNull
    private Long userId;

    @NotNull
    private Integer numberOfPeople;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

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
        return "BookingVM{" +
                "userId=" + userId +
                ", numberOfPeople=" + numberOfPeople +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
