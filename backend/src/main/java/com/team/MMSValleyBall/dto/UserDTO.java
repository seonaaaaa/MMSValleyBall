package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.*;
import com.team.MMSValleyBall.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String userEmail;
    private String userPassword;
    private String userName;
    private String userPhone;
    private String userAddress;
    private String userMembershipName; // 출력 편의를 위해 멤버십 이름으로 변경
    private UserRole userRole = UserRole.USER;
    private LocalDateTime userCreateAt;
    private LocalDateTime userUpdateAt;
    private List<TicketDTO> tickets;
    private List<PaymentDTO> payments;
    private List<MembershipSalesDTO> membershipSales;

    @Override
    public String toString() {
        return "UserDTO{" +
                "Id=" + userId +
                ", Email='" + userEmail + '\'' +
                ", Password='" + userPassword + '\'' +
                ", Name='" + userName + '\'' +
                ", Phone='" + userPhone + '\'' +
                ", Address='" + userAddress + '\'' +
                ", Membership=" + userMembershipName +
                ", Role=" + userRole +
                ", CreateAt=" + userCreateAt +
                ", UpdateAt=" + userUpdateAt +
                ", TicketsCount=" + (tickets != null ? tickets.size() : 0) +
                ", PaymentsCount=" + (payments != null ? payments.size() : 0) +
                ", MembershipSalesCount=" + (membershipSales != null ? membershipSales.size() : 0) +
                '}' + '\n';
    }


    public static UserDTO fromEntity(Users user){
        List<TicketDTO> ticketDTOList = null;
        List<Ticket> ticketList = user.getTickets();
        if(!ObjectUtils.isEmpty(ticketList)){
            ticketDTOList = ticketList.stream().map(ticket -> TicketDTO.fromEntity(ticket)).toList();
        }
        List<PaymentDTO> paymentDTOList = null;
        List<Payment> paymentList = user.getPayments();
        if(!ObjectUtils.isEmpty(paymentList)){
            paymentDTOList = paymentList.stream().map(payment -> PaymentDTO.fromEntity(payment)).toList();
        }
        List<MembershipSalesDTO> membershipSalesDTOList = null;
        List<MembershipSales> membershipSalesList = user.getMembershipSales();
        if(!ObjectUtils.isEmpty(membershipSalesList)){
            membershipSalesDTOList = membershipSalesList.stream().map(membershipSales -> MembershipSalesDTO.fromEntity(membershipSales)).toList();
        }
        return new UserDTO(
                user.getUserId(),
                user.getUserEmail(),
                user.getUserPassword(),
                user.getUserName(),
                user.getUserPhone(),
                user.getUserAddress(),
                user.getUserMembership().getMembershipName(),
                user.getUserRole(),
                user.getUserCreateAt(),
                user.getUserUpdateAt(),
                ticketDTOList,
                paymentDTOList,
                membershipSalesDTOList
        );
    }
}
