package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.entity.*;
import com.team.MMSValleyBall.enums.PaymentStatus;
import com.team.MMSValleyBall.enums.TicketStatus;
import com.team.MMSValleyBall.enums.UserStatus;
import com.team.MMSValleyBall.repository.*;
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

    public MyPageService(UserRepository userRepository, MembershipRepository membershipRepository, MembershipSalesRepository membershipSalesRepository, PaymentRepository paymentRepository, TicketRepository ticketRepository, SeatRepository seatRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.membershipSalesRepository = membershipSalesRepository;
        this.paymentRepository = paymentRepository;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.matchRepository = matchRepository;
    }

    public UserDTO findByEmail(String email) {
        return UserDTO.fromEntity(userRepository.findByUserEmail(email));
    }

    public List<Reservation> getReservationList(String email){
        List<Reservation>reservationList = new ArrayList<>();
        UserDTO user = UserDTO.fromEntity(userRepository.findByUserEmail(email));
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
        return reservationList;
    }
    public boolean changeTicketStatusById(Long id){
        Ticket cancelTicket = ticketRepository.findById(id).get();
        if(ObjectUtils.isEmpty(cancelTicket)){
            cancelTicket.setTicketStatus(TicketStatus.CANCELLED);
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

    public Map<String, Object> getUserCurrentMembership(String email) {
        Map<String, Object> response = new HashMap<>();
        // 사용자가 가진 멤버십의 정보
        MembershipDTO usersMembership = getUserMembership(findByEmail(email).getUserMembershipName());
        // 멤버십의 가격 담기
        response.put("price", usersMembership.getMembershipPrice());
        // 출력에 불필요한 정보 삭제
        usersMembership.setUsers(null);
        if (usersMembership.getMembershipPrice() != 0) {
            // 사용자의 멤버십 결제 정보
            List<MembershipSalesDTO> userMembershipSalesList = getUserMembershipSalesList(email);
            // 마지막 결제 정보만 담아주기
            List<MembershipSalesDTO> userRecentMembershipSales = new ArrayList<>();
            userRecentMembershipSales.add(userMembershipSalesList.get(userMembershipSalesList.size() - 1));
            usersMembership.setMembershipSales(userRecentMembershipSales);
        } else {
            // 멤버십이 가진 판매내역은 해당 사용자의 판매내역만 가지고 있는게 아니므로 삭제
            usersMembership.setMembershipSales(null);
        }
        response.put("myMembership", usersMembership);
        return response;
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
