package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.entity.Ticket;
import com.team.MMSValleyBall.enums.MembershipSalesStatus;
import com.team.MMSValleyBall.enums.PaymentStatus;
import com.team.MMSValleyBall.enums.TicketStatus;
import com.team.MMSValleyBall.repository.AutoStatusSystemRepository;
import com.team.MMSValleyBall.repository.MembershipSalesRepository;
import com.team.MMSValleyBall.repository.PaymentRepository;
import com.team.MMSValleyBall.repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AutoStatusSystemService {

//    private final AutoStatusSystemRepository autoStatusSystemRepository;
    private final PaymentRepository paymentRepository;
    private final MembershipSalesRepository membershipSalesRepository;
    private final TicketRepository ticketRepository;

    public AutoStatusSystemService(PaymentRepository paymentRepository, MembershipSalesRepository membershipSalesRepository, TicketRepository ticketRepository) {
        this.paymentRepository = paymentRepository;
        this.membershipSalesRepository = membershipSalesRepository;
        this.ticketRepository = ticketRepository;
    }

    // 현재 10분 단위로 업데이트 진행 중  ( con = " 0초 10분마다 매시간 매일 매월 매년 ?")
    @Scheduled(cron = "0 */1 * * * ?")
    @Transactional
    public void updatePaymentStatus() {
        // 충전: 현재 서버 시간 기준으로 7일이 지나면 CONFIRMED로 상태 변경됨
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Payment> payments = paymentRepository.findByPaymentStatusAndPaymentCreateAtBefore(PaymentStatus.COMPLETED, sevenDaysAgo);
        System.out.println("payments"+payments);
        for (Payment payment : payments) {
            if (payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
                payment.setPaymentStatus(PaymentStatus.CONFIRMED);
                payment.setPaymentUpdateAt(LocalDateTime.now());
                paymentRepository.save(payment);
                log.info("Updated payment status for payment ID: {}", payment.getPaymentId());
            }
        }

        // 멤버십: 현재 서버 시간 기준으로 7일이 지나면 CONFIRMED로 상태 변경됨
        List<MembershipSales> membershipSalesList = membershipSalesRepository.findByMembershipSalesStatusAndMembershipSalesCreateAtBefore(MembershipSalesStatus.CONFIRMED, sevenDaysAgo);
        System.out.println("membershipSalesList"+membershipSalesList);
        for (MembershipSales membershipSales : membershipSalesList) {
            if (membershipSales.getMembershipSalesStatus() == MembershipSalesStatus.PURCHASE) {
                membershipSales.setMembershipSalesStatus(MembershipSalesStatus.CONFIRMED);
                membershipSales.setMembershipSalesUpdateAt(LocalDateTime.now());
                membershipSalesRepository.save(membershipSales);
                log.info("Updated membership sales status for ID: {}", membershipSales.getMembershipSalesId());
            }
        }

        // 티켓: match_date가 오늘 날짜인 티켓을 찾고, 아직 CONFIRMED 상태가 아닌 경우 상태를 업데이트
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnightToday = now.toLocalDate().atStartOfDay(); // 오늘 자정 (00:00)

        // 특정 날짜의 티켓을 가져오고, 상태가 CONFIRMED가 아닌 경우
        List<Ticket> tickets = ticketRepository.findTicketsByMatchDateAndStatus(midnightToday, TicketStatus.CONFIRMED);
        System.out.println("ticket match date : " + tickets);
        for (Ticket ticket : tickets) {
            if (ticket.getTicketStatus() == TicketStatus.BOOKED) {
                ticket.setTicketStatus(TicketStatus.CONFIRMED);
                ticket.setTicketUpdateAt(LocalDateTime.now());
                ticketRepository.save(ticket);
                log.info("Updated ticket status for ticket ID: {}", ticket.getTicketId());
            }
        }
    }
}
