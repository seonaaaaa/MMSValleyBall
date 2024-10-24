package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MembershipDTO;
import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.service.MembershipService;
import com.team.MMSValleyBall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/membership")
public class MembershipController {
    @Autowired
    MembershipService membershipService;
    @Autowired
    UserService userService;

    @GetMapping("/info")
    public String viewMembership() {
        return "info";
    }

    @GetMapping("/purchase")
    public String viewMembershipPurchase(Model model) {
        //1. 로그인 정보 확인 -> 로그인 안된 상태면 로그인 화면으로 이동

        //2. 세션으로 user 정보 가져와서 dto로 보내기
        Long id = 2L;
        UserDTO userDTO = userService.findOneByUserId(id);
//        System.out.println("controller - user :"+userDTO.toString());
        model.addAttribute("user", userDTO);


        //3. membershipSalesDTO 보내기
        MembershipSalesDTO dto = new MembershipSalesDTO();
        dto.setMembershipSalesUser(userDTO);
//        System.out.println("user 충전금액 : "+ userDTO.getPayments());
        model.addAttribute("membership", dto);
        return "purchase";
    }

//    @PostMapping("/purchase/completed")
//    public String membershipPurchase(@ModelAttribute("membership") MembershipSalesDTO membership, BindingResult result) {
//        return "mypage";
//    }
}
