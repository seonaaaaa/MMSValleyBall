package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.CustomUserDetails;
import com.team.MMSValleyBall.dto.ResponseDto;
import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.service.MainService;
import com.team.MMSValleyBall.service.UsersBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {
    private final MainService mainService;
    private final UsersBalanceService usersBalanceService;

    public MainController(MainService mainService, UsersBalanceService usersBalanceService) {
        this.mainService = mainService;
        this.usersBalanceService = usersBalanceService;
    }

    @PostMapping("/signup/check/email")
    public String checkEmail(@RequestParam("userEmail") String userEmail) {
        System.out.println(userEmail);
        String message = mainService.checkEmail(userEmail);
        return message;
    }

    @PostMapping("/signup/check/phone")
    public String checkPhone(@RequestParam("userPhone") String userPhone) {
        System.out.println(userPhone);
        String message = mainService.checkPhone(userPhone);
        return message;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> joinProcess(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        mainService.signupProcess(userDTO);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login/check-status")
    public String checkStatus(@RequestParam("userEmail") String userEmail) {
        System.out.println("============"+ userEmail);
        String message = mainService.checkStatus(userEmail);
        System.out.println(message);
        return message;
    }

    @GetMapping("/main")
    public ResponseEntity<ResponseDto> mainP() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        ResponseDto dto = ResponseDto.builder()
                .email(authentication.getName())
                .role(userDetails.getAuthorities().iterator().next().getAuthority())
                .name(userDetails.getName())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dto);
    }

    @PostMapping("/main")
    public ResponseEntity<Map<String, String>> sendUserInfo(@RequestParam("email")String email){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        System.out.println(authentication.getName());
        Map<String, String> body = new HashMap<>();
        // 맵에 담아서 보내기
        body.put("membership", mainService.userMembershipName(email));
        body.put("balance", String.valueOf(usersBalanceService.getUsersBalance(email).getLeftMoney()));
        System.out.println(body.get("balance"));
        return ResponseEntity.ok(body);
    }


}
