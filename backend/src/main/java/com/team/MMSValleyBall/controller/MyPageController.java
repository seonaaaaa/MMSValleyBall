package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.service.MyPageService;
import com.team.MMSValleyBall.service.UsersBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("myPage")
public class MyPageController {
   private final MyPageService myPageService;
   private final UsersBalanceService usersBalanceService;

    public MyPageController(MyPageService myPageService, UsersBalanceService usersBalanceService) {
        this.myPageService = myPageService;
        this.usersBalanceService = usersBalanceService;
    }

    // 티켓 예매 탭에서 받을 티켓 예매 내역
    @GetMapping("ticket")
    public ResponseEntity<?> userTicket(@RequestParam("email")String email){
        return ResponseEntity.ok(myPageService.getReservationList(email));
    }

    @PostMapping("/ticket/cancel")
    public ResponseEntity<String> ticketCancel(@RequestParam("id") Long id){
       if (myPageService.changeTicketStatusById(id)) {
            return ResponseEntity.ok("예매가 성공적으로 취소되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("예매 취소에 실패했습니다.");
        }
    }

    // 나의 멤버십 탭에서 받을 사용자 멤버십 정보
    @PostMapping("membership")
    public ResponseEntity<ResponseMembershipInfoDTO> userMembership(@RequestParam("email")String email){
        System.out.println(email);
        ResponseMembershipInfoDTO users = myPageService.getUserCurrentMembership(email);
//        Map<String, Object> users = myPageService.getUserCurrentMembership(email);
        System.out.println(users);
        return ResponseEntity.ok(users);
    }

    // 나의 정보 탭에서 받을 사용자 정보
    @GetMapping("info")
    public ResponseEntity<Map<String, Object>> userInfo(@RequestParam("email")String email){
        UserDTO findUser = myPageService.findByEmail(email);
        // 출력에 불필요한 정보 삭제
        findUser.setTickets(null);
        findUser.setPayments(null);
        findUser.setMembershipSales(null);
        // 맵에 담아서 보내기
        Map<String, Object> body = new HashMap<>();
        body.put("user", findUser);
        // 충전 잔액 담기
        body.put("balance", usersBalanceService.getUsersBalance(email).getLeftMoney());
        return ResponseEntity.ok(body);
    }

    // 사용자 정보 수정 요청처리
    @PostMapping("info/modify")
    public ResponseEntity<String> modifyUserInfo(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(myPageService.modifyUserInfo(userDTO));
    }

    // 휴대전화 유니크인증
    @PostMapping("info/phone")
    public ResponseEntity<Boolean> isPhoneValid(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(myPageService.isPhoneValid(userDTO));
    }

    // 회원 탈퇴
    @PatchMapping("info/deactivate")
    public ResponseEntity<String> deleteUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(myPageService.deactivateUser(userDTO.getUserId()));
    }

    // 잔액 충전
    @PostMapping("info/recharge")
    public ResponseEntity<String> topUp(@RequestBody Recharge recharge){
        return ResponseEntity.ok(myPageService.topUp(recharge));
    }
}
