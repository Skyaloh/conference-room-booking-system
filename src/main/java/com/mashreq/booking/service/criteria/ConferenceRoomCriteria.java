package com.mashreq.booking.service.criteria;

import com.mashreq.booking.service.helper.filter.IntegerFilter;
import com.mashreq.booking.service.helper.filter.LongFilter;
import com.mashreq.booking.service.helper.filter.StringFilter;
import java.io.Serializable;
import java.util.Objects;


public class ConferenceRoomCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter capacity;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getCapacity() {
        return capacity;
    }

    public void setCapacity(IntegerFilter capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceRoomCriteria that = (ConferenceRoomCriteria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ConferenceRoomCriteria{" +
                "id=" + id +
                ", name=" + name +
                ", capacity=" + capacity +
                '}';
    }
}
