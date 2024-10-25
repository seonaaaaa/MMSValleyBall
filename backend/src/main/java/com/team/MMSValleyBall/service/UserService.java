package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MoneyDTO;
import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.entity.Ticket;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.MembershipSalesStatus;
import com.team.MMSValleyBall.enums.PaymentStatus;
import com.team.MMSValleyBall.enums.TicketStatus;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findOneByUserId(Long id) {
        Optional<Users> user = userRepository.findById(id);
        if (user.isPresent()) {  // Optional의 존재 여부를 확인
            System.out.println("service - user : " + user.get());
            UserDTO dto = UserDTO.fromEntity(user.get());
            System.out.println("service - userDTO : "+dto.toString());
            return dto;
        }
        return null;
    }

    public MoneyDTO getUserMoney(String email) {
        // Optional 사용 시 값이 있는지 체크
        Optional<Users> userOptional = userRepository.findByUserEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with id: " + userOptional.get().getUserId());
        }

        Users user = userOptional.get();

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