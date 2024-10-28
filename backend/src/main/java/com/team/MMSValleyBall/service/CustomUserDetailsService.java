package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.CustomUserDetails;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
                throws UsernameNotFoundException {
        //DB에서 조회
        Users userData = userRepository.findByUserEmail(email);
        if (userData != null) {
            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }
        return null;
    }
}
