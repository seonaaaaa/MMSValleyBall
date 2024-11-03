package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MembershipDTO;
import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.dto.MembershipSalesInfo;
import com.team.MMSValleyBall.dto.SeasonDTO;
import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.MembershipSalesStatus;
import com.team.MMSValleyBall.repository.MembershipRepository;
import com.team.MMSValleyBall.repository.MembershipSalesRepository;
import com.team.MMSValleyBall.repository.SeasonRepository;
import com.team.MMSValleyBall.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MembershipSalesService {
    private final MembershipSalesRepository membershipSalesRepository;
    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final EntityManager em;

    public MembershipSalesService(MembershipSalesRepository membershipSalesRepository, MembershipRepository membershipRepository, UserRepository userRepository, SeasonRepository seasonRepository, EntityManager em) {
        this.membershipSalesRepository = membershipSalesRepository;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.seasonRepository = seasonRepository;
        this.em = em;
    }

    public void saveMembership(MembershipSalesInfo membershipSalesInfo) {
        //1. membershipSales 에 필요한 엔티티 추가
        Users user = userRepository.findByUserEmail(membershipSalesInfo.getEmail());
        String seasonName = seasonRepository.findAll().get(seasonRepository.findAll().size()-1).getSeasonName();
        Membership membership = membershipRepository.findByMembershipName(seasonName + "-" + membershipSalesInfo.getMembership());
        //2. 새로운 membershipSales 엔티티 생성
        MembershipSales newMembership = new MembershipSales();
        if (!ObjectUtils.isEmpty(user) && !ObjectUtils.isEmpty(membership)) {
            newMembership.setMembershipSalesUser(user);
            user.setUserMembership(membership);
            newMembership.setMembershipSalesMembership(membership);
            newMembership.setMembershipSalesStatus(MembershipSalesStatus.PURCHASE);
            newMembership.setMembershipSalesCreateAt(LocalDateTime.now());
            membershipSalesRepository.save(newMembership);
        }
    }

    // 사용자의 멤버십 결제 내역
    public List<MembershipSalesDTO> myMembershipSalesList(String email) {
        List<MembershipSales> membershipSalesList = membershipSalesRepository.findByMembershipSalesUser(userRepository.findByUserEmail(email));
        return membershipSalesList.stream().map(x -> MembershipSalesDTO.fromEntity(x)).toList();
    }

    // 사용자가 가진 멤버십이름으로 멤버십의 정보 불러오기
    public MembershipDTO getUsersMembership(String userMembershipName) {
        return MembershipDTO.fromEntity(membershipRepository.findByMembershipName(userMembershipName));
    }

    // 멤버십 결제 취소
    public boolean changeMembershipStatusByEmail(String userEmail) {
        // Users 엔티티 찾기
        Users user = userRepository.findByUserEmail(userEmail);
        if (user == null) {
            return false;
        }

        // user와 관련된 membershipSales 엔티티 리스트 찾기
        List<MembershipSales> membershipSalesList = membershipSalesRepository.findByMembershipSalesUser(user);
        if (membershipSalesList.isEmpty()) {
            return false;
        }

        // status를 REFUNDED로 변경
        for (MembershipSales membershipSales : membershipSalesList) {
            membershipSales.setMembershipSalesStatus(MembershipSalesStatus.REFUNDED);
            membershipSalesRepository.save(membershipSales);
        }

        String jpql = "SELECT m FROM Membership m " +
                "JOIN m.membershipSeason s " +
                "WHERE s.id = (SELECT MAX(s2.id) FROM Season s2) AND m.membershipPrice = 0";

        Membership bronzeMembership = em.createQuery(jpql, Membership.class)
                .setMaxResults(1)  // 가장 최근 데이터 1개만 가져오기
                .getSingleResult();

        user.setUserMembership(bronzeMembership);
        userRepository.save(user);

        return true;
    }
    }
