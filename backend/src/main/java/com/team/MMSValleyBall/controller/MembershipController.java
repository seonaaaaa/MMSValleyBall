package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.dto.MoneyDTO;
import com.team.MMSValleyBall.service.MembershipSalesService;
import com.team.MMSValleyBall.service.UserService;
import com.team.MMSValleyBall.service.UsersBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/membership")
@Slf4j
public class MembershipController {
    @Autowired
    MembershipSalesService membershipSalesService;
    @Autowired
    UserService userService;
    @Autowired
    UsersBalanceService usersBalanceService;

    // 멤버십 정보 보기
    @GetMapping("/info")
    public ResponseEntity<String> viewMembership() {
        return ResponseEntity.ok("Membership Info");
    }

    // 멤버십 구매 페이지 정보 가져오기
    @GetMapping("/purchase")
    public ResponseEntity<?> viewMembershipPurchase(@RequestParam String email) {
        try {
            // 1. 세션에서 보낸 RequestParam으로 email 받아옴.
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid email parameter");
            }

            // 2. user email로 user의 moneyDTO 생성
            MoneyDTO moneyDTO = usersBalanceService.getUsersBalance(email);
            log.info("### controller - moneyDTO : " + moneyDTO.getLeftMoney());

            // 3. 응답 데이터 생성 - 새로운 membershipSalesDTO에 유저 아이디 저장해서 보내기
            MembershipSalesDTO membershipSalesDTO = new MembershipSalesDTO();
            membershipSalesDTO.setMembershipSalesUserId(moneyDTO.getUserId());
            Map<String, Object> response = new HashMap<>();
            response.put("userMoney", moneyDTO.getLeftMoney());
            response.put("newMembership", membershipSalesDTO);
            log.info("### user Money : "+response.get("userMoney"));
            log.info("### new Membership : "+response.get("newMembership"));

            return ResponseEntity.ok(response); // JSON으로 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user data");
        }
    }

    // 멤버십 구매 완료 POST API
    @PostMapping("/purchase/completed")
    public ResponseEntity<?> membershipPurchase(@RequestBody MembershipSalesDTO newMembership) {
        // 유효성 검사
        if (newMembership == null || newMembership.getMembershipSalesMembershipId() == null || newMembership.getMembershipSalesUserId() == null) {
            return ResponseEntity.badRequest().body("Invalid membership data");
        }

        try {
            // 로그 출력
            log.info("Membership ID: {}", newMembership.getMembershipSalesMembershipId());
            log.info("User ID: {}", newMembership.getMembershipSalesUserId());
            log.info("membership_sales_create_at: {}", newMembership.getMembershipSalesCreateAt());

            // 멤버십 구매 정보 저장
            membershipSalesService.saveMembership(newMembership);

            return ResponseEntity.ok("Purchase completed successfully!");
        } catch (DataAccessException dae) {
            log.error("Data access error while completing purchase: {}", dae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error completing purchase");
        } catch (Exception e) {
            log.error("Unexpected error while completing purchase: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error completing purchase");
        }
    }

}