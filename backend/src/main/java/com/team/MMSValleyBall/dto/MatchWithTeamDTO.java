package com.team.MMSValleyBall.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.enums.MailStatus;
import com.team.MMSValleyBall.enums.MatchStadium;

import com.team.MMSValleyBall.enums.MatchStatus;
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

    // 라운드 정보
    private int matchRoundId;

    // 팀 정보
    private Long teamId;
    private String teamName;
    private String teamStadium;

    // 메일 발송 상태
    private MailStatus matchMailStatus;

    // 경기 상태
    private MatchStatus matchStatus;

    public static MatchWithTeamDTO fromEntity(Match match) {

        return new MatchWithTeamDTO(
            match.getMatchId(),
            match.getMatchDate(),
            match.getMatchStadium(),
            match.getMatchSetScore(),
            match.getMatchOpponentTeamSetScore(),
            match.getMatchSeason().getSeasonId(),          // 시즌 ID
            match.getMatchSeason().getSeasonName(),        // 시즌 이름
            match.getMatchRoundId(),                       // 라운드 ID
            match.getMatchOpponentTeam().getTeamId(),      // 상대 팀 ID
            match.getMatchOpponentTeam().getTeamName(),    // 상대 팀 이름
            match.getMatchOpponentTeam().getTeamStadium(), // 상대 팀 경기장
            match.getMatchMailStatus(),                    // 메일 발송 상태
            match.getMatchStatus()                         // 경기 상태
        );
    }
}