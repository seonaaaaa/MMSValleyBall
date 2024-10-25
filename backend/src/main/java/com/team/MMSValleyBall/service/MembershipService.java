package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.repository.MembershipSalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembershipService {
    private final MembershipSalesRepository membershipRepository;

    public MembershipService(MembershipSalesRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public void saveMembership(MembershipSalesDTO membership) {
        //1. membershipSales 레코드 추가
//        MembershipSales newMembership = new MembershipSales();
//        newMembership.setMembershipSalesUser();
    }
}

