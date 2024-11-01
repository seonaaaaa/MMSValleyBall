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

    // 유저 조회
    @GetMapping("/userList")
    public String getUsers(
            @PageableDefault(page = 0, size = 5, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            Model model) {

        // 유저 전체 조회
        Page<Users> userPage = adminService.findAllUsers(pageable);

        model.addAttribute("userPage", userPage); // 페이징된 유저 목록을 전달

        // 페이지 블럭 처리
        int totalPage = userPage.getTotalPages();
        int currentPage = userPage.getNumber();

        List<Integer> barNumbers = paginationService.getPaginationBarNumber(currentPage, totalPage);
        model.addAttribute("pageBars", barNumbers);

        // 유저 수 합계
        long totalUsers = userPage.getTotalElements();
        model.addAttribute("totalUsers", totalUsers);

        return "userList";
    }

    @GetMapping("/createAdmin")
    public String createAdmin() {
        return "createAdmin";
    }

    // 매출 조회 (총매출, 경기별 매출, 월별 매출)
    @GetMapping("/sales")
    public String getSales(Model model) {
        // 총매출 조회
        Map<String, Map<String, Integer>> totalPayments = adminService.getTotalPaymentAmount();
        // 경기별 매출 조회
        Map<String, List<MatchPaymentDTO>> matchPayments = adminService.getPaymentsByMatchId();
        // 월별 매출 조회
        Map<String, Map<String, Long>> monthlySales = adminService.getMonthlySales();
//        System.out.println("경기별 매출 조회: " + matchPayments);
//        System.out.println("월별 매출 조회: " + monthlySales);
        // 시즌 정보
        Map<Long, String> seasons = adminService.getSeasonList();
//        System.out.println("시즌 정보:"+seasons);

        model.addAttribute("totalPayments", totalPayments);
        model.addAttribute("matchPayments", matchPayments);
        model.addAttribute("monthlySales", monthlySales);
        model.addAttribute("seasons", seasons);
        return "sales";
    }
}
