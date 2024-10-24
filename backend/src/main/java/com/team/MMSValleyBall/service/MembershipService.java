package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.repository.MembershipRepository;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MembershipService {
    @Autowired
    MembershipRepository membershipRepository;
    @Autowired
    UserRepository userRepository;

    public UserDTO findOneByUserId(Long id) {
        Optional<Users> user = userRepository.findById(id);
        System.out.println(user.toString());
        return null;
    }
}
