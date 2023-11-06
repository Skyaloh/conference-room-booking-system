package com.mashreq.booking.dto;


import com.mashreq.booking.domain.AbstractAuditingEntity;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Objects;

public class MaintenanceWindowDTO extends AbstractAuditingEntity implements Serializable {

    private Long id;
    private LocalTime startTime;

    private LocalTime endTime;

    @NotNull
    private Long conferenceRoomId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getConferenceRoomId() {
        return conferenceRoomId;
    }

    public void setConferenceRoomId(Long conferenceRoomId) {
        this.conferenceRoomId = conferenceRoomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaintenanceWindowDTO that = (MaintenanceWindowDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MaintenanceWindowDTO{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", conferenceRoomId=" + conferenceRoomId +
                '}';
    }
}
