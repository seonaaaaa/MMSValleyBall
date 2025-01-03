package com.team.MMSValleyBall.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDto {
    private String name;
    private String role;
    private String email;
}
