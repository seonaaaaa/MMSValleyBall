package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.entity.*;
import com.team.MMSValleyBall.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final MatchRepository matchRepository;

    public AdminService(UserRepository userRepository, PaymentRepository paymentRepository, TicketRepository ticketRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.matchRepository = matchRepository;
    }

    // 유저 전체 조회 [ 페이징 ]
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDTO::fromEntity);
    }

    // 멤버쉽별 유저 조회 [ 페이징 ]
    public Page<UserDTO> findUsersByUserMembership(Membership userMembership, Pageable pageable) {
        return userRepository.findUsersByUserMembership(userMembership, pageable)
                .map(UserDTO::fromEntity);
    }

    // 특정 유저 디테일 조회
    public UserDTO findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserDTO::fromEntity)
                .orElse(null);
    }

    // 총 매출 조회
    public int getTotalPaymentAmount() {
        List<Payment> paymentList = paymentRepository.findAll();
        int totalAmount = paymentList.stream()
                .mapToInt(Payment::getPaymentAmount)
                .sum();
        return totalAmount;
    }

    // 경기별 매출 조회
    public Map<Integer, String> getPaymentsByMatchId() {

        List<Object[]> results = ticketRepository.findTotalPaidPriceByMatch();
        Map<Integer, String> matchPayments = new HashMap<>();
        for (Object[] result : results) {
            Integer totalAmount = ((Number) result[0]).intValue();
            String matchName = (String) result[1];
            matchPayments.put(totalAmount, matchName);
        }
        return matchPayments;
    }

    // 월별 매출 조회
    public Map<String, Long> getMonthlySales() {
        List<Object[]> results = ticketRepository.findMonthlySalesNative();

        Map<String, Long> monthlySales = new HashMap<>();
        for (Object[] result : results) {
            Integer month = ((Number) result[0]).intValue();  // 월을 Integer로 변환
            Long totalSales = (Long) result[1];               // 매출 합계를 Long으로 받기
            monthlySales.put(getMonthName(month), totalSales);  // 월 이름과 매출 합계를 Map에 저장
        }
        return monthlySales;
    }

    private String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[month - 1];
    }
}



