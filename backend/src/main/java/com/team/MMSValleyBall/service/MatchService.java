package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.dto.MatchTableDTO;
import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MatchService {
    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }


    public List<MatchDTO> findAllMatches() {
        List<Match> matchList = matchRepository.findAll();
        System.out.println("service - match size : " + matchList.size());
        return matchList.stream().map(x -> MatchDTO.fromEntity(x)).toList();
    }

    public List<MatchTableDTO> convertMatchToString() {
        List<Match> matchList = matchRepository.findAll();
        List<MatchTableDTO> matches = new ArrayList<>();

        for (Match match : matchList) {
            MatchTableDTO dto = new MatchTableDTO();
            dto.setMatchId(match.getMatchId());
            dto.setMatchDate(match.getMatchDate());

            // null 체크 추가
            if (match.getMatchOpponentTeam() != null) {
                dto.setMatchTeam(match.getMatchOpponentTeam().getTeamName());
                dto.setMatchStadium(match.getMatchOpponentTeam().getTeamStadium());
            } else {
                dto.setMatchTeam("Unknown");
                dto.setMatchStadium("Unknown"); // 혹은 다른 기본 값 설정
            }

            matches.add(dto);
        }
        return matches;
    }

    public MatchTableDTO getOneMatch(Long matchId) {
        Match e = matchRepository.findById(matchId)
                .orElseThrow(() -> new NoSuchElementException("Match not found with id: " + matchId));

        MatchTableDTO dto = new MatchTableDTO();
        dto.setMatchId(matchId);
        dto.setMatchDate(e.getMatchDate());
        dto.setMatchTeam(e.getMatchOpponentTeam().getTeamName());
        dto.setMatchStadium(e.getMatchOpponentTeam().getTeamStadium());

        return dto;
    }
}
