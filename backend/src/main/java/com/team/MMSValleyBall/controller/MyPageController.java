package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.service.MyPageService;
import com.team.MMSValleyBall.service.UsersBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
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
    public ResponseEntity<List<TicketDTO>> userTicket(@RequestParam("email")String email){
        UserDTO findUser = myPageService.findByEmail(email);
        return ResponseEntity.ok(findUser.getTickets());
    }

    // 나의 멤버십 탭에서 받을 사용자 멤버십 정보
    @GetMapping("membership")
    public ResponseEntity<Map<String, Object>> userMembership(@RequestParam("email")String email){
        return ResponseEntity.ok(myPageService.getUserCurrentMembership(email));
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
    @PatchMapping("info/modify")
    public ResponseEntity<String> modifyUserInfo(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(myPageService.modifyUserInfo(userDTO));
    }

    // 회원 탈퇴
    @PatchMapping("info/deactivate")
    public ResponseEntity<String> deleteUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(myPageService.deactivateUser(userDTO.getUserId()));
    }

    // 사용자의 충전 잔액
    @GetMapping("info/recharge")
    public ResponseEntity<Integer> balance(@RequestParam("email")String email){
        return ResponseEntity.ok(usersBalanceService.getUsersBalance(email).getLeftMoney());
    }

    // 잔액 충전
    @PostMapping("info/recharge")
    public ResponseEntity<String> topUp(@RequestBody Map<String, String> data){

        return ResponseEntity.ok(myPageService.topUp(data));
    }
}
