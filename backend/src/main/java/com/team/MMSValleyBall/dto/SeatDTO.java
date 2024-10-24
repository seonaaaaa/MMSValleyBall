package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Seat;
import com.team.MMSValleyBall.entity.TicketDetail;
import com.team.MMSValleyBall.enums.SeatSection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {
    private Long seatId;
    private String seatZone;
    private SeatSection seatSection;
    private int seatPrice;
    private int seatCount;
    private List<TicketDetailDTO> ticketDetails;

    public static SeatDTO fromEntity(Seat seat){
        List<TicketDetailDTO> ticketDetailDTOList = null;
        List<TicketDetail> ticketDetailList = seat.getTicketDetails();
        if(!ObjectUtils.isEmpty(ticketDetailList)){
            ticketDetailDTOList = ticketDetailList.stream().map(ticketDetail -> TicketDetailDTO.fromEntity(ticketDetail)).toList();
        }
        return new SeatDTO(
                seat.getSeatId(),
                seat.getSeatZone(),
                seat.getSeatSection(),
                seat.getSeatPrice(),
                seat.getSeatCount(),
                ticketDetailDTOList
        );
    }
}
