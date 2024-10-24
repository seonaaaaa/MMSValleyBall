package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.entity.Ticket;
import com.team.MMSValleyBall.enums.MatchStadium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchDTO {
    private Long matchId;
    private LocalDateTime matchDate;
    private MatchStadium matchStadium;
    private int matchSetScore;
    private TeamDTO matchOpponentTeam;
    private int matchOpponentTeamSetScore;
    private SeasonDTO matchSeason;
    private List<TicketDTO> tickets;

    public static MatchDTO fromEntity(Match match){
        List<TicketDTO> ticketDTOList = null;
        List<Ticket> ticketList = match.getTickets();
        if(!ObjectUtils.isEmpty(ticketList)){
            ticketDTOList = ticketList.stream().map(ticket -> TicketDTO.fromEntity(ticket)).toList();
        }
        return new MatchDTO(
                match.getMatchId(),
                match.getMatchDate(),
                match.getMatchStadium(),
                match.getMatchSetScore(),
                TeamDTO.fromEntity(match.getMatchOpponentTeam()),
                match.getMatchOpponentTeamSetScore(),
                SeasonDTO.fromEntity(match.getMatchSeason()),
                ticketDTOList
        );
    }
}
