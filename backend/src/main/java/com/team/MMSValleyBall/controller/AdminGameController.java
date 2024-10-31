package com.team.MMSValleyBall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminGameController {

    @GetMapping("/userList")
    public String showUserList() {
        return "userList";
    }
}
