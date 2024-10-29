package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.service.SignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SignupController {
    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping("/signup/check/email")
    public String checkEmail(@RequestParam("userEmail") String userEmail) {
        System.out.println(userEmail);
        String message = signupService.checkEmail(userEmail);
        return message;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> joinProcess(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        signupService.signupProcess(userDTO);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}