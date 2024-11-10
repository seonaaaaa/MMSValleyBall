package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByMembershipName(String userMembershipName);

}
