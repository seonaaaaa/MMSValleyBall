package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 충전 테이블: 자동 상태 변환 로직 - 일
    List<Payment> findByPaymentStatusAndPaymentCreateAtBefore(PaymentStatus paymentStatus, LocalDateTime createAt);

}
