package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.service.MyPageService;
import com.team.MMSValleyBall.service.UsersBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<?> userTicket(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(myPageService.getReservationList(authentication.getName()));
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
    @GetMapping("membership")
    public ResponseEntity<ResponseMembershipInfoDTO> userMembership(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ResponseMembershipInfoDTO users = myPageService.getUserCurrentMembership(authentication.getName());
        System.out.println(users);
        return ResponseEntity.ok(users);
    }

    // 나의 정보 탭에서 받을 사용자 정보
    @GetMapping("info")
    public ResponseEntity<UserDTO> userInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO findUser = myPageService.findByEmail(authentication.getName());
        // 출력에 불필요한 정보 삭제
        findUser.setTickets(null);
        findUser.setPayments(null);
        findUser.setMembershipSales(null);
        findUser.setUserPassword(null);
        return ResponseEntity.ok(findUser);
    }

    // 사용자 정보 수정 요청처리
    @PostMapping("info/modify")
    public ResponseEntity<String> modifyUserInfo(@RequestBody UserDTO userDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userDTO.setUserEmail(authentication.getName());
        return ResponseEntity.ok(myPageService.modifyUserInfo(userDTO));
    }

    // 휴대전화 유니크인증
    @PostMapping("info/phone")
    public ResponseEntity<Boolean> isPhoneValid(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(myPageService.isPhoneValid(userDTO));
    }

    // 회원 탈퇴
    @GetMapping("info/deactivate")
    public ResponseEntity<String> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(myPageService.deactivateUser(authentication.getName()));
    }

    // 잔액 충전
    @PostMapping("info/recharge")
    public ResponseEntity<String> topUp(@RequestBody Map<String, String> request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(myPageService.topUp(authentication.getName(), Integer.parseInt(request.get("amount"))));
    }
}
