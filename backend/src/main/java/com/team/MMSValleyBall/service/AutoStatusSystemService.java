package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.enums.PaymentStatus;
import com.team.MMSValleyBall.repository.AutoStatusSystemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AutoStatusSystemService {

    private final AutoStatusSystemRepository autoStatusSystemRepository;

    public AutoStatusSystemService(AutoStatusSystemRepository paymentRepository) {
        this.autoStatusSystemRepository = paymentRepository;
    }

    // 현재 10분 단위로 업데이트 진행 중  ( con = " 0초 10분마다 매시간 매일 매월 매년 ?")
    @Scheduled(cron = "0 */1 * * * ?")
    public void updatePaymentStatus() {
        // 현재 서버 시간 기준으로 3일이 지나면 CONFIRMED로 상태 변경됨
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<Payment> payments = autoStatusSystemRepository.findByPaymentStatusAndPaymentCreateAtBefore(PaymentStatus.COMPLETED, threeDaysAgo);

        for (Payment payment : payments) {
            payment.setPaymentStatus(PaymentStatus.CONFIRMED);
            payment.setPaymentUpdateAt(LocalDateTime.now());
            autoStatusSystemRepository.save(payment);
        }
    }
}
