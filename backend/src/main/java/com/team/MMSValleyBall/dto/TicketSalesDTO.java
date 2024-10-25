package com.team.MMSValleyBall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TicketSalesDTO {
    private Long ticketId;
    private Long userId;
    private Long matchId;
    private Long ticketDetailSeat;
    private int ticketDetailAmount;
    private int ticketPaidPrice;
    private String ticketNumber;
    private LocalDateTime ticketCreateAt;

    public TicketSalesDTO(Long ticketId, Long userId, Long matchId, Long ticketDetailSeat, int ticketDetailAmount, int ticketPaidPrice) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.matchId = matchId;
        this.ticketDetailSeat = ticketDetailSeat;
        this.ticketDetailAmount = ticketDetailAmount;
        this.ticketPaidPrice = ticketPaidPrice;
    }
}