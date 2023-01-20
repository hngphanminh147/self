package com.self.uaa.controller.rest;

import com.self.uaa.helper.AuthenticationHelper;
import com.self.uaa.helper.JwtTokenHelper;
import com.self.uaa.model.dto.AuthRequestDTO;
import com.self.uaa.model.dto.AuthResponseDTO;
import com.self.uaa.model.dto.UserDTO;
import com.self.uaa.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtTokenHelper jwtTokenHelper;

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody AuthRequestDTO authRequestDTO) throws Exception {
        AuthenticationHelper.authenticate(authRequestDTO.getUsername(), authRequestDTO.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestDTO.getUsername());
        String jwt = jwtTokenHelper.generateToken(userDetails);
        return ResponseEntity.ok(AuthResponseDTO.builder().jwt(jwt).build());
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserDTO userDTO) throws Exception {
        userDetailsService.register(userDTO);
        return ResponseEntity.ok("register");
    }
}
