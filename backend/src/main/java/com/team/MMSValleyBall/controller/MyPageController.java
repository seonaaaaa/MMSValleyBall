package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MembershipDTO;
import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.service.MembershipService;
import com.team.MMSValleyBall.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("myPage")
public class MyPageController {
    private final UserService userService;
    private final MembershipService membershipService;

    public MyPageController(UserService userService, MembershipService membershipService) {
        this.userService = userService;
        this.membershipService = membershipService;
    }

    // 티켓 예매 탭에서 받을 티켓 예매 내역
    @GetMapping("ticket")
    public ResponseEntity<?> myTicket(){
        // 인증 정보가 가진 이메일로 조회
        UserDTO findUser = userService.findByEmail("won@cbc.com");
        return ResponseEntity.status(HttpStatus.OK).body(findUser.getTickets());
    }

    // 나의 멤버십 탭에서 받을 사용자 멤버십 정보
    @GetMapping("membership")
    public ResponseEntity<?> myMembership(){
        UserDTO findUser = userService.findByEmail("won@cbc.com");
        // 사용자가 가진 멤버십의 정보
        MembershipDTO usersMembership = membershipService.getUsersMembership(findUser.getUserMembershipName());
        usersMembership.setUsers(null); // 출력에 불필요한 정보 삭제
        if(usersMembership.getMembershipPrice() == 0){
            return ResponseEntity.ok(usersMembership);
        }else {
            // 사용자의 멤버십 결제 정보
            List<MembershipSalesDTO> membershipSalesList = membershipService.myMembershipSalesList("won@cbc.com");
            // 마지막 결제 정보만 불러오면 됨
            return ResponseEntity.status(HttpStatus.OK).body(membershipSalesList.get(membershipSalesList.size()-1));
        }
    }

    @GetMapping("info")
    public ResponseEntity<?> myInfo(){
        UserDTO findUser = userService.findByEmail("won@cbc.com");
        findUser.setTickets(null); // 출력에 불필요한 정보 삭제
        return ResponseEntity.status(HttpStatus.OK).body(findUser);
    }
}
