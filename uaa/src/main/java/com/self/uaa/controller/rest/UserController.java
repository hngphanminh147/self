package com.self.uaa.controller.rest;

import com.self.uaa.constants.Constants;
import com.self.uaa.model.User;
import com.self.uaa.model.exception.ApiError;
import com.self.uaa.model.mapper.UserMapper;
import com.self.uaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @GetMapping("users/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getUsers() {
        return ResponseEntity.ok(
                userRepository.findAll().stream()
                        .map(userMapper::toUserDTO)
                        .collect(Collectors.toList()));
    }

    @GetMapping("users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiError(HttpStatus.BAD_REQUEST.value(),
                        Constants.USERNAME_NOT_FOUND,
                        String.format(Constants.USERNAME_NOT_FOUND_FORMAT, id)));
        return ResponseEntity.ok(userMapper.toUserDTO(user));
    }
}
