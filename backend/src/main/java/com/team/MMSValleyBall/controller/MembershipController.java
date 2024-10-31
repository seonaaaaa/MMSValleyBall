package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.dto.MoneyDTO;
import com.team.MMSValleyBall.service.MembershipSalesService;
import com.team.MMSValleyBall.service.UserService;
import com.team.MMSValleyBall.service.UsersBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/membership")
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
    public ResponseEntity<?> viewMembershipPurchase() {
        try {
            // 세션에서 유저 ID를 가져옴 (로그인 시 세션에 저장된다고 가정)
//            String email = (String) session.getAttribute("userEmail");
//            if (email == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
//            }
            // 1. 로그인 정보 확인 -> 로그인 안된 상태면 예외 처리
            String email = "kimka@cbc.com"; // 추후 세션에서 유저 정보를 가져오는 방식으로 수정 필요
            // 2. 세션으로 user 정보 가져와서 user의 moneyDTO 생성
            MoneyDTO moneyDTO = usersBalanceService.getUsersBalance(email);
            System.out.println("controller - moneyDTO : " + moneyDTO.getLeftMoney());

            // 3. 응답 데이터 생성 - 새로운 membershipSalesDTO에 유저 아이디 저장해서 보내기
            MembershipSalesDTO membershipSalesDTO = new MembershipSalesDTO();
            membershipSalesDTO.setMembershipSalesUserId(moneyDTO.getUserId());
            Map<String, Object> response = new HashMap<>();
            response.put("userMoney", moneyDTO.getLeftMoney());
            response.put("newMembership", membershipSalesDTO);
            System.out.println("user Money : "+response.get("userMoney"));
            System.out.println("new Membership : "+response.get("newMembership"));

            return ResponseEntity.ok(response); // JSON으로 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user data");
        }
    }

    // 멤버십 구매 완료 POST API
    @PostMapping("/purchase/completed")
    public ResponseEntity<?> membershipPurchase(@RequestBody MembershipSalesDTO newMembership) {
        // 여기서 실제 멤버십 구매 로직 처리 (서비스 호출 등)
        // 성공적인 구매라면 응답을 리턴
        try {
            // 받은 데이터를 로그로 출력
            System.out.println("Membership ID: " + newMembership.getMembershipSalesMembershipId());
            System.out.println("User ID: " + newMembership.getMembershipSalesUserId());
            // 받은 멤버십 구매 정보 저장
            membershipSalesService.saveMembership(newMembership);
            return ResponseEntity.ok("Purchase completed successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error completing purchase");
        }
    }

    // 멤버십 결제 취소
    @PostMapping("/cancel")
    public ResponseEntity<String> ticketCancel(@RequestParam("userEmail") String userEmail){
        System.out.println(userEmail);
        if (membershipSalesService.changeMembershipStatusByEmail(userEmail)) {
            return ResponseEntity.ok("멤버십 결제가 성공적으로 취소되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("멤버십 취소에 실패했습니다.");
        }
    }
}