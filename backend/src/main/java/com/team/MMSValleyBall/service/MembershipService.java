package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.repository.MembershipShipRepository;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {
    private final MembershipShipRepository membershipRepository;

    @Autowired
    public MembershipService(MembershipShipRepository membershipRepository, UserRepository userRepository) {
        this.membershipRepository = membershipRepository;
    }

}

