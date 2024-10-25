package com.team.MMSValleyBall.entity;

import com.team.MMSValleyBall.enums.PaymentStatus;
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
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(nullable = false)
    private int paymentAmount;

    @ManyToOne
    @JoinColumn(name = "payment_user_id", nullable = false)
    private Users paymentUser;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'COMPLETED'")
    @Column(nullable = false, length = 50)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private LocalDateTime paymentCreateAt;

    @Column
    private LocalDateTime paymentUpdateAt;

    @Override
    public String toString() {
        return "Payment{" +
                "Id=" + paymentId +
                ", Amount=" + paymentAmount +
                ", User=" + paymentUser.getUserId() +
                ", Status=" + paymentStatus +
                ", CreateAt=" + paymentCreateAt +
                ", UpdateAt=" + paymentUpdateAt +
                '}'+'\n';
    }
}
