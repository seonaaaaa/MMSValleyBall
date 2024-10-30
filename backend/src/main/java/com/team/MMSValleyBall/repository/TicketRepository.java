package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // 경기별 매출 조회
    @Query("SELECT SUM(t.ticketPaidPrice), te.teamName " +
            "FROM Ticket t " +
            "JOIN t.ticketMatch m " +
            "JOIN m.matchOpponentTeam te " +
            "WHERE t.ticketStatus = 'BOOKED' " +  // 구매된 티켓만 포함
            "GROUP BY te.teamName")
    List<Object[]> findTotalPaidPriceByMatch();

    // 월별 매출 조회
    @Query("SELECT EXTRACT(MONTH FROM t.ticketCreateAt) AS month, SUM(t.ticketPaidPrice) AS totalSales " +
            "FROM Ticket t " +
            "WHERE t.ticketStatus = 'BOOKED' " +  // 구매된 티켓만 포함
            "GROUP BY EXTRACT(MONTH FROM t.ticketCreateAt) " +
            "ORDER BY month ASC")
    List<Object[]> findMonthlySalesNative();
}

