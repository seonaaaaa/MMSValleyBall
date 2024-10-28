package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Match;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Match, Long> {
    // 시즌 ID를 기반으로 매치 조회
    Page<Match> findByMatchSeason_SeasonId(Long seasonId, Pageable pageable);

    // 특정 시즌 ID, 월, 라운드 ID에 해당하는 매치 목록을 페이징 처리하여 가져오기 위한 메서드
    @Query("SELECT m FROM Match m WHERE (:month IS NULL OR MONTH(m.matchDate) = :month) AND m.matchSeason.seasonId = :seasonId AND (:matchRoundId IS NULL OR m.matchRoundId = :matchRoundId)")
    Page<Match> findBySeasonAndMonthAndRound(@Param("seasonId") Long seasonId, 
                                            @Param("month") Integer month, 
                                            @Param("matchRoundId") Integer matchRoundId, 
                                            Pageable pageable);
}