package com.team.MMSValleyBall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoneyDTO {
    private Long userId;
    private int paymentAmount;
    private int membershipPrice;
    private int ticketPaidPrice;
    private int leftMoney;

    public MoneyDTO(Long userId, int paymentAmount, int membershipPrice, int ticketPaidPrice) {
        this.userId = userId;
        this.paymentAmount = paymentAmount;
        this.membershipPrice = membershipPrice;
        this.ticketPaidPrice = ticketPaidPrice;
        this.leftMoney = this.paymentAmount - this.membershipPrice - this.ticketPaidPrice;
    }

    // Setter 메서드에서 leftMoney 자동 계산
    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
        calculateLeftMoney();
    }

    public void setMembershipPrice(int membershipPrice) {
        this.membershipPrice = membershipPrice;
        calculateLeftMoney();
    }

    public void setTicketPaidPrice(int ticketPaidPrice) {
        this.ticketPaidPrice = ticketPaidPrice;
        calculateLeftMoney();
    }

    // leftMoney 계산 메서드
    private void calculateLeftMoney() {
        this.leftMoney = this.paymentAmount - this.membershipPrice - this.ticketPaidPrice;
    }

    public int getLeftMoney() {
        return leftMoney;
    }
}
