package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {
    private Long teamId;
    private String teamName;
    private String teamStadium;
    private List<MatchDTO> matches;

    public static TeamDTO fromEntity(Team team){
        List<MatchDTO> matchDTOList = null;
        List<Match> matchList = team.getMatches();
        if(!ObjectUtils.isEmpty(matchList)){
            matchDTOList = matchList.stream().map(match -> MatchDTO.fromEntity(match)).toList();
        }
        return new TeamDTO(
                team.getTeamId(),
                team.getTeamName(),
                team.getTeamStadium(),
                matchDTOList
        );
    }
}
