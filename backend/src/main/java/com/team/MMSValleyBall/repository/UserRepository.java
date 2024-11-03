package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {

    // Users 객체를 직접 반환하는 메서드
    Users findByUserEmail(String userEmail);

    // 이메일 존재 여부 확인 메서드
    boolean existsByUserEmail(String userEmail);

    boolean existsByUserPhone(String userPhone);

    Page<Users> findByUserNameContainingOrderByUserIdAsc(@Param("keyword") String keyword, Pageable pageable);

    Page<Users> findByUserEmailContainingOrderByUserIdAsc(@Param("keyword") String keyword, Pageable pageable);

    Page<Users> findByUserMembership_MembershipNameContainingIgnoreCaseOrderByUserIdAsc(@Param("keyword") String keyword, Pageable pageable);

    List<Users> findByUserRole(UserRole userRole);

    long countByUserRole(UserRole userRole);
}