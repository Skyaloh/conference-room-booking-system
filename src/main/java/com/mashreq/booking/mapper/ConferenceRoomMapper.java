package com.mashreq.booking.mapper;

import com.mashreq.booking.domain.ConferenceRoom;
import com.mashreq.booking.dto.ConferenceRoomDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { })
public interface ConferenceRoomMapper extends EntityMapper<ConferenceRoomDTO, ConferenceRoom>{

    ConferenceRoomDTO toDto(ConferenceRoom conferenceRoom);

    ConferenceRoom toEntity(ConferenceRoomDTO conferenceRoomDTO);

    default ConferenceRoom fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConferenceRoom conferenceRoom = new ConferenceRoom();
        conferenceRoom.setId(id);
        return conferenceRoom;
    }
}
