package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.MembershipSales;
import com.team.MMSValleyBall.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipSalesRepository extends JpaRepository<MembershipSales, Long> {
   List<MembershipSales> findByMembershipSalesUser(Users user);
}
