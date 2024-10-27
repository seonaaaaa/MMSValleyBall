package com.team.MMSValleyBall.repository;

import com.team.MMSValleyBall.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
