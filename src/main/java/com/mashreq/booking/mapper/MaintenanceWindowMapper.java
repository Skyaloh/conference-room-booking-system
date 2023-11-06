package com.mashreq.booking.mapper;

import com.mashreq.booking.domain.MaintenanceWindow;
import com.mashreq.booking.dto.MaintenanceWindowDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity MaintenanceWindow and its DTO MaintenanceWindowDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ConferenceRoomMapper.class})
public interface MaintenanceWindowMapper extends EntityMapper<MaintenanceWindowDTO, MaintenanceWindow>{

    @Mapping(source = "conferenceRoom.id", target = "conferenceRoomId")
    MaintenanceWindowDTO toDto(MaintenanceWindow maintenanceWindow);

    @Mapping(source = "conferenceRoomId", target = "conferenceRoom")
    MaintenanceWindow toEntity(MaintenanceWindowDTO maintenanceWindowDTO);

    default MaintenanceWindow fromId(Long id) {
        if (id == null) {
            return null;
        }
        MaintenanceWindow maintenanceWindow = new MaintenanceWindow();
        maintenanceWindow.setId(id);
        return maintenanceWindow;
    }
}
