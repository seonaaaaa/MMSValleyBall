package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.enums.MembershipSalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembershipSalesDTO {
    private Long membershipSalesId;
    private Long membershipSalesUser;
    private MembershipDTO membershipSalesMembership;
    private MembershipSalesStatus membershipSalesStatus = MembershipSalesStatus.PURCHASE;
    private LocalDateTime membershipSalesCreateAt;
    private LocalDateTime membershipSalesUpdateAt;

    @Override
    public String toString() {
        return "MembershipSalesDTO{" +
                "Id=" + membershipSalesId +
                ", User=" + membershipSalesUser +
                ", Membership=" + (membershipSalesMembership != null ? membershipSalesMembership.getMembershipName() : "null") +
                ", Status=" + membershipSalesStatus +
                ", CreateAt=" + membershipSalesCreateAt +
                ", UpdateAt=" + membershipSalesUpdateAt +
                '}' + '\n';
    }


    public static MembershipSalesDTO fromEntity(MembershipSales membershipSales){
        return new MembershipSalesDTO(
                membershipSales.getMembershipSalesId(),
                membershipSales.getMembershipSalesUser().getUserId(),
                MembershipDTO.fromEntity(membershipSales.getMembershipSalesMembership()),
                membershipSales.getMembershipSalesStatus(),
                membershipSales.getMembershipSalesCreateAt(),
                membershipSales.getMembershipSalesUpdateAt()
        );
    }
}
