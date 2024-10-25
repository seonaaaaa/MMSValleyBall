package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.service.SignupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SignupController {
    private final SignupService signupService;

    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    @GetMapping("/signup")
    public String joinForm(){
        return "signup";
    }

    @GetMapping("/signup/check/email")
    @ResponseBody
    public ResponseEntity<String> checkEmail(@RequestParam String userEmail) {
        String message = signupService.checkEmail(userEmail);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/signupProc")
    public String joinProcess(UserDTO userDTO) {
        signupService.signupProcess(userDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

}
