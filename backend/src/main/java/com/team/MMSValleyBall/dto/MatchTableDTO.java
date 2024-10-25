package com.team.MMSValleyBall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MatchTableDTO {
    private Long matchId;
    private LocalDateTime matchDate;
    private String matchTeam;
    private String matchStadium;
}
