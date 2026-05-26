package edu.co.ustavillavicencio.coachboard.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long userId;
    private String username;
    private String email;
    private String rol;
    private Long organizacionId;
}