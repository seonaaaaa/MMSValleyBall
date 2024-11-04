package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.entity.*;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.PaymentStatus;
import com.team.MMSValleyBall.enums.TicketStatus;
import com.team.MMSValleyBall.enums.UserStatus;
import com.team.MMSValleyBall.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MyPageService {
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipSalesRepository membershipSalesRepository;
    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final MatchRepository matchRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MyPageService(UserRepository userRepository, MembershipRepository membershipRepository, MembershipSalesRepository membershipSalesRepository, PaymentRepository paymentRepository, TicketRepository ticketRepository, SeatRepository seatRepository, MatchRepository matchRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.membershipSalesRepository = membershipSalesRepository;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.matchRepository = matchRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDTO findByEmail(String email) {
        return UserDTO.fromEntity(userRepository.findByUserEmail(email));
    }

    public List<Reservation> getReservationList(String email){
        List<Reservation>reservationList = new ArrayList<>();
        UserDTO user = UserDTO.fromEntity(userRepository.findByUserEmail(email));
        if(!ObjectUtils.isEmpty(user.getTickets())){
            List<TicketDTO> ticketList = user.getTickets().stream().sorted(Comparator.comparing(TicketDTO::getTicketId).reversed()).toList();
            for(TicketDTO dto: ticketList){
                Reservation reservation = new Reservation();
                reservation.setTicket(dto);
                Match match = matchRepository.findById(dto.getTicketMatchId()).get();
                reservation.setOpponentTeam(match.getMatchOpponentTeam().getTeamName());
                reservation.setOpponentTeamStadium(match.getMatchOpponentTeam().getTeamStadium());
                reservation.setWhere(match.getMatchStadium());
                reservation.setMatchDate(String.valueOf(match.getMatchDate()));
                Seat seat = seatRepository.findById(dto.getTicketDetails().get(0).getTicketDetailSeat()).get();
                reservation.setSeatSection(seat.getSeatZone()+"-"+seat.getSeatSection());
                reservationList.add(reservation);
            }
            }
        return reservationList;
    }
    public boolean changeTicketStatusById(Long id){
        Ticket cancelTicket = ticketRepository.findById(id).get();
        if(!ObjectUtils.isEmpty(cancelTicket)){
            cancelTicket.setTicketStatus(TicketStatus.CANCELLED);
            System.out.println(cancelTicket);
            cancelTicket.setTicketUpdateAt(LocalDateTime.now());
            System.out.println(cancelTicket);
            ticketRepository.save(cancelTicket);
            return true;
        }
        return  false;
    }

    public MembershipDTO getUserMembership(String userMembershipName) {
        return MembershipDTO.fromEntity(membershipRepository.findByMembershipName(userMembershipName));
    }

    public List<MembershipSalesDTO> getUserMembershipSalesList(String email) {
        List<MembershipSales> myMembershipSalesList = membershipSalesRepository.findByMembershipSalesUser(userRepository.findByUserEmail(email));
        return myMembershipSalesList.stream().map(x-> MembershipSalesDTO.fromEntity(x)).toList();
    }

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
        if(!ObjectUtils.isEmpty(modifyUser)){
            modifyUser.setUserUpdateAt(LocalDateTime.now());
            if(userDTO.getUserPassword() != null) {
                if (bCryptPasswordEncoder.matches(userDTO.getUserPassword(), modifyUser.getUserPassword())) {
                    return "기존 비밀번호입니다.";
                }
                modifyUser.setUserPassword(bCryptPasswordEncoder.encode(userDTO.getUserPassword()));
                userRepository.save(modifyUser);
                return "비밀번호가 변경되었습니다.";
            }else {
                modifyUser.setUserPhone(userDTO.getUserPhone());
                modifyUser.setUserAddress(userDTO.getUserAddress());
                userRepository.save(modifyUser);
                return "수정되었습니다!";
            }
        }
        return "수정에 실패하였습니다!";
    }

    public Boolean isPhoneValid(UserDTO userDTO){
        return ObjectUtils.isEmpty(userRepository.findByUserPhone(userDTO.getUserPhone()));
    }

    public String deactivateUser(String email) {
        Users deactivateUser = userRepository.findByUserEmail(email);
        if(ObjectUtils.isEmpty(deactivateUser)) return "해당 회원이 존재하지 않습니다.";
        deactivateUser.setUserStatus(UserStatus.INACTIVE); // 찾은 회원 비활성화 상태로 변경
        userRepository.save(deactivateUser);
        return "회원 탈퇴가 완료되었습니다.";
    }

    public String topUp(Recharge recharge) {
        Payment payment = new Payment();
        payment.setPaymentUser(userRepository.findByUserEmail(recharge.getEmail()));
        payment.setPaymentCreateAt(LocalDateTime.now());
        payment.setPaymentAmount(recharge.getAmount());
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
        return "충전되었습니다.";
    }
}
