package com.self.uaa.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String accessToken;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String refreshToken;
}
