package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
   Users findByUserEmail(String userEmail);
}
