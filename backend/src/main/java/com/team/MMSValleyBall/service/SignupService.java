package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.UserRole;
import com.team.MMSValleyBall.enums.UserStatus;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class SignupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PersistenceContext
    private EntityManager em;

    public String checkEmail(String userEmail) {
        // 이메일 중복 확인
        Boolean isUser = userRepository.existsByUserEmail(userEmail);
        if(userEmail == ""){
            return "null";
        }

        if (isUser) {
            return "False";
        }
        return "True";
    }

    public void signupProcess(UserDTO userDTO) {
        // 이번 시즌의 브론즈 멤버십 가져오기
        Long bronzeMembershipId = 2L;
        Membership bronzeMembership = em.find(Membership.class, bronzeMembershipId);


        // 없으면 회원가입
        Users data = new Users();
        data.setUserEmail(userDTO.getUserEmail());
        data.setUserPassword(
                bCryptPasswordEncoder.encode(userDTO.getUserPassword())
        );
        data.setUserName(userDTO.getUserName());
        data.setUserPhone(userDTO.getUserPhone());
        data.setUserAddress(userDTO.getUserAddress());
        data.setUserMembership(bronzeMembership); // 브론즈 멤버십 설정
        data.setUserRole(UserRole.USER);
        data.setUserStatus(UserStatus.ACTIVE);

        userRepository.save(data);
    }

    public String checkPhone(String userPhone) {
        // 이메일 중복 확인
        Boolean isUser = userRepository.existsByUserPhone(userPhone);
        if(userPhone.equals("010--")){
            return "null";
        }

        if (isUser) {
            return "False";
        }
        return "True";
    }
}
