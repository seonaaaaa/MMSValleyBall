package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.TicketDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailDTO {
    private Long ticketDetailId;
    private SeatDTO ticketDetailSeat;
    private int ticketDetailAmount;
    private TicketDTO ticketDetailTicket;

    public static TicketDetailDTO fromEntity(TicketDetail ticketDetail){
        return new TicketDetailDTO(
                ticketDetail.getTicketDetailId(),
                SeatDTO.fromEntity(ticketDetail.getTicketDetailSeat()),
                ticketDetail.getTicketDetailAmount(),
                TicketDTO.fromEntity(ticketDetail.getTicketDetailTicket())
        );
    }
}
