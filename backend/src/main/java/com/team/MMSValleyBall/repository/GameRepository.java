package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Match;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Match, Long> {
    // 시즌 ID를 기반으로 매치 조회
    Page<Match> findByMatchSeason_SeasonId(Long seasonId, Pageable pageable);
}
