package com.team.MMSValleyBall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatchPaymentDTO {
    private Long matchId; // 경기 ID
    private String matchInfo; // 경기 정보
    private Integer totalAmount; // 매출액

    public MatchPaymentDTO(Long matchId, String matchInfo, Integer totalAmount) {
        this.matchId = matchId;
        this.matchInfo = matchInfo;
        this.totalAmount = totalAmount;
    }
}
