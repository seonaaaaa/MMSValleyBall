package com.team.MMSValleyBall.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AvailableSeatDTO {
    private String zoneName;
    private Long availableSeatAmount;
    private List<SectionInfo> sections;

    public AvailableSeatDTO(String seatName, Long availableSeatAmount, List<SectionInfo> sections) {
        this.zoneName = seatName;
        this.availableSeatAmount = availableSeatAmount;
        this.sections = sections;
    }
}

