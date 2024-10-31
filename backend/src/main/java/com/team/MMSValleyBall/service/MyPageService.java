package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.entity.Ticket;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.PaymentStatus;
import com.team.MMSValleyBall.enums.UserStatus;
import com.team.MMSValleyBall.repository.MembershipRepository;
import com.team.MMSValleyBall.repository.MembershipSalesRepository;
import com.team.MMSValleyBall.repository.PaymentRepository;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MyPageService {
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipSalesRepository membershipSalesRepository;
    private final PaymentRepository paymentRepository;

    public MyPageService(UserRepository userRepository, MembershipRepository membershipRepository, MembershipSalesRepository membershipSalesRepository, PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.membershipSalesRepository = membershipSalesRepository;
        this.paymentRepository = paymentRepository;
    }

    public UserDTO findByEmail(String email) {
        return UserDTO.fromEntity(userRepository.findByUserEmail(email));
    }

    public MembershipDTO getUserMembership(String userMembershipName) {
        return MembershipDTO.fromEntity(membershipRepository.findByMembershipName(userMembershipName));
    }

    public List<MembershipSalesDTO> getUserMembershipSalesList(String email) {
        List<MembershipSales> myMembershipSalesList = membershipSalesRepository.findByMembershipSalesUser(userRepository.findByUserEmail(email));
        return myMembershipSalesList.stream().map(x-> MembershipSalesDTO.fromEntity(x)).toList();
    }

//    public ResponseMembershipInfoDTO getUserCurrentMembership(String email) {
//        Map<String, Object> response = new HashMap<>();
//        // 사용자가 가진 멤버십의 정보
//        MembershipDTO usersMembership = getUserMembership(findByEmail(email).getUserMembershipName());
//        System.out.println(usersMembership);
//        // 멤버십의 가격 담기
//        response.put("price", usersMembership.getMembershipPrice());
//        System.out.println(response);
//        // 출력에 불필요한 정보 삭제
//        usersMembership.setUsers(null);
//        if (usersMembership.getMembershipPrice() != 0) {
//            // 사용자의 멤버십 결제 정보
//            List<MembershipSalesDTO> userMembershipSalesList = getUserMembershipSalesList(email);
//            // 마지막 결제 정보만 담아주기
//            List<MembershipSalesDTO> userRecentMembershipSales = new ArrayList<>();
//            if (userMembershipSalesList.size() > 0) {
//                userRecentMembershipSales.add(userMembershipSalesList.get(userMembershipSalesList.size() - 1));
//            } else {
//                userRecentMembershipSales = null;
//            }
//            usersMembership.setMembershipSales(userRecentMembershipSales);
//        } else {
//            // 멤버십이 가진 판매내역은 해당 사용자의 판매내역만 가지고 있는게 아니므로 삭제
//            usersMembership.setMembershipSales(null);
//        }
//        response.put("myMembership", usersMembership);
//        System.out.println(response);
//        return response;
//    }

    public ResponseMembershipInfoDTO getUserCurrentMembership(String email) {
        // 사용자가 가진 멤버십 정보 가져오기
        MembershipDTO usersMembership = getUserMembership(findByEmail(email).getUserMembershipName());
        System.out.println(usersMembership);

        // 사용자의 멤버십 가격 정보 설정
        int membershipPrice = usersMembership.getMembershipPrice();
        System.out.println("Membership Price: " + membershipPrice);

        // 출력에 불필요한 정보 삭제
        usersMembership.setUsers(null);

        // ResponseMembershipInfoDTO 생성
        ResponseMembershipInfoDTO responseDTO = new ResponseMembershipInfoDTO();
        responseDTO.setMembershipName(usersMembership.getMembershipName());
        responseDTO.setMembershipPrice(membershipPrice);

        if (membershipPrice != 0) {
            // 사용자의 멤버십 결제 정보 가져오기
            List<MembershipSalesDTO> userMembershipSalesList = getUserMembershipSalesList(email);

            // 마지막 결제 정보만 담아주기
            if (!userMembershipSalesList.isEmpty()) {
                MembershipSalesDTO recentMembershipSale = userMembershipSalesList.get(userMembershipSalesList.size() - 1);
                responseDTO.setMembershipSalesStatus(String.valueOf(recentMembershipSale.getMembershipSalesStatus()));
                responseDTO.setMembershipSalesCreateAt(recentMembershipSale.getMembershipSalesCreateAt());
            }
        }

        System.out.println(responseDTO);
        return responseDTO;
    }


    public String modifyUserInfo(UserDTO userDTO) {
        // 수정할 유저의 정보를 불러오기
        Users modifyUser = userRepository.findByUserEmail(userDTO.getUserEmail());
        System.out.println(modifyUser);
        if(!ObjectUtils.isEmpty(modifyUser)){
            // 수정 가능한 항목(비밀번호, 주소, 핸드폰번호)만 set
            modifyUser.setUserPassword(userDTO.getUserPassword());
            modifyUser.setUserAddress(userDTO.getUserAddress());
            modifyUser.setUserPhone(userDTO.getUserPhone());
            userRepository.save(modifyUser);
            return "수정되었습니다!";
        }else {
            return "수정에 실패하였습니다!";
        }
    }

    public String deactivateUser(Long userId) {
        Users deactivateUser = userRepository.findById(userId).orElse(null);
        if(ObjectUtils.isEmpty(deactivateUser)) return "해당 회원이 존재하지 않습니다.";
        deactivateUser.setUserStatus(UserStatus.INACTIVE); // 찾은 회원 비활성화 상태로 변경
        return "회원 탈퇴 성공";
    }

    public String topUp(Map<String, String> data) {
        Payment payment = new Payment();
        payment.setPaymentUser(userRepository.findByUserEmail(data.get("email")));
        payment.setPaymentCreateAt(LocalDateTime.now());
        payment.setPaymentAmount(Integer.parseInt(data.get("amount")));
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
        return "충전되었습니다.";
    }
}
