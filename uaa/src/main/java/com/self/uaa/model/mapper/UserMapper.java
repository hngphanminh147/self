package com.self.uaa.model.mapper;

import com.self.uaa.model.User;
import com.self.uaa.model.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);
}
