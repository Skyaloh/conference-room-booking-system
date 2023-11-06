package com.mashreq.booking.service.criteria;

import com.mashreq.booking.service.helper.filter.InstantFilter;
import com.mashreq.booking.service.helper.filter.LongFilter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

public class MaintenanceWindowCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalTime startTime;

    private LocalTime endTime;

    private LongFilter conferenceRoomId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LongFilter getConferenceRoomId() {
        return conferenceRoomId;
    }

    public void setConferenceRoomId(LongFilter conferenceRoomId) {
        this.conferenceRoomId = conferenceRoomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenanceWindowCriteria that = (MaintenanceWindowCriteria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MaintenanceWindowCriteria{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", conferenceRoomId=" + conferenceRoomId +
                '}';
    }
}
