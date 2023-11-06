package com.mashreq.booking.mapper;


import com.mashreq.booking.domain.Booking;
import com.mashreq.booking.dto.BookingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Booking and its DTO BookingDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ConferenceRoomMapper.class})
public interface BookingMapper extends EntityMapper<BookingDTO, Booking>{

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "conferenceRoom.id", target = "conferenceRoomId")
    BookingDTO toDto(Booking booking);

    @Mapping(source = "conferenceRoomId", target = "conferenceRoom")
    @Mapping(source = "userId", target = "user")
   Booking toEntity(BookingDTO bookingDTO);

    default Booking fromId(Long id) {
        if (id == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }
}
