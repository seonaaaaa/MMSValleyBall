package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
