package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MembershipSalesInfo;
import com.team.MMSValleyBall.service.MembershipSalesService;
import com.team.MMSValleyBall.service.UserService;
import com.team.MMSValleyBall.service.UsersBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membership")
@Slf4j
public class MembershipController {
    @Autowired
    MembershipSalesService membershipSalesService;

    // 멤버십 정보 보기
    @GetMapping("/info")
    public ResponseEntity<String> viewMembership() {
        return ResponseEntity.ok("Membership Info");
    }

    // 멤버십 구매 완료 POST API
    @PostMapping("/purchase")
    public ResponseEntity<?> membershipPurchase(@RequestBody MembershipSalesInfo membershipSalesInfo) {
        try {
            membershipSalesService.saveMembership(membershipSalesInfo);
            return ResponseEntity.ok("Purchase completed successfully!");
        } catch (DataAccessException dae) {
            log.error("Data access error while completing purchase: {}", dae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error completing purchase");
        } catch (Exception e) {
            log.error("Unexpected error while completing purchase: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error completing purchase");
        }
    }

    // 멤버십 결제 취소
    @PostMapping("/cancel")
    public ResponseEntity<String> ticketCancel(@RequestParam("userEmail") String userEmail) {
        System.out.println(userEmail);
        if (membershipSalesService.changeMembershipStatusByEmail(userEmail)) {
            return ResponseEntity.ok("멤버십 결제가 성공적으로 취소되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("예매 취소에 실패했습니다.");
        }
    }
}