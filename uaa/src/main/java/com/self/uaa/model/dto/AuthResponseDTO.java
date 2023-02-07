package com.self.uaa.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty(value = "refresh_token")
    private String refreshToken;
}
