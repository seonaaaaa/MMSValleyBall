package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Payment;
import com.team.MMSValleyBall.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private int paymentAmount;
    private Long paymentUserId;
    private PaymentStatus paymentStatus = PaymentStatus.COMPLETED;
    private LocalDateTime paymentCreateAt;
    private LocalDateTime paymentUpdateAt;

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "Id=" + paymentId +
                ", Amount=" + paymentAmount +
                ", User=" + paymentUserId +
                ", Status=" + paymentStatus +
                ", CreateAt=" + paymentCreateAt +
                ", UpdateAt=" + paymentUpdateAt +
                '}' + '\n';
    }


    public static PaymentDTO fromEntity(Payment payment){
        return new PaymentDTO(
                payment.getPaymentId(),
                payment.getPaymentAmount(),
                payment.getPaymentUser().getUserId(),
                payment.getPaymentStatus(),
                payment.getPaymentCreateAt(),
                payment.getPaymentUpdateAt()
        );
    }
}
