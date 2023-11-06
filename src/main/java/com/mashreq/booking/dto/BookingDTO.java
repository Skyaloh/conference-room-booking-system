package com.mashreq.booking.dto;


import com.mashreq.booking.domain.AbstractAuditingEntity;
import com.mashreq.booking.domain.enumeration.BookingStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;


public class BookingDTO extends AbstractAuditingEntity implements Serializable {

    private Long id;

    private Long conferenceRoomId;

    @NotNull
    private Long userId;

    @NotNull
    private Integer numberOfPeople;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.BOOKED;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getConferenceRoomId() {
        return conferenceRoomId;
    }

    public void setConferenceRoomId(Long conferenceRoomId) {
        this.conferenceRoomId = conferenceRoomId;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDTO that = (BookingDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "id=" + id +
                ", numberOfPeople=" + numberOfPeople +
                ", conferenceRoomId=" + conferenceRoomId +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                '}';
    }
}
