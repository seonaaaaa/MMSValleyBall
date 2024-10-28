package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT s.seatZone, s.seatSection, (s.seatCount - COALESCE(SUM(td.ticketDetailAmount), 0)) AS availableSeats " +
            "FROM Seat s " +
            "LEFT JOIN TicketDetail td ON td.ticketDetailSeat = s " +
            "LEFT JOIN Ticket t ON t = td.ticketDetailTicket AND t.ticketMatch.matchId = :matchId " +
            "GROUP BY s.seatZone, s.seatSection, s.seatCount")
    List<Object[]> findAvailableSeatsByMatch(@Param("matchId") Long matchId);
}
