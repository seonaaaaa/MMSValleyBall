package com.team.MMSValleyBall.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TicketPurchaseResponseDTO {
    private MatchTableDTO matchInfo;
    private List<AvailableSeatDTO> availableSeatsList;
    private Map<String, Object> userMembership;
    private List<SeatDTO> seatDTOList;

    // 생성자
    public TicketPurchaseResponseDTO() {
        this.matchInfo = matchInfo;
        this.availableSeatsList = availableSeatsList;
        this.userMembership = userMembership;
        this.seatDTOList = seatDTOList;
    }
}
