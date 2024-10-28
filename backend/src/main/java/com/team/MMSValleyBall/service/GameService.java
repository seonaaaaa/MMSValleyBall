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
        // System.out.println("matchList.size() : " + matchList.size());
        return matchList;
    }

    // 시즌별 페이지 단위 경기 일정 조회 (Page<MatchWithTeamDTO> 반환)
    public Page<MatchWithTeamDTO> findPagedMatchesBySeason(Long seasonId, Pageable pageable) {
        Page<MatchWithTeamDTO> matchPages = gameRepository.findByMatchSeason_SeasonId(seasonId, pageable)
                                                          .map(entity -> MatchWithTeamDTO.fromEntity(entity));
    
        // 가져온 전체 경기의 수
        // System.out.println("Total elements in matchPages: " + matchPages.getTotalElements());
        // 현재 페이지의 요소 수
        // System.out.println("Number of elements in current page: " + matchPages.getNumberOfElements());
        // 총 페이지 수
        // System.out.println("Total pages: " + matchPages.getTotalPages());
        
        return matchPages;
    }
}
