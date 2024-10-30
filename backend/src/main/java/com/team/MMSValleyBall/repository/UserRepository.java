package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    // Users 객체를 직접 반환하는 메서드
    Users findByUserEmail(String userEmail);

    // 이메일 존재 여부 확인 메서드
    boolean existsByUserEmail(String userEmail);

    // 유저 리스트 [페이징 처리]
    Page<Users> findAll(Pageable pageable);

    // 멤버쉽별 유저리스트 [ 페이징 처리]
    Page<Users> findUsersByUserMembership(Membership userMembership, Pageable pageable);

}

