package com.team.MMSValleyBall.controller;


import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 유저 조회 [ 페이징 처리 ]
    @GetMapping("/user-list")
    public ResponseEntity<?> getUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "userMembership", required = false) Membership userMembership,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "sort", defaultValue = "userId") String sortField,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        if (userId != null) {
            // 특정 유저 조회
            UserDTO user = adminService.findUserById(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(user);
        }
        // 정렬 방향 설정
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserDTO> users;
        if (userMembership == null) {
            // 유저 전체 조회
            users = adminService.findAllUsers(pageable);
        } else {
            // 특정 멤버십 유저 조회
            users = adminService.findUsersByUserMembership(userMembership, pageable);
        }
        return ResponseEntity.ok(users);
    }

    // 매출 조회 (총매출, 경기별 매출, 월별 매출)
    @GetMapping("/sales")
    public ResponseEntity<Object> getSales(
            @RequestParam(name = "type", defaultValue = "total") String type) {
        switch (type.toLowerCase()) {
            case "total":
                // 총매출 조회
                int totalPayments = adminService.getTotalPaymentAmount();
                return ResponseEntity.ok(totalPayments);
            case "match":
                // 경기별 매출 조회
                Map<Integer, String> matchPayments = adminService.getPaymentsByMatchId();
                return ResponseEntity.ok(matchPayments);
            case "monthly":
                // 월별 매출 조회
                Map<String, Long> monthlySales = adminService.getMonthlySales();
                return ResponseEntity.ok(monthlySales);
            default:
                // 잘못된 요청 처리
                return ResponseEntity.badRequest().body("Invalid type parameter. Use 'total', 'match', or 'monthly'.");
        }
    }
}
