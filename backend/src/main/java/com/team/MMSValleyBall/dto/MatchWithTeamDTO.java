package com.team.MMSValleyBall.dto;

import java.time.LocalDateTime;

import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.enums.MatchStadium;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchWithTeamDTO {
    // 일정 정보
    private Long matchId;
    private LocalDateTime matchDate;
    private MatchStadium matchStadium;
    private int matchSetScore;
    private int matchOpponentTeamSetScore;

    // 시즌 정보
    private Long seasonId;
    private String seasonName;

    // 팀 정보
    private Long teamId;
    private String teamName;
    private String teamStadium;

    public static MatchWithTeamDTO fromEntity(Match match) {

        return new MatchWithTeamDTO(
            match.getMatchId(),
            match.getMatchDate(),
            match.getMatchStadium(),
            match.getMatchSetScore(),
            match.getMatchOpponentTeamSetScore(),
            match.getMatchSeason().getSeasonId(),          // 시즌 ID
            match.getMatchSeason().getSeasonName(),        // 시즌 이름
            match.getMatchOpponentTeam().getTeamId(),      // 상대 팀 ID
            match.getMatchOpponentTeam().getTeamName(),    // 상대 팀 이름
            match.getMatchOpponentTeam().getTeamStadium()  // 상대 팀 경기장
        );
    }
}
