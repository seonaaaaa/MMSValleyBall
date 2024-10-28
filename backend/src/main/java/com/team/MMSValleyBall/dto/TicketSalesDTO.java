package com.team.MMSValleyBall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TicketSalesDTO {
    private Long ticketId;
    private String userEmail;
    private Long matchId;
    //좌석 구역 id
    private Long ticketDetailSeat;
    //티켓 예매 수량
    private int ticketDetailAmount;
    private int ticketPaidPrice;
    private String ticketNumber;
    private LocalDateTime ticketCreateAt;

    public TicketSalesDTO(Long ticketId, String userEmail, Long matchId, Long ticketDetailSeat, int ticketDetailAmount, int ticketPaidPrice) {
        this.ticketId = ticketId;
        this.userEmail = userEmail;
        this.matchId = matchId;
        this.ticketDetailSeat = ticketDetailSeat;
        this.ticketDetailAmount = ticketDetailAmount;
        this.ticketPaidPrice = ticketPaidPrice;
    }
}