package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.UserDTO;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
