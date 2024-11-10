package com.team.MMSValleyBall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class TicketSalesDTO {
    private Long matchId;
    private Long ticketDetailSeat;
    private int ticketDetailAmount;
    private int ticketPaidPrice;
}
