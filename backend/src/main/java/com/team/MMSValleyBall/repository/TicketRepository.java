package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    //구역별 잔여좌석 조회 쿼리문
    @Query("SELECT s.seatId AS seatId, " +
            "s.seatZone AS seatZone, " +
            "s.seatSection AS seatSection, " +
            "COALESCE(s.seatCount - SUM(td.ticketDetailAmount), s.seatCount) AS availableSeats " +
            "FROM Seat s " +
            "LEFT JOIN TicketDetail td ON td.ticketDetailSeat = s " +
            "LEFT JOIN Ticket t ON t = td.ticketDetailTicket AND t.ticketMatch.matchId = :matchId " +
            "GROUP BY s.seatId, s.seatZone, s.seatSection, s.seatCount " +
            "ORDER BY s.seatId")
    List<Object[]> findAvailableSeatsByMatch(@Param("matchId") Long matchId);

}
