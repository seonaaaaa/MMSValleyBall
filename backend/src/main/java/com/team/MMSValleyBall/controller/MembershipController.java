package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MembershipDTO;
import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.dto.MoneyDTO;
import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.service.MembershipService;
import com.team.MMSValleyBall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/membership")
public class MembershipController {
    @Autowired
    MembershipService membershipService;
    @Autowired
    UserService userService;

    // 멤버십 정보 보기
    @GetMapping("/info")
    public ResponseEntity<String> viewMembership() {
        return ResponseEntity.ok("Membership Info");
    }

    // 멤버십 구매 페이지 정보 가져오기
    @GetMapping("/purchase")
    public ResponseEntity<?> viewMembershipPurchase() {
        // 1. 로그인 정보 확인 -> 로그인 안된 상태면 예외 처리
        Long id = 14L; // 추후 세션에서 유저 정보를 가져오는 방식으로 수정 필요
        try {
            // 2. 세션으로 user 정보 가져와서 user의 moneyDTO 생성
            MoneyDTO moneyDTO = userService.getUserMoney(id);
            System.out.println("controller - moneyDTO : " + moneyDTO.getLeftMoney());

            // 3. 응답 데이터 생성 - 새로운 membershipSalesDTO에 유저 아이디 저장해서 보내기
            MembershipSalesDTO membershipSalesDTO = new MembershipSalesDTO();
            membershipSalesDTO.setMembershipSalesUserId(id);
            Map<String, Object> response = new HashMap<>();
            response.put("userMoney", moneyDTO);
            response.put("newMembership", membershipSalesDTO);

            return ResponseEntity.ok(response); // JSON으로 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user data");
        }
    }

    // 멤버십 구매 완료 POST API
    @PostMapping("/purchase/completed")
    public ResponseEntity<?> membershipPurchase(@RequestBody MembershipSalesDTO membership) {
        // 여기서 실제 멤버십 구매 로직 처리 (서비스 호출 등)
        // 성공적인 구매라면 응답을 리턴
        try {
            System.out.println(membership.getMembershipSalesMembershipId());
            // 받은 멤버십 구매 정보 저장
            membershipService.saveMembership(membership);
            return ResponseEntity.ok("Purchase completed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error completing purchase");
        }
    }
}