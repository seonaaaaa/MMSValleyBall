package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

    // 매치  리스트 [ 페이징 처리 ]
    Page<Match> findAll(Pageable pageable);

}
