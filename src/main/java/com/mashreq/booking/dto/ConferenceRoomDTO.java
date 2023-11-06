package com.mashreq.booking.dto;

import com.mashreq.booking.domain.AbstractAuditingEntity;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


public class ConferenceRoomDTO extends AbstractAuditingEntity implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer capacity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConferenceRoomDTO that = (ConferenceRoomDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ConferenceRoomDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
