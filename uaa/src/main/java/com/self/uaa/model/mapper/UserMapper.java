package com.self.uaa.model.mapper;

import com.self.uaa.model.Role;
import com.self.uaa.model.User;
import com.self.uaa.model.dto.UserDTO;
import io.jsonwebtoken.lang.Collections;
import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);

    default Set<String> toRoleValue(Collection<? extends GrantedAuthority> roles) {
        if (Collections.isEmpty(roles)) {
            return null;
        }
        Set<String> values = new HashSet<>();
        for (GrantedAuthority role : roles) {
            values.add(role.getAuthority());
        }
        return values;
    }

    User toUser(UserDTO userDTO);

    default Set<Role> toUserRoles(Set<String> authorities) {
        if (Collections.isEmpty(authorities)) {
            return null;
        }
        Set<Role> roles = new HashSet<>();
        for (String authority : authorities) {
            roles.add(Role.valueOf(authority));
        }
        return roles;
    }
}
