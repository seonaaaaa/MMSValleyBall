package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.entity.*;
import com.team.MMSValleyBall.enums.*;
import com.team.MMSValleyBall.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EntityManager em;
    private final MembershipRepository membershipRepository;

    @Autowired
    public AdminService(UserRepository userRepository, PaymentRepository paymentRepository, TicketRepository ticketRepository, MatchRepository matchRepository, SeasonRepository seasonRepository, MembershipSalesRepository membershipSalesRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EntityManager em, MembershipRepository membershipRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.matchRepository = matchRepository;
        this.seasonRepository = seasonRepository;
        this.membershipSalesRepository = membershipSalesRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.em = em;
        this.membershipRepository = membershipRepository;
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
        // 시즌별 매출 계산
        for (Season season : seasons) {
            // 시즌 시작일을 해당 월의 첫째 날로 조정
            LocalDate startDate = season.getSeasonStartDate().withDayOfMonth(1);
            // 시즌 종료일을 해당 월의 마지막 날로 조정
            LocalDate endDate = season.getSeasonEndDate().withDayOfMonth(season.getSeasonEndDate().lengthOfMonth());

            int totalPaymentAmount = paymentList.stream()
                    .filter(payment -> !payment.getPaymentCreateAt().toLocalDate().isBefore(startDate) &&
                            !payment.getPaymentCreateAt().toLocalDate().isAfter(endDate)) // 날짜 필터링
                    .filter(payment -> !payment.getPaymentStatus().equals(PaymentStatus.REFUNDED))
                    .mapToInt(Payment::getPaymentAmount)
                    .sum();

            int totalMembershipSales = membershipSalesList.stream()
                    .filter(sales -> !sales.getMembershipSalesCreateAt().toLocalDate().isBefore(startDate) && // 시작일 포함
                            !sales.getMembershipSalesCreateAt().toLocalDate().isAfter(endDate)) // 종료일 포함
                    .filter(x -> !x.getMembershipSalesStatus().equals(MembershipSalesStatus.REFUNDED))
                    .mapToInt(x -> x.getMembershipSalesMembership().getMembershipPrice()) // 가격을 가져오는 메서드
                    .sum();

            int totalTicketSales = ticketList.stream()
                    .filter(ticket -> !ticket.getTicketCreateAt().toLocalDate().isBefore(startDate) && // 시작일 포함
                            !ticket.getTicketCreateAt().toLocalDate().isAfter(endDate)) // 종료일 포함
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

        // System.out.println("시즌별 총 매출: " + seasonTotalMap);
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
    public Map<String, Map<String, Long>> getMonthlyTicketSales() {
        List<Object[]> results = ticketRepository.findMonthlySalesNative();

        // 시즌별 월별 매출을 저장할 Map
        Map<String, Map<String, Long>> monthlySalesBySeason = new HashMap<>();

        for (Object[] result : results) {
            String seasonName = ((Season) result[2]).getSeasonName(); // 시즌 이름
            Integer month = ((Number) result[0]).intValue();         // 월을 Integer로 변환
            Long totalSales = (Long) result[1];                      // 매출 합계를 Long으로 받기

            // 시즌별 매출 Map을 가져오거나 새로 생성
            monthlySalesBySeason
                    .computeIfAbsent(seasonName, k -> new HashMap<>()) // 시즌 이름이 없으면 새 Map 생성
                    .put(getMonthName(month), totalSales); // 월 이름과 매출 합계를 시즌별 Map에 저장
        }

        return monthlySalesBySeason;
    }

    public Map<String, Map<String, Long>> getMonthlyPaymentSales() {
        // 시즌 정보 찾기
        List<Season> seasons = seasonRepository.findAll();

        List<Object[]> results = paymentRepository.findMonthlySalesNative();

        System.out.println("월별 매출 조회: " + results);

        // 시즌별 월별 매출을 저장할 Map
        Map<String, Map<String, Long>> monthlySalesBySeason = new HashMap<>();

        for (Object[] result : results) {
            LocalDateTime paymentDate = (LocalDateTime) result[0]; // paymentCreateAt
            Integer month = ((Number) result[1]).intValue(); // 월
            Long totalSales = ((Number) result[2]).longValue(); // 총 매출

            // LocalDate로 변환
            LocalDate date = paymentDate.toLocalDate();

            // 시즌을 찾아서 해당 시즌에 매출 추가
            for (Season season : seasons) {
                LocalDate startDate = season.getSeasonStartDate(); // 시즌 시작일
                LocalDate endDate = season.getSeasonEndDate();     // 시즌 종료일

                // 시즌 시작일을 해당 월의 첫째 날로 조정
                LocalDate seasonStartMonthFirstDay = startDate.withDayOfMonth(1);
                LocalDate seasonEndMonthLastDay = endDate.withDayOfMonth(endDate.lengthOfMonth()); // 시즌 종료일의 해당 월 마지막 날

                // 날짜가 시즌 범위 내에 있는 경우
                if (!date.isBefore(seasonStartMonthFirstDay) && !date.isAfter(seasonEndMonthLastDay)) {
                    String seasonName = season.getSeasonName(); // 시즌 이름

                    // 시즌별 매출 Map을 가져오거나 새로 생성
                    monthlySalesBySeason
                            .computeIfAbsent(seasonName, k -> new HashMap<>()) // 시즌 이름이 없으면 새 Map 생성
                            .put(getMonthName(month), totalSales); // 월 이름과 매출 합계를 시즌별 Map에 저장

                    // 시즌 범위에 추가된 경우 한 번만 추가하고 나가도록 break
                    break;
                }
            }
        }
        System.out.println("월별 충전금액: " + monthlySalesBySeason);

        return monthlySalesBySeason;
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

    // ============================================================================================
    //  유저 정보 조회
    // 전체 유저수
    public long countUsersByRole(UserRole userRole) {
        return userRepository.countByUserRole(userRole);
    }

    // 유저 발란스 가져오기
    public Map<Long, Integer> getAllUsersBalanceMap(List<UserDTO> userDTOS) {
        Map<Long, Integer> userBalanceMap = new HashMap<>();

        for (UserDTO user : userDTOS) {
            // payment 계산 (null 체크 추가)
            int payment = (user.getPayments() != null)
                    ? user.getPayments().stream()
                    .filter(p -> p.getPaymentStatus() != PaymentStatus.REFUNDED)
                    .mapToInt(PaymentDTO::getPaymentAmount)
                    .sum()
                    : 0;

            // membership 계산 (null 체크 추가)
            int membership = (user.getMembershipSales() != null)
                    ? user.getMembershipSales().stream()
                    .mapToInt(membershipSalesDTO -> {
                        MembershipSales membershipSales = membershipSalesRepository.findById(membershipSalesDTO.getMembershipSalesId())
                                .orElseThrow(() -> new RuntimeException("MembershipSales not found with id: " + membershipSalesDTO.getMembershipSalesId()));
                        return membershipSales.getMembershipSalesMembership().getMembershipPrice();
                    })
                    .sum()
                    : 0;

            // ticket 계산 (null 체크 추가)
            int ticket = (user.getTickets() != null)
                    ? user.getTickets().stream()
                    .mapToInt(TicketDTO::getTicketPaidPrice)
                    .sum()
                    : 0;

            // leftMoney 계산
            int leftMoney = payment - membership - ticket;
            userBalanceMap.put(user.getUserId(), leftMoney);
        }

        return userBalanceMap;
    }

    // ============================================================================================
    //    유저 검색
    public Page<UserDTO> searchUsers(String searchCriteria, String keyword, Pageable pageable) {
        if (searchCriteria == null || keyword == null || keyword.trim().isEmpty()) {
            // 검색 기준이나 키워드가 없을 경우 전체 조회
            return userRepository.findAll(pageable).map(UserDTO::fromEntity);
        }
        switch (searchCriteria) {
            case "name":
                return userRepository.findByUserNameContainingOrderByUserIdAsc(keyword, pageable).map(UserDTO::fromEntity);
            case "email":
                return userRepository.findByUserEmailContainingOrderByUserIdAsc(keyword, pageable).map(UserDTO::fromEntity);
            case "membership":
                return userRepository.findByUserMembership_MembershipNameContainingIgnoreCaseOrderByUserIdAsc(keyword, pageable).map(UserDTO::fromEntity);
            default:
                return userRepository.findAll(pageable).map(UserDTO::fromEntity);
        }
    }

    // ============================================================================================
    //    필터링

    //    시즌 + 상태
    public Page<UserDTO> findUsersBySeasonAndStatus(String season, UserStatus userStatus, Pageable pageable) {
        Page<Users> usersPage = userRepository.findByUserMembershipMembershipSeasonSeasonNameAndUserStatus(season, userStatus, pageable);
        return usersPage.map(UserDTO::fromEntity);
    }

    // 멤버십 + 상태 필터링
    public Page<UserDTO> findUsersByMembershipAndStatus(String membership, UserStatus userStatus, Pageable pageable) {
        Page<Users> usersPage = userRepository.findByUserMembershipMembershipNameContainingIgnoreCaseAndUserStatusOrderByUserIdAsc(membership, userStatus, pageable);
        return usersPage.map(UserDTO::fromEntity);
    }

    // 시즌 + 멤버쉽 필터링
    public Page<UserDTO> findUsersByMembershipAndSeason(String membership, String season, Pageable pageable) {
        Page<Users> usersPage = userRepository.findByUserMembershipMembershipNameContainingIgnoreCaseAndUserMembershipMembershipSeasonSeasonNameOrderByUserIdAsc(membership, season, pageable);
        return usersPage.map(UserDTO::fromEntity);
    }

    // 시즌 + 멤버십 + 상태 필터링
    public Page<UserDTO> findUsersBySeasonAndStatusAndMembership(String season, UserStatus userStatus, String membership, Pageable pageable) {
        Page<Users> usersPage = userRepository.findByUserMembershipMembershipSeasonSeasonNameAndUserStatusAndUserMembershipMembershipNameContainingIgnoreCase(
                season, userStatus, membership, pageable);
        return usersPage.map(UserDTO::fromEntity);
    }

    //    전체 보여주기
    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserDTO::fromEntity);
    }

    // 시즌 필터링
    public Page<UserDTO> findUsersBySeason(String season, Pageable pageable) {
        Page<Users> usersPage = userRepository.findByUserMembership_MembershipSeason_SeasonNameContainingIgnoreCaseOrderByUserIdAsc(season, pageable);
        return usersPage.map(UserDTO::fromEntity);
    }

    //    멤버쉽 필터링
    public Page<UserDTO> findUsersByMembership(String membership, Pageable pageable) {
        return userRepository.findByUserMembership_MembershipNameContainingIgnoreCaseOrderByUserIdAsc(membership, pageable).map(UserDTO::fromEntity);
    }

    //    상태 필터링
    public Page<UserDTO> findUsersByStatus(UserStatus userStatus, Pageable pageable) {
        Page<Users> usersPage = userRepository.findByUserStatus(userStatus, pageable);
        return usersPage.map(UserDTO::fromEntity);
    }

    // ============================================================================================
    // 상세정보
    public UserDTO findUserById(Long userId) {
        return userRepository.findById(userId)
                .map(UserDTO::fromEntity)  // Users 엔티티를 UserDTO로 변환하는 매핑 메서드
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userId: " + userId));
    }

    // 유저 상태 변경
    public void toggleUserStatus(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        if (user.getUserStatus() == UserStatus.ACTIVE) {
            user.setUserStatus(UserStatus.INACTIVE);
        } else {
            user.setUserStatus(UserStatus.ACTIVE);
        }
        userRepository.save(user);
    }

    // ============================================================================================
    // 관리자 추가
    // 이메일 중복 확인[ 완료 ]
    public boolean isEmailDuplicate(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }

    public boolean isPhoneDuplicate(String userPhone) {
        return userRepository.existsByUserPhone(userPhone);
    }

    // 관리자 생성
    public void createAdmin(UserDTO adminDTO) {
        Users adminUser = new Users();
        adminUser.setUserEmail(adminDTO.getUserEmail());
        adminUser.setUserPassword(bCryptPasswordEncoder.encode(adminDTO.getUserPassword())); // 비밀번호 암호화
        adminUser.setUserName(adminDTO.getUserName());
        adminUser.setUserPhone(adminDTO.getUserPhone());
        adminUser.setUserAddress(adminDTO.getUserAddress());
        adminUser.setUserRole(UserRole.ADMIN);
        adminUser.setUserStatus(UserStatus.ACTIVE);
        adminUser.setUserCreateAt(LocalDateTime.now());

        // 기본 멤버십 설정
        Membership defaultMembership = membershipRepository.findByMembershipName("24/25-bronze");
        adminUser.setUserMembership(defaultMembership);

        userRepository.save(adminUser); // 사용자 저장
    }

    // ============================================================================================
    // 관리자 삭제
    public void deleteAdmin(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        userRepository.delete(user);
    }


}




