package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Ticket;
import com.team.MMSValleyBall.entity.TicketDetail;
import com.team.MMSValleyBall.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private Long ticketId;
    private String ticketNumber;
    private Long ticketMatchId;
    private Long ticketUserId;
    private int ticketPaidPrice;
    private TicketStatus ticketStatus = TicketStatus.BOOKED;
    private LocalDateTime ticketCreateAt;
    private LocalDateTime ticketUpdateAt;
    private List<TicketDetailDTO> ticketDetails;

    @Override
    public String toString() {
        return "TicketDTO{" +
                "Id=" + ticketId +
                ", Number='" + ticketNumber + '\'' +
                ", MatchId=" + ticketMatchId +
                ", UserId=" + ticketUserId +
                ", TicketPaidPrice=" + ticketPaidPrice +
                ", Status=" + ticketStatus +
                ", CreateAt=" + ticketCreateAt +
                ", UpdateAt=" + ticketUpdateAt +
                ", DetailsCount=" + (ticketDetails != null ? ticketDetails.size() : 0) +
                '}' + '\n';
    }


    public static TicketDTO fromEntity(Ticket ticket){
        List<TicketDetailDTO> ticketDetailDTOList = null;
        List<TicketDetail> ticketDetailList = ticket.getTicketDetails();
        if(!ObjectUtils.isEmpty(ticketDetailList)){
            ticketDetailDTOList = ticketDetailList.stream().map(ticketDetail -> TicketDetailDTO.fromEntity(ticketDetail)).toList();
        }
        return new TicketDTO(
                ticket.getTicketId(),
                ticket.getTicketNumber(),
                ticket.getTicketId(),
                ticket.getTicketUser().getUserId(),
                ticket.getTicketPaidPrice(),
                ticket.getTicketStatus(),
                ticket.getTicketCreateAt(),
                ticket.getTicketUpdateAt(),
                ticketDetailDTOList
        );
    }
}
