package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<MatchDTO> findAllMatches() {
        // 모든 경기 정보를 조회하고 MatchDTO로 변환하여 반환
//        List<Match> matchList = gameRepository.findAll();
//        System.out.println("smatchList.size : " + matchList.size());
//        return gameRepository.findAll()
//                .stream()
//                .map(entity-> MatchDTO.fromEntity(entity)).toList();

        return gameRepository.findAll()
                .stream()
                .map(entity-> MatchDTO.fromEntity(entity))
                .toList();
    }
}
