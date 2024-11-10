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

    @GetMapping("/signup/check/email")
    public ResponseEntity<String> checkEmail(String userEmail) {
        String message = signupService.checkEmail(userEmail);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> joinProcess(UserDTO userDTO) {
        System.out.println(userDTO);
        signupService.signupProcess(userDTO);
        return ResponseEntity.ok("회원가입이 페이지입니다.");
    }
}