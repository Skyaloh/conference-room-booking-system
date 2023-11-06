package com.mashreq.booking.mapper;

import com.mashreq.booking.domain.User;
import com.mashreq.booking.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { })
public interface UserMapper extends EntityMapper<UserDTO, User>{

    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);

    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
