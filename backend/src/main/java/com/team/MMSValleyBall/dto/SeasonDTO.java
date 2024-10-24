package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.Season;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonDTO {
    private Long seasonId;
    private String seasonName;
    private LocalDate seasonStartDate;
    private LocalDate seasonEndDate;
    private List<MatchDTO> matches;
    private List<MembershipDTO> memberships;

    @Override
    public String toString() {
        return "SeasonDTO{" +
                "seasonId=" + seasonId +
                ", seasonName='" + seasonName + '\'' +
                ", seasonStartDate=" + seasonStartDate +
                ", seasonEndDate=" + seasonEndDate +
                ", matches=" + matches + // MatchDTO에 대한 toString() 구현 필요
                ", memberships=" + memberships + // MembershipDTO에 대한 toString() 구현 필요
                '}';
    }

    public static SeasonDTO fromEntity(Season season){
        List<MatchDTO> matchDTOList = null;
        List<Match> matchList = season.getMatches();
        if(!ObjectUtils.isEmpty(matchList)){
            matchDTOList = matchList.stream().map(match -> MatchDTO.fromEntity(match)).toList();
        }
        List<MembershipDTO> membershipDTOList = null;
        List<Membership> membershipList = season.getMemberships();
        if(!ObjectUtils.isEmpty(membershipList)){
            membershipDTOList = membershipList.stream().map(membership -> MembershipDTO.fromEntity(membership)).toList();
        }
        return new SeasonDTO(
                season.getSeasonId(),
                season.getSeasonName(),
                season.getSeasonStartDate(),
                season.getSeasonEndDate(),
                matchDTOList,
                membershipDTOList
                );
    }
}
