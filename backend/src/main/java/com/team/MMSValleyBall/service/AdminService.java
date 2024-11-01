package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.entity.*;
import com.team.MMSValleyBall.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public AdminService(UserRepository userRepository, PaymentRepository paymentRepository, TicketRepository ticketRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.matchRepository = matchRepository;
    }

    // 유저 전체 조회
    public Page<Users> findAllUsers(Pageable pageable) {
        Page<Users> usersList = userRepository.findAll(pageable);
        return usersList;
    }

    // 특정 유저  조회
    public UserDTO findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserDTO::fromEntity)  // Users 엔티티를 UserDTO로 변환하는 매핑 메서드
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userId: " + userId));
    }

    // 전체 유저 수 계산
    public long getTotalUserCount() {
        return userRepository.count();
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
                "1월", "2월", "3월", "4월", "5월", "6월",
                "7월", "8월", "9월", "10월", "11월", "12월"
        };
        return monthNames[month - 1];
    }
}




