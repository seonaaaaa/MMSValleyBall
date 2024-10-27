package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
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

    // MAIN 페이지 경기 일정 조회 (MatchDTO 반환)
    public List<MatchDTO> findAllMatches() {
        // 출력 TEST
//        List<Match> matchList = gameRepository.findAll();
//        System.out.println("matchList.size : " + matchList.size());
//        return gameRepository.findAll()
//                .stream()
//                .map(entity-> MatchDTO.fromEntity(entity)).toList();

        return gameRepository.findAll()
                .stream()
                .map(entity-> MatchDTO.fromEntity(entity))
                .toList();
    }

    // 전체 경기 일정 조회 (MatchWithTeamDTO 반환)
    public List<MatchWithTeamDTO> findAllMatchesWithTeams() {
        // Match 엔티티 리스트를 가져온 후, 이를 MatchWithTeamDTO로 변환하여 반환
        List<MatchWithTeamDTO> matchList = gameRepository.findAll()
                                            .stream()
                                            .map(entity -> MatchWithTeamDTO.fromEntity(entity))
                                            .toList();

        // 리스트 크기 출력
        System.out.println("matchList.size() : " + matchList.size());

        return matchList;
    }
}
