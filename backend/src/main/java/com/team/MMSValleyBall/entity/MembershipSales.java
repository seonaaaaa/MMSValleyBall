package com.team.MMSValleyBall.entity;

import com.team.MMSValleyBall.enums.MembershipSalesStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "membership_sales")
public class MembershipSales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipSalesId;

    @ManyToOne
    @JoinColumn(name = "membership_sales_user_id", nullable = false)
    private Users membershipSalesUser;

    @ManyToOne
    @JoinColumn(name = "membership_sales_membership_id", nullable = false)
    private Membership membershipSalesMembership;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PURCHASE'")
    @Column(nullable = false, length = 50)
    private MembershipSalesStatus membershipSalesStatus;

    @Column(nullable = false)
    private LocalDateTime membershipSalesCreateAt;

    @Column
    private LocalDateTime membershipSalesUpdateAt;

    @Override
    public String toString() {
        return "MembershipSales{" +
                "Id=" + membershipSalesId +
                ", User=" + membershipSalesUser.getUserId() +
                ", Membership=" + membershipSalesMembership.getMembershipName() +
                ", Status=" + membershipSalesStatus +
                ", CreateAt=" + membershipSalesCreateAt +
                ", UpdateAt=" + membershipSalesUpdateAt +
                '}'+'\n';
    }
}
