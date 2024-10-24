package com.team.MMSValleyBall.entity;

import com.team.MMSValleyBall.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @Column(nullable = false, unique = true, length = 50)
    private String ticketNumber;

    @ManyToOne
    @JoinColumn(name = "ticket_match_id", nullable = false)
    private Match ticketMatch;

    @ManyToOne
    @JoinColumn(name = "ticket_user_id", nullable = false)
    private Users ticketUser;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'BOOKED'")
    @Column(nullable = false, length = 50)
    private TicketStatus ticketStatus;

    @Column(nullable = false)
    private LocalDateTime ticketCreateAt;

    @Column
    private LocalDateTime ticketUpdateAt;

    @OneToMany(mappedBy = "ticketDetailTicket", fetch = FetchType.EAGER)
    private List<TicketDetail> ticketDetails;

    @Override
    public String toString() {
        return "Ticket{" +
                "Id=" + ticketId +
                ", Number='" + ticketNumber + '\'' +
                ", MatchId=" + ticketMatch.getMatchId() +
                ", UserId=" + ticketUser.getUserId() +
                ", Status=" + ticketStatus +
                ", CreateAt=" + ticketCreateAt +
                ", UpdateAt=" + ticketUpdateAt +
                ", Details=" + ticketDetails +
                '}'+'\n';
    }
}
