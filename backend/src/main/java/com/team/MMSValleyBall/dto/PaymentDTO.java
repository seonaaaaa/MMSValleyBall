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
    private UserDTO paymentUser;
    private PaymentStatus paymentStatus = PaymentStatus.COMPLETED;
    private LocalDateTime paymentCreateAt;
    private LocalDateTime paymentUpdateAt;

    public static PaymentDTO fromEntity(Payment payment){
        return new PaymentDTO(
                payment.getPaymentId(),
                payment.getPaymentAmount(),
                UserDTO.fromEntity(payment.getPaymentUser()),
                payment.getPaymentStatus(),
                payment.getPaymentCreateAt(),
                payment.getPaymentUpdateAt()
        );
    }
}
