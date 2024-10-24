package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.entity.Users;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    EntityManager em;

    @GetMapping("/")
    public String mainView(){
        System.out.println(em.find(Users.class,1L));
        return "test";
    }
}
