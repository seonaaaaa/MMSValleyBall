package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchPaymentDTO;
import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.Season;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.service.AdminService;
import com.team.MMSValleyBall.service.PaginationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:8080") // 클라이언트 주소를 명시
public class AdminController {
    private final AdminService adminService;
    private final PaginationService paginationService;

    public AdminController(AdminService adminService, PaginationService paginationService) {
        this.adminService = adminService;
        this.paginationService = paginationService;
    }

    //유저 선택 조회
    @GetMapping("/userDetail/{userId}")
    public String showUserById(@PathVariable("userId") Long userId, Model model) {
        // 유저 정보 조회
        UserDTO user = adminService.findUserById(userId);
        model.addAttribute("user", user);
        return "userDetail";
    }

    @GetMapping("/userList")
    public String getUsers(
            @RequestParam(name = "searchCriteria", required = false) String searchCriteria,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(value = "membership", required = false) String membership,
            @PageableDefault(page = 0, size = 5, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {

        Page<UserDTO> userPage;

        if (membership != null && !membership.isEmpty()) {
            userPage = adminService.findUsersByMembership(membership, pageable);
        } else if (searchCriteria != null && keyword != null && !keyword.trim().isEmpty()) {
            // 유저 검색 요청
            userPage = adminService.searchUsers(searchCriteria, keyword, pageable);
        } else {
            userPage = adminService.findAllUsers(pageable);
        }

        model.addAttribute("userPage", userPage);

        // 검색 조건과 키워드를 모델에 추가하여 페이지 이동 시에도 값 유지
        model.addAttribute("searchCriteria", searchCriteria);
        model.addAttribute("keyword", keyword);
        model.addAttribute("membership", membership);

        // 페이지 블럭 처리
        int totalPage = userPage.getTotalPages();
        int currentPage = userPage.getNumber();

        List<Integer> barNumbers = paginationService.getPaginationBarNumber(currentPage, totalPage);
        model.addAttribute("pageBars", barNumbers);

        // 유저 수 합계
        long totalUsers = adminService.countUsers();
        model.addAttribute("totalUsers", totalUsers);

        // 필터링된 상태를 유지하기 위해 파라미터를 URL에 추가
        model.addAttribute("pageableParams", "&membership=" + (membership != null ? membership : "") +
                "&searchCriteria=" + (searchCriteria != null ? searchCriteria : "") +
                "&keyword=" + (keyword != null ? keyword : ""));

        return "userList";
    }

    @PostMapping("/userDetail/{userId}")
    public String changeStatus(@PathVariable("userId") Long userId) {
        adminService.toggleUserStatus(userId);
        return "redirect:/admin/userDetail/" + userId; // 상태 업데이트 후 해당 사용자 상세 페이지로 리다이렉트
    }

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

    // 신규 관리자
    @GetMapping("/createAdmin")
    public String createAdmin(Model model) {
        return "createAdmin";
    }

    // 관리자
    @PostMapping("/createAdmin")
    public String updateAdmin(){
        return "createAdmin";
    }
}
