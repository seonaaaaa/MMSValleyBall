package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.enums.UserRole;
import lombok.Getter;


@Getter
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    private UserRole role;

    public JwtResponse(String accessToken, String username, String email, UserRole role) {
        this.token = accessToken;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}