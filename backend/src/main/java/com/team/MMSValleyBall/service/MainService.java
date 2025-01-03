package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.UserRole;
import com.team.MMSValleyBall.enums.UserStatus;
import com.team.MMSValleyBall.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MainService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EntityManager em;

    public MainService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EntityManager em) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.em = em;
    }

    public String checkEmail(String userEmail) {
        // 이메일 중복 확인
        if (userEmail.isEmpty()) {
            return "null";
        }
        Boolean isUser = userRepository.existsByUserEmail(userEmail);
        return isUser ? "False" : "True";
    }

    public String checkPhone(String userPhone) {
        // 전화번호 중복 확인
        if (userPhone.equals("010--")) {
            return "null";
        }

        Boolean isUser = userRepository.existsByUserPhone(userPhone);
        return isUser ? "False" : "True";
    }

    public void signupProcess(UserDTO userDTO) {
        String jpql = "SELECT m FROM Membership m " +
                "JOIN m.membershipSeason s " +
                "WHERE s.id = (SELECT MAX(s2.id) FROM Season s2) AND m.membershipPrice = 0";

        Membership bronzeMembership = em.createQuery(jpql, Membership.class)
                .setMaxResults(1)  // 가장 최근 데이터 1개만 가져오기
                .getSingleResult();

        System.out.println(bronzeMembership);

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

    public String userMembershipName(String email) {
        UserDTO userData = UserDTO.fromEntity(userRepository.findByUserEmail(email));
        return userData.getUserMembershipName().split("-")[1];
    }

    public String checkStatus(String userEmail) {
        // 이메일로 사용자 찾기
        Users user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return "User not found.";
        }
        String userStatus = String.valueOf(user.getUserStatus());
        return String.valueOf(user.getUserStatus());
    }
}
