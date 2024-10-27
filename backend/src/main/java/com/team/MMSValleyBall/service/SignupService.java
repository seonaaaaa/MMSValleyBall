package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.UserRole;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class SignupService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    public String checkEmail(String UserEmail) {
        // 이메일 중복 확인
        Boolean isUser = userRepository.existsByUserEmail(UserEmail);
        if (isUser) {
            return "이미 존재하는 이메일입니다.";
        }
        return "사용 가능한 이메일입니다.";
    }

    public void signupProcess(UserDTO userDTO) {
        // 기존 같은 유저가 있는지 확인
        Boolean isUser = userRepository.existsByUserEmail(userDTO.getUserEmail());

        if (isUser) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다."); // 예외를 던져 중복 알림
        }

        // 이번 시즌의 브론즈 멤버십 가져오기
        Long bronzeMembershipId = 2L;
        Membership bronzeMembership = em.find(Membership.class, bronzeMembershipId);


        // 없으면 회원가입
        Users data = new Users();
        data.setUserEmail(userDTO.getUserEmail());
        data.setUserPassword(userDTO.getUserPassword());
        data.setUserName(userDTO.getUserName());
        data.setUserPhone(userDTO.getUserPhone());
        data.setUserAddress(userDTO.getUserAddress());
        data.setUserMembership(bronzeMembership); // 브론즈 멤버십 설정
        data.setUserRole(UserRole.USER);

        userRepository.save(data);
    }
}
