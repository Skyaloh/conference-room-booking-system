package com.mashreq.booking.service.criteria;

import com.mashreq.booking.domain.enumeration.BookingStatus;
import com.mashreq.booking.service.helper.filter.Filter;
import com.mashreq.booking.service.helper.filter.InstantFilter;
import com.mashreq.booking.service.helper.filter.IntegerFilter;
import com.mashreq.booking.service.helper.filter.LongFilter;
import java.io.Serializable;
import java.util.Objects;

public class BookingCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    public static class BookingStatusFilter extends Filter<BookingStatus> {
    }

    private LongFilter id;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private LongFilter conferenceRoomId;

    private IntegerFilter numberOfPeople;

    private BookingStatusFilter status;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public LongFilter getConferenceRoomId() {
        return conferenceRoomId;
    }

    public void setConferenceRoomId(LongFilter conferenceRoomId) {
        this.conferenceRoomId = conferenceRoomId;
    }

    public BookingStatusFilter getStatus() {
        return status;
    }

    public void setStatus(BookingStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(IntegerFilter numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingCriteria that = (BookingCriteria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BookingCriteria{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", conferenceRoomId=" + conferenceRoomId +
                '}';
    }
}
