package com.team.MMSValleyBall.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket_detail")
public class TicketDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketDetailId;

    @ManyToOne
    @JoinColumn(name = "ticket_detail_seat_id", nullable = false)
    private Seat ticketDetailSeat;

    @Column(nullable = false)
    private int ticketDetailAmount;

    @ManyToOne
    @JoinColumn(name = "ticket_detail_ticket_id", nullable = false)
    private Ticket ticketDetailTicket;

    @Override
    public String toString() {
        return "TicketDetail{" +
                "Id=" + ticketDetailId +
                ", SeatId=" + ticketDetailSeat.getSeatId() +
                ", Amount=" + ticketDetailAmount +
                ", TicketNumber=" + ticketDetailTicket.getTicketNumber() +
                '}'+'\n';
    }
}
