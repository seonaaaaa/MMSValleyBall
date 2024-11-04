package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.*;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO findByEmail(String email) {
        Users user = userRepository.findByUserEmail(email);
        return UserDTO.fromEntity(user);
    }

    public UserDTO findOneByUserId(Long id) {
        Optional<Users> user = userRepository.findById(id);
        if (user.isPresent()) {  // Optional의 존재 여부를 확인
            System.out.println("service - user : " + user.get());
            UserDTO dto = UserDTO.fromEntity(user.get());
            System.out.println("service - userDTO : "+dto.toString());
            return dto;
        }
        return null;
    }

    public Map<String, Object> findMembership(String userEmail) {
        Users user = userRepository.findByUserEmail(userEmail);
        Membership membership = user.getUserMembership();

        //멤버십 id가 5면, membershipType은 24/25-silver, 할인율 10%
        //멤버십 id가 6이면, membershipType은 24/25-gold, 할인율 30%
        String membershipType = "24/25 Bronze";
        int membershipDiscount = 0;
        if (membership.getMembershipId() == 5) {
            membershipType = "24/25 Silver";
            membershipDiscount = 10;
        } else if (membership.getMembershipId() == 6) {
            membershipType = "24/25 Gold";
            membershipDiscount = 30;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("membershipType", membershipType);
        result.put("membershipDiscount", membershipDiscount);
        return result;
    }
}