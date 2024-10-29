package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
import com.team.MMSValleyBall.repository.GameRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return gameRepository.findAll()
                .stream()
                .map(entity-> MatchDTO.fromEntity(entity))
                .toList();
    }

    // 전체 경기 일정 조회 (MatchWithTeamDTO 반환)
    public List<MatchWithTeamDTO> findAllMatchesWithTeams() {
        List<MatchWithTeamDTO> matchList = gameRepository.findAll()
                                            .stream()
                                            .map(entity -> MatchWithTeamDTO.fromEntity(entity))
                                            .toList();
        // 리스트 크기 출력
        // System.out.println("matchList.size() : " + matchList.size());
        return matchList;
    }

    // 시즌별 페이지 단위 경기 일정 조회 (Page<MatchWithTeamDTO> 반환)
    public Page<MatchWithTeamDTO> getMatchesBySeasonAndRound(Long seasonId, Integer matchRoundId, Pageable pageable) {
        return gameRepository.findBySeasonAndRound(seasonId, matchRoundId, pageable)
                                .map(entity -> MatchWithTeamDTO.fromEntity(entity));
    }
}
