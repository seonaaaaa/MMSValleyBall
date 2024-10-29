package com.team.MMSValleyBall.entity;

import com.team.MMSValleyBall.enums.UserRole;
import com.team.MMSValleyBall.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 100)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(unique = true, length = 13)
    private String userPhone;

    @Column
    private String userAddress;

    @ManyToOne
    @JoinColumn(name = "user_membership_id")
    private Membership userMembership;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'USER'")
    @Column(nullable = false)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    @Column(nullable = false)
    private UserStatus userStatus;

    @Column(nullable = false)
    private LocalDateTime userCreateAt;

    @PrePersist
    protected void onCreate() {
        this.userCreateAt = LocalDateTime.now();
    }

    @Column
    private LocalDateTime userUpdateAt;

    @OneToMany(mappedBy = "ticketUser", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "paymentUser", fetch = FetchType.EAGER)
    private List<Payment> payments;

    @OneToMany(mappedBy = "membershipSalesUser", fetch = FetchType.EAGER)
    private List<MembershipSales> membershipSales;

    @Override
    public String toString() {
        return "User{" +
                "Id=" + userId +
                ", Email='" + userEmail + '\'' +
                ", Password='" + userPassword + '\'' +
                ", Name='" + userName + '\'' +
                ", Phone='" + userPhone + '\'' +
                ", Address='" + userAddress + '\'' +
                ", Membership=" + userMembership.getMembershipName() +
                ", Role=" + userRole +
                ", CreateAt=" + userCreateAt +
                ", UpdateAt=" + userUpdateAt +
                ", Tickets=" + tickets +
                ", Payments=" + payments +
                ", MembershipSales=" + membershipSales +
                '}'+'\n';
    }
}
