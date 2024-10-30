package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Ticket;
import com.team.MMSValleyBall.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // 티켓 예매 테이블: match_date가 오늘 날짜인 티켓을 찾고, 아직 CONFIRMED 상태가 아닌 경우 상태를 업데이트
    @Query("SELECT t FROM Ticket t " +
            "JOIN t.ticketMatch m " +
            "WHERE m.matchDate = :matchDate AND t.ticketStatus <> :ticketStatus " +
            "ORDER BY t.ticketId ASC")
    List<Ticket> findTicketsByMatchDateAndStatus(@Param("matchDate") LocalDateTime matchDate,
                                                 @Param("ticketStatus") TicketStatus ticketStatus);
////    List<Ticket> findByTicketStatusAndTicketCreateAtBefore(TicketStatus ticketStatus, LocalDateTime oneDayAgo);

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

    // ticket_match_id (Match ID)를 사용하여 모든 티켓 조회
    List<Ticket> findByTicketMatch_MatchId(Long matchId);
}
