package com.mashreq.booking.repository;

import com.mashreq.booking.domain.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Long>, JpaSpecificationExecutor<ConferenceRoom> {
}
