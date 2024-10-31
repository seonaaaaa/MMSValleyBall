package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.enums.MatchStatus;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Match, Long> {

    @Query("SELECT m FROM Match m WHERE m.matchStatus = 'DEFAULT'")
    List<Match> findAllDefaultMatches();

    // 특정 시즌 ID, 라운드 ID에 해당하는 매치 목록을 페이징 처리하여 가져오기 위한 메서드
    @Query("SELECT m FROM Match m WHERE m.matchSeason.seasonId = :seasonId AND (:matchRoundId IS NULL OR m.matchRoundId = :matchRoundId) AND m.matchStatus = :matchStatus")
    Page<Match> findBySeasonAndRound(@Param("seasonId") Long seasonId, 
                                     @Param("matchRoundId") Integer matchRoundId, 
                                     @Param("matchStatus") MatchStatus matchStatus,
                                     Pageable pageable);
}