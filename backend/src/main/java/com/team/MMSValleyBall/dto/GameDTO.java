package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.entity.Season;
import com.team.MMSValleyBall.entity.Team;
import com.team.MMSValleyBall.enums.MatchStadium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private Long matchId;
    private LocalDateTime matchDate;
    private MatchStadium matchStadium;
    private int matchSetScore;
    private Team matchOpponentTeam;
    private int matchOpponentTeamSetScore;
    private Season matchSeason;
    // 티켓 데이터 없이 경기 정보만 출력

    public static GameDTO fromEntity(Match match) {
        return new GameDTO(
                match.getMatchId(),
                match.getMatchDate(),
                match.getMatchStadium(),
                match.getMatchSetScore(),
                match.getMatchOpponentTeam(),
                match.getMatchOpponentTeamSetScore(),
                match.getMatchSeason()
        );
    }
}
