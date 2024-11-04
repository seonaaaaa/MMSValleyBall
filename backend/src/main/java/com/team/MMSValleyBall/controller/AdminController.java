package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchPaymentDTO;
import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.UserRole;
import com.team.MMSValleyBall.enums.UserStatus;
import com.team.MMSValleyBall.repository.UserRepository;
import com.team.MMSValleyBall.service.AdminService;
import com.team.MMSValleyBall.service.MainService;
import com.team.MMSValleyBall.service.PaginationService;
import com.team.MMSValleyBall.service.UsersBalanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AdminService adminService;
    private final PaginationService paginationService;
    private final UsersBalanceService usersBalanceService;
    private MainService mainService;
    private final UserRepository userRepository;

    public AdminController(BCryptPasswordEncoder bCryptPasswordEncoder, AdminService adminService, PaginationService paginationService, MainService mainService, UsersBalanceService usersBalanceService, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.adminService = adminService;
        this.paginationService = paginationService;
        this.mainService = mainService;
        this.usersBalanceService = usersBalanceService;
        this.userRepository = userRepository;
    }

    @GetMapping("/userList")
    public String getUsers(
            @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(value = "membership", required = false) String membership,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "season", required = false) String season,
            @PageableDefault(page = 0, size = 5, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {

        // 필터링 조건에 따라 유저 리스트 가져오기
        Page<UserDTO> userPage = getUserPageByFilter(season, status, membership, searchCriteria, keyword, pageable);

        // 모델에 유저 관련 정보 추가
        addUserDetailsToModel(model, userPage);

        // 페이지 정보 추가
        addPaginationToModel(model, userPage);

        // 필터링 상태를 모델에 추가하여 페이지에 유지
        addFilterStatusToModel(model, searchCriteria, keyword, membership, status, season);

        return "userList"; // AJAX 요청 시 content 부분만 반환
    }

    private Page<UserDTO> getUserPageByFilter(
            String season, String status, String membership,
            String searchCriteria, String keyword, Pageable pageable) {

        // 모든 필터가 제공된 경우: 시즌, 상태, 멤버십
        if (membership != null && !membership.isEmpty()
                && season != null && !season.isEmpty()
                && status != null && !status.isEmpty()) {
            try {
                UserStatus userStatus = UserStatus.valueOf(status.toUpperCase());
                return adminService.findUsersBySeasonAndStatusAndMembership(season, userStatus, membership, pageable);
            } catch (IllegalArgumentException e) {
                // 잘못된 상태 값이 전달된 경우 예외 처리
                System.out.println("Invalid status value: " + status);
                // 기본값으로 전체 사용자 반환 또는 사용자에게 알림 처리 등을 고려
            }
        }

        // 시즌과 상태 필터
        if (season != null && !season.isEmpty() && status != null && !status.isEmpty()) {
            try {
                UserStatus userStatus = UserStatus.valueOf(status.toUpperCase());
                return adminService.findUsersBySeasonAndStatus(season, userStatus, pageable);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status value: " + status);
            }
        }

        // 멤버십과 상태 필터
        if (membership != null && !membership.isEmpty() && status != null && !status.isEmpty()) {
            try {
                UserStatus userStatus = UserStatus.valueOf(status.toUpperCase());
                return adminService.findUsersByMembershipAndStatus(membership, userStatus, pageable);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status value: " + status);
            }
        }

        // 멤버십과 시즌 필터
        if (membership != null && !membership.isEmpty() && season != null && !season.isEmpty()) {
            return adminService.findUsersByMembershipAndSeason(membership, season, pageable);
        }

        // 시즌 필터
        if (season != null && !season.isEmpty()) {
            return adminService.findUsersBySeason(season, pageable);
        }

        // 멤버십 필터
        if (membership != null && !membership.isEmpty()) {
            return adminService.findUsersByMembership(membership, pageable);
        }

        // 상태 필터
        if (status != null && !status.isEmpty()) {
            try {
                UserStatus userStatus = UserStatus.valueOf(status.toUpperCase());
                return adminService.findUsersByStatus(userStatus, pageable);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status value: " + status);
            }
        }

        // 검색 조건과 키워드 필터
        if (searchCriteria != null && keyword != null && !keyword.trim().isEmpty()) {
            return adminService.searchUsers(searchCriteria, keyword, pageable);
        }

        // 모든 조건이 없는 경우 전체 사용자 조회
        return adminService.findAllUsers(pageable);
    }


    private void addUserDetailsToModel(Model model, Page<UserDTO> userPage) {
        List<UserDTO> userDTOS = userPage.getContent();

        model.addAttribute("userPage", userPage);
        model.addAttribute("userBalanceMap", adminService.getAllUsersBalanceMap(userDTOS));
        model.addAttribute("totalUsers", adminService.countUsersByRole(UserRole.USER));

        // 멤버십 및 시즌 정보 추가
        Map<Long, String> seasonMap = new HashMap<>();
        Map<Long, String> membershipTypeMap = new HashMap<>();
        for (UserDTO user : userDTOS) {
            String userMembershipName = user.getUserMembershipName();
            if (userMembershipName != null && userMembershipName.contains("-")) {
                String[] parts = userMembershipName.split("-");
                seasonMap.put(user.getUserId(), parts[0]);
                membershipTypeMap.put(user.getUserId(), parts[1]);
            }
        }
        model.addAttribute("seasonMap", seasonMap);
        model.addAttribute("membershipTypeMap", membershipTypeMap);
    }

    private void addPaginationToModel(Model model, Page<UserDTO> userPage) {
        int totalPage = userPage.getTotalPages();
        int currentPage = userPage.getNumber();
        List<Integer> barNumbers = paginationService.getPaginationBarNumber(currentPage, totalPage);

        model.addAttribute("pageBars", barNumbers);
    }

    private void addFilterStatusToModel(
            Model model, String searchCriteria, String keyword,
            String membership, String status, String season) {

        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("keyword", keyword);
        model.addAttribute("membership", membership);
        model.addAttribute("status", status);
        model.addAttribute("season", season);
    }

    // ============================================================================================
    //유저 세부사항
    @GetMapping("/userDetail/{userId}")
    public String showUserById(@PathVariable("userId") Long userId, Model model) {
        // 유저 정보 조회
        UserDTO user = adminService.findUserById(userId);
        model.addAttribute("user", user);
        return "userDetail";
    }

    // 유저 선택 상태 수정
    @PostMapping("/userDetail/{userId}")
    public String changeStatus(@PathVariable("userId") Long userId) {
        adminService.toggleUserStatus(userId);
        return "redirect:/admin/userDetail/" + userId; // 상태 업데이트 후 해당 사용자 상세 페이지로 리다이렉트
    }

    // ============================================================================================
    // 매출 조회 (총매출, 경기별 매출, 월별 매출)
    @GetMapping("/sales")
    public String getSales(Model model) {
        // 총매출 조회
        Map<String, Map<String, Integer>> totalPayments = adminService.getTotalPaymentAmount();
        // 경기별 매출 조회
        Map<String, List<MatchPaymentDTO>> matchPayments = adminService.getPaymentsByMatchId();
        // 월별 매출 조회(1) 충전금액
        Map<String, Map<String, Long>> monthlyPaymentSales = adminService.getMonthlyPaymentSales();

        // 월별 매출 조회(2) 티켓
        Map<String, Map<String, Long>> monthlyTicketSales = adminService.getMonthlyTicketSales();
//        System.out.println("경기별 매출 조회: " + matchPayments);
//        System.out.println("월별 매출 조회: " + monthlySales);
        // 시즌 정보
        Map<Long, String> seasons = adminService.getSeasonList();
//        System.out.println("시즌 정보:"+seasons);

        model.addAttribute("totalPayments", totalPayments);
        model.addAttribute("matchPayments", matchPayments);
        model.addAttribute("monthlyTicketSales", monthlyTicketSales);
        model.addAttribute("monthlyPaymentSales", monthlyPaymentSales);
        model.addAttribute("seasons", seasons);

        return "sales";
    }

    // ============================================================================================
    // 신규 관리자
    @GetMapping("/createAdmin")
    public String createAdmin() {
        return "createAdmin";
    }

    // 이메일 중복체크 [ 완료 ]
    @GetMapping("/createAdmin/checkEmail")
    @ResponseBody

    public boolean checkEmailDuplicate(@RequestParam("email") String userEmail) {
        return adminService.isEmailDuplicate(userEmail);
    }

    // 전화번호 중복 확인
    @GetMapping("/createAdmin/checkPhone")
    @ResponseBody
    public boolean checkPhoneDuplicate(@RequestParam("phone") String userPhone) {
        return adminService.isPhoneDuplicate(userPhone);
    }

    // 관리자 생성
    @PostMapping("/createAdmin")
    public String createAdmin(@RequestParam("email") String userEmail,
                              @RequestParam("password") String userPassword,
                              @RequestParam("name") String userName,
                              @RequestParam("phone") String userPhone,
                              @RequestParam("address") String userAddress,
                              Model model) {

        UserDTO adminDTO = new UserDTO();
        adminDTO.setUserEmail(userEmail);
        adminDTO.setUserPassword(userPassword);
        adminDTO.setUserName(userName);
        adminDTO.setUserPhone(userPhone);
        adminDTO.setUserAddress(userAddress);
        adminDTO.setUserRole(UserRole.ADMIN);


        adminService.createAdmin(adminDTO);
        return "redirect:/admin/userList"; // 성공적으로 생성되면 첫 페이지로 리다이렉트
    }

    // ============================================================================================
    // 관리자 삭제페이지
    @GetMapping("/deleteAdmin")
    public String deleteAdminPage(Model model) {
        List<Users> adminUsers = userRepository.findByUserRole(UserRole.ADMIN);
        model.addAttribute("users", adminUsers);
        return "deleteAdmin"; // deleteAdmin 페이지로 이동
    }

    // 관리자 삭제
    @DeleteMapping("/deleteAdmin/{userId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable("userId") Long userId) {
        userRepository.findById(userId).ifPresent(userRepository::delete);
        return ResponseEntity.ok().build();
    }
}



