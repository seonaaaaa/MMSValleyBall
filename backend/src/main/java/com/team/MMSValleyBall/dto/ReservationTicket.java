package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.enums.MatchStadium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationTicket {
    private TicketDTO ticket;
    private String opponentTeam;
    private String opponentTeamStadium;
    private MatchStadium where;
    private LocalDateTime date;
    private String seatSection;
}
