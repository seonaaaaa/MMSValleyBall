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
    private Long membershipSalesUserId;
    private Long membershipSalesMembershipId;
    private MembershipSalesStatus membershipSalesStatus = MembershipSalesStatus.PURCHASE;
    private LocalDateTime membershipSalesCreateAt;
    private LocalDateTime membershipSalesUpdateAt;

    @Override
    public String toString() {
        return "MembershipSalesDTO{" +
                "Id=" + membershipSalesId +
                ", User=" + membershipSalesUserId +
                ", Membership=" + membershipSalesMembershipId +
                ", Status=" + membershipSalesStatus +
                ", CreateAt=" + membershipSalesCreateAt +
                ", UpdateAt=" + membershipSalesUpdateAt +
                '}' + '\n';
    }


    public static MembershipSalesDTO fromEntity(MembershipSales membershipSales){
        return new MembershipSalesDTO(
                membershipSales.getMembershipSalesId(),
                membershipSales.getMembershipSalesUser().getUserId(),
                membershipSales.getMembershipSalesMembership().getMembershipId(),
                membershipSales.getMembershipSalesStatus(),
                membershipSales.getMembershipSalesCreateAt(),
                membershipSales.getMembershipSalesUpdateAt()
        );
    }
}
