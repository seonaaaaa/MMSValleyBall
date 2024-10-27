package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MoneyDTO;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.entity.Ticket;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.MembershipSalesStatus;
import com.team.MMSValleyBall.enums.PaymentStatus;
import com.team.MMSValleyBall.enums.TicketStatus;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class UsersBalanceService {
    private final UserRepository userRepository;

    public UsersBalanceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MoneyDTO getUsersBalance(String email) {
        // Optional 사용 시 값이 있는지 체크
        Users user = userRepository.findByUserEmail(email);

        if (ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("User not found with id: " + user.getUserId());
        }


        // 총 충전 금액, 결제 금액 계산하기
        int payment = 0;
        int membership = 0;
        int ticket = 0;

        // Payment 계산
        for (Payment x : user.getPayments()) {
            if (x.getPaymentStatus() != PaymentStatus.REFUNDED) {
                payment += x.getPaymentAmount();
            }
        }
        System.out.println("payment : " + payment);

        // Membership 계산
        for (MembershipSales x : user.getMembershipSales()) {
            if (x.getMembershipSalesStatus() != MembershipSalesStatus.REFUNDED) {
                membership += x.getMembershipSalesMembership().getMembershipPrice();
            }
        }
        System.out.println("membership : " + membership);

        // Ticket 계산
        for (Ticket x : user.getTickets()) {
            if (x.getTicketStatus() != TicketStatus.CANCELLED) {
                ticket += x.getTicketPaidPrice();
            }
        }
        System.out.println("ticket price : " + ticket);

        // MoneyDTO 생성
        MoneyDTO moneyDTO = new MoneyDTO();
        moneyDTO.setUserId(user.getUserId());
        moneyDTO.setPaymentAmount(payment);
        moneyDTO.setMembershipPrice(membership);
        moneyDTO.setTicketPaidPrice(ticket);
        System.out.println("left money : "+moneyDTO.getLeftMoney());

        return moneyDTO;
    }
}
