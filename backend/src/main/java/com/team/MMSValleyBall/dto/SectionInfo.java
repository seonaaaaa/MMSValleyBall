package com.team.MMSValleyBall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SectionInfo {
    private Long seatId;
    private String sectionName;
    private Long availableSeatAmount;
}
