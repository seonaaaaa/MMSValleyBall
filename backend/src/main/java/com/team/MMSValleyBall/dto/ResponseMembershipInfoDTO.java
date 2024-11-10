package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.MembershipSales;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMembershipInfoDTO {
    private String membershipName;
    private int membershipPrice;
    private String membershipSalesStatus;
    private LocalDateTime membershipSalesCreateAt;
    private LocalDate seasonEndDate;
}
