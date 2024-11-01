package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MembershipDTO;
import com.team.MMSValleyBall.dto.MembershipSalesDTO;
import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.MembershipSalesStatus;
import com.team.MMSValleyBall.repository.MembershipRepository;
import com.team.MMSValleyBall.repository.MembershipSalesRepository;
import com.team.MMSValleyBall.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
            user.get().setUserMembership(membership.get());
        }
        if (membership.isPresent()) {
            newMembership.setMembershipSalesMembership(membership.get());
        }
        newMembership.setMembershipSalesStatus(MembershipSalesStatus.PURCHASE);
        newMembership.setMembershipSalesCreateAt(LocalDateTime.now());
        log.info("### membership sales service - new Membership : " + newMembership);
        //3. 새로운 membershipSales 엔티티 저장
        membershipSalesRepository.save(newMembership);
        //4. user membership 정보 업데이트
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

            return true;
        }
    }
