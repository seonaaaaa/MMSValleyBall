package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.enums.MatchStadium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private TicketDTO ticket;
    private String opponentTeam;
    private String opponentTeamStadium;
    private MatchStadium where;
    private String matchDate;
    private String  seatSection;
}
