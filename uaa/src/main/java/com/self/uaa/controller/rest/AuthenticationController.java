package com.self.uaa.controller.rest;

import com.self.uaa.helper.AuthenticationHelper;
import com.self.uaa.helper.JwtTokenHelper;
import com.self.uaa.model.dto.AuthRequestDTO;
import com.self.uaa.model.dto.AuthResponseDTO;
import com.self.uaa.model.dto.TokenDTO;
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
@RequestMapping("api/auth/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtTokenHelper jwtTokenHelper;

    private final AuthenticationHelper authenticationHelper;

    @PostMapping("authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody AuthRequestDTO authRequestDTO) throws Exception {
        authenticationHelper.authenticate(authRequestDTO.getUsername(), authRequestDTO.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestDTO.getUsername());
        return ResponseEntity.ok(
                AuthResponseDTO.builder()
                        .accessToken(jwtTokenHelper.generateAccessToken(userDetails))
                        .refreshToken(jwtTokenHelper.generateRefreshToken(userDetails))
                        .build());
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody UserDTO userDTO) throws Exception {
        userDetailsService.register(userDTO);
        return ResponseEntity.ok("register");
    }

    @PostMapping("refresh-token")
    public ResponseEntity<Object> refreshToken(@RequestBody TokenDTO tokenDTO) throws Exception {
        if (jwtTokenHelper.isTokenExpired(tokenDTO.getRefreshToken())) {
            throw new Exception("Token expired");
        }

        return ResponseEntity.ok(
                TokenDTO.builder()
                        .refreshToken(jwtTokenHelper.regenerateRefreshToken(tokenDTO.getRefreshToken()))
                        .build());
    }
}
