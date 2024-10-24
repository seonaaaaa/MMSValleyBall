package com.team.MMSValleyBall.entity;

import com.team.MMSValleyBall.enums.SeatSection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @Column(nullable = false, length = 20)
    private String seatZone;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private SeatSection seatSection;

    @Column(nullable = false)
    private int seatPrice;

    @Column(nullable = false)
    private int seatCount;

    @OneToMany(mappedBy = "ticketDetailSeat", fetch = FetchType.EAGER)
    private List<TicketDetail> ticketDetails;
}
