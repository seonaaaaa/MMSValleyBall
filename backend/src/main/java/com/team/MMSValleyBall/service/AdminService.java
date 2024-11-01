package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.entity.*;
import com.team.MMSValleyBall.enums.MembershipSalesStatus;
import com.team.MMSValleyBall.enums.PaymentStatus;
import com.team.MMSValleyBall.enums.TicketStatus;
import com.team.MMSValleyBall.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@Slf4j
public class AdminService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final MatchRepository matchRepository;
    private final SeasonRepository seasonRepository;
    private final MembershipSalesRepository membershipSalesRepository;

    @Autowired
    public AdminService(UserRepository userRepository, PaymentRepository paymentRepository, TicketRepository ticketRepository, MatchRepository matchRepository, SeasonRepository seasonRepository, MembershipSalesRepository membershipSalesRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.matchRepository = matchRepository;
        this.seasonRepository = seasonRepository;
        this.membershipSalesRepository = membershipSalesRepository;
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

    // 시즌별 총 매출 조회
    public Map<String, Map<String, Integer>> getTotalPaymentAmount() {
        // 시즌 정보 찾기
        List<Season> seasons = seasonRepository.findAll();
        // 충전금액 조회
        List<Payment> paymentList = paymentRepository.findAll();
        // 멤버십구매 금액 조회
        List<MembershipSales> membershipSalesList = membershipSalesRepository.findAll();
        // 티켓 구매 금액 조회
        List<Ticket> ticketList = ticketRepository.findAll();

        // 시즌별 매출을 저장할 맵
        Map<String, Map<String, Integer>> seasonTotalMap = new HashMap<>();
        //시즌별 매출 계산
        for (Season season : seasons) {
            int totalPaymentAmount = paymentList.stream()
                    .filter(payment -> !payment.getPaymentCreateAt().isBefore(season.getSeasonStartDate().atStartOfDay()) &&
                            !payment.getPaymentCreateAt().isAfter(season.getSeasonEndDate().atStartOfDay())) // 날짜 필터링
                    .filter(payment -> !payment.getPaymentStatus().equals(PaymentStatus.REFUNDED))
                    .mapToInt(Payment::getPaymentAmount)
                    .sum();

            int totalMembershipSales = membershipSalesList.stream()
                    .filter(sales -> !sales.getMembershipSalesCreateAt().isBefore(season.getSeasonStartDate().atStartOfDay()) && // 시작일 포함
                            !sales.getMembershipSalesCreateAt().isAfter(season.getSeasonEndDate().atStartOfDay())) // 종료일 포함
                    .filter(x -> !x.getMembershipSalesStatus().equals(MembershipSalesStatus.REFUNDED))
                    .mapToInt(x -> x.getMembershipSalesMembership().getMembershipPrice()) // 가격을 가져오는 메서드
                    .sum();

            int totalTicketSales = ticketList.stream()
                    .filter(ticket -> !ticket.getTicketCreateAt().isBefore(season.getSeasonStartDate().atStartOfDay()) && // 시작일 포함
                            !ticket.getTicketCreateAt().isAfter(season.getSeasonEndDate().atStartOfDay())) // 종료일 포함
                    .filter(x -> !x.getTicketStatus().equals(TicketStatus.CANCELLED))
                    .mapToInt(x -> x.getTicketPaidPrice()) // 가격을 가져오는 메서드
                    .sum();

            // 총합 계산
            Map<String, Integer> totalAmount = new HashMap<>();
            totalAmount.put("payment", totalPaymentAmount);
            totalAmount.put("membershipSales", totalMembershipSales);
            totalAmount.put("ticketSales", totalTicketSales);

            // 시즌과 해당 시즌의 총합을 맵에 추가
            seasonTotalMap.put(season.getSeasonName(), totalAmount);

        }
//        System.out.println("시즌별 총 매출:"+seasonTotalMap);
        return seasonTotalMap;
    }

    // 경기별 매출 조회
    public Map<String, List<MatchPaymentDTO>> getPaymentsByMatchId() {
        // 경기별 매출 조회
        List<Object[]> results = ticketRepository.findTotalPaidPriceByMatch();
        // 시즌별 매출 정보를 담을 Map
        Map<String, List<MatchPaymentDTO>> matchPaymentsBySeason = new HashMap<>();

        for (Object[] result : results) {
            String seasonName = ((Season) result[2]).getSeasonName(); // 시즌 이름
            Integer totalAmount = ((Number) result[0]).intValue(); // 총 매출액
            Long matchId = (Long) result[3]; // 경기 ID
            String matchInfo = ((LocalDateTime) result[4]).toLocalDate().toString() + " VS" + (String) result[1]; // 경기 정보

            // MatchPaymentDTO 객체 생성
            MatchPaymentDTO paymentInfo = new MatchPaymentDTO(matchId, matchInfo, totalAmount);

            // 시즌별 리스트에 추가
            matchPaymentsBySeason
                    .computeIfAbsent(seasonName, k -> new ArrayList<>()) // 시즌 이름이 없으면 새 리스트 생성
                    .add(paymentInfo); // 리스트에 매출 정보 추가
        }

        // 시즌별 경기별 매출 조회 결과 출력
        System.out.println("경기별 매출 조회: " + results);
        return matchPaymentsBySeason;
    }


    // 월별 매출 조회
    public Map<String, Long> getMonthlySales() {
        List<Object[]> results = ticketRepository.findMonthlySalesNative();
        System.out.println("월별 매출 조회: "+ results);

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

    public Map<Long, String> getSeasonList() {
        List<Season> seasonList = seasonRepository.findAll(Sort.by(Sort.Direction.DESC, "seasonId"));
        Map<Long, String> seasons = new LinkedHashMap<>();

        for (Season season : seasonList) {
            seasons.put(season.getSeasonId(), season.getSeasonName());
        }
        return seasons;
    }

}




