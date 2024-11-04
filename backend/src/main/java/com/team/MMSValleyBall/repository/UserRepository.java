package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.UserRole;
import com.team.MMSValleyBall.enums.UserStatus;
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

    Users findByUserPhone(String userPassword);

    Page<Users> findByUserNameContainingOrderByUserIdAsc(@Param("keyword") String keyword, Pageable pageable);

    Page<Users> findByUserEmailContainingOrderByUserIdAsc(@Param("keyword") String keyword, Pageable pageable);

    Page<Users> findByUserMembership_MembershipNameContainingIgnoreCaseOrderByUserIdAsc(@Param("keyword") String keyword, Pageable pageable);

    // ============================================================================================
    //    관리자 삭제
    List<Users> findByUserRole(UserRole userRole);

    // ============================================================================================
    //    권한별 카운트
    long countByUserRole(UserRole userRole);

    // ============================================================================================
    //    필터링
    Page<Users> findByUserStatus(UserStatus userStatus, Pageable pageable);

    Page<Users> findByUserMembership_MembershipSeason_SeasonNameContainingIgnoreCaseOrderByUserIdAsc(@Param("seasonName") String seasonName, Pageable pageable);

    // 시즌 이름과 사용자 상태로 필터링하는 메서드
    Page<Users> findByUserMembershipMembershipSeasonSeasonNameAndUserStatus(
            String seasonName, UserStatus userStatus, Pageable pageable);

    // 멤버십 + 사용자 상태로 필터링하는 메서드
    Page<Users> findByUserMembershipMembershipNameContainingIgnoreCaseAndUserStatusOrderByUserIdAsc(
            String membershipName, UserStatus userStatus, Pageable pageable);

    // 시즌 이름과 멤버십으로  필터링하는 메서드
    Page<Users> findByUserMembershipMembershipNameContainingIgnoreCaseAndUserMembershipMembershipSeasonSeasonNameOrderByUserIdAsc(
            String membership, String season, Pageable pageable);

    // 시즌 이름, 사용자 상태, 멤버십 이름으로 필터링하는 메서드
    Page<Users> findByUserMembershipMembershipSeasonSeasonNameAndUserStatusAndUserMembershipMembershipNameContainingIgnoreCase(
            String seasonName, UserStatus userStatus, String membershipName, Pageable pageable);

}
