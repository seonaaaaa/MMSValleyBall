package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.MembershipSalesStatus;
import com.team.MMSValleyBall.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MembershipSalesRepository extends JpaRepository<MembershipSales, Long> {
   // 멤버십 구매 테이블: 자동 상태 변환 로직 - 7일
   List<MembershipSales> findByMembershipSalesStatusAndMembershipSalesCreateAtBefore(MembershipSalesStatus membershipSalesStatus, LocalDateTime createAt);

   // 멤버십 리스트 조회
   List<MembershipSales> findByMembershipSalesUser(Users user);
}
