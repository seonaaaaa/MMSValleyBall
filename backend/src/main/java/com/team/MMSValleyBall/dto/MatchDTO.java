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
    private Long matchOpponentTeamId;
    private int matchOpponentTeamSetScore;
    private Long matchSeasonId;
    private List<TicketDTO> tickets;

    @Override
    public String toString() {
        return "MatchDTO{" +
                "Id=" + matchId +
                ", Date=" + matchDate +
                ", Stadium=" + (matchStadium != null ? matchStadium : "null") +
                ", SetScore=" + matchSetScore +
                ", OpponentTeam=" + matchOpponentTeamId +
                ", OpponentTeamSetScore=" + matchOpponentTeamSetScore +
                ", Season=" + matchSeasonId +
                ", tickets=" + (tickets != null ? tickets : "null") +
                '}' + '\n';
    }


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
                match.getMatchOpponentTeam().getTeamId(),
                match.getMatchOpponentTeamSetScore(),
                match.getMatchSeason().getSeasonId(),
                ticketDTOList
        );
    }
}
