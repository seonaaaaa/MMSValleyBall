package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.enums.PaymentStatus;
import com.team.MMSValleyBall.repository.PaymentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // 매일 자정마다 실행
    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePaymentStatus() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<Payment> payments = paymentRepository.findByPaymentStatusAndPaymentCreateAtBefore(PaymentStatus.COMPLETED, threeDaysAgo);

        for (Payment payment : payments) {
            payment.setPaymentStatus(PaymentStatus.CONFIRMED);
            paymentRepository.save(payment);
        }
    }
}
