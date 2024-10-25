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
    private Long ticketDetailSeat;
    private int ticketDetailAmount;
    private Long ticketDetailTicketId;

    @Override
    public String toString() {
        return "TicketDetailDTO{" +
                "Id=" + ticketDetailId +
                ", SeatId=" + ticketDetailSeat +
                ", Amount=" + ticketDetailAmount +
                ", TicketNumber=" + ticketDetailTicketId +
                '}' + '\n';
    }

    public static TicketDetailDTO fromEntity(TicketDetail ticketDetail){
        return new TicketDetailDTO(
                ticketDetail.getTicketDetailId(),
                ticketDetail.getTicketDetailSeat().getSeatId(),
                ticketDetail.getTicketDetailAmount(),
                ticketDetail.getTicketDetailTicket().getTicketId()
        );
    }
}
