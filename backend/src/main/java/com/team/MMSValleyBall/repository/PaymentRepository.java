package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
