package com.team.MMSValleyBall.dto;

import com.team.MMSValleyBall.entity.Membership;
import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembershipDTO {
    private Long membershipId;
    private String membershipName;
    private int membershipPrice;
    private SeasonDTO membershipSeason;
    private List<UserDTO> users;
    private List<MembershipSalesDTO> membershipSales;

    public static MembershipDTO fromEntity(Membership membership){
        List<UserDTO> userDTOList = null;
        List<Users> userList = membership.getUsers();
        if(!ObjectUtils.isEmpty(userList)){
            userDTOList = userList.stream().map(user -> UserDTO.fromEntity(user)).toList();
        }
        List<MembershipSalesDTO> membershipSalesDTOList = null;
        List<MembershipSales> membershipSalesList = membership.getMembershipSales();
        if(!ObjectUtils.isEmpty(membershipSalesList)){
            membershipSalesDTOList = membershipSalesList.stream().map(membershipSales -> MembershipSalesDTO.fromEntity(membershipSales)).toList();
        }
        return new MembershipDTO(
                membership.getMembershipId(),
                membership.getMembershipName(),
                membership.getMembershipPrice(),
                SeasonDTO.fromEntity(membership.getMembershipSeason()),
                userDTOList,
                membershipSalesDTOList
        );
    }
}
