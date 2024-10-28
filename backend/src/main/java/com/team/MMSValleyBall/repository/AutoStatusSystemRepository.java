package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AutoStatusSystemRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPaymentStatusAndPaymentCreateAtBefore(PaymentStatus paymentStatus, LocalDateTime threeDaysAgo);
}
