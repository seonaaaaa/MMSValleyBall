package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    // Users 객체를 직접 반환하는 메서드
    Users findByUserEmail(String userEmail);

    // 이메일 존재 여부 확인 메서드
    boolean existsByUserEmail(String userEmail);

    boolean existsByUserPhone(String userPhone);

    Users findByUserPhone(String userPassword);
}