package com.team.MMSValleyBall.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "membership")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long membershipId;

    @Column(nullable = false, unique = true, length = 50)
    private String membershipName;

    @Column(nullable = false)
    private int membershipPrice;

    @ManyToOne
    @JoinColumn(name = "membership_season_id")
    private Season membershipSeason;

    @OneToMany(mappedBy = "userMembership", fetch = FetchType.EAGER)
    private List<Users> users;

    @OneToMany(mappedBy = "membershipSalesMembership", fetch = FetchType.EAGER)
    private List<MembershipSales> membershipSales;

    @Override
    public String toString() {
        return "Membership{" +
                "Id=" + membershipId +
                ", Name=" + membershipName +
                ", Price=" + membershipPrice +
                ", EndDate=" + membershipSeason.getSeasonEndDate() +
                ", Users=" + users +
                ", MembershipSales=" + membershipSales +
                '}'+'\n';
    }
}
