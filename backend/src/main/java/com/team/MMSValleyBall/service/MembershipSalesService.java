package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.repository.MembershipRepository;
import com.team.MMSValleyBall.repository.MembershipSalesRepository;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MembershipSalesService {
    private final MembershipSalesRepository membershipSalesRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;

    public MembershipSalesService(MembershipSalesRepository membershipSalesRepository, MembershipRepository membershipRepository, UserRepository userRepository) {
        this.membershipSalesRepository = membershipSalesRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    public void saveMembership(MembershipSalesDTO membershipSalesDTO) {
        //1. membershipSales 에 필요한 엔티티 추가
        Optional<Users> user = userRepository.findById(membershipSalesDTO.getMembershipSalesUserId());
        Optional<Membership> membership = membershipRepository.findById(membershipSalesDTO.getMembershipSalesMembershipId());
        //2. 새로운 membershipSales 엔티티 생성
        MembershipSales newMembership = new MembershipSales();
        if (user.isPresent()) {
            newMembership.setMembershipSalesUser(user.get());
        }
        if (membership.isPresent()) {
            newMembership.setMembershipSalesMembership(membership.get());
        }
        //3. 새로운 membershipSales 엔티티 저장
        membershipSalesRepository.save(newMembership);
    }
}
