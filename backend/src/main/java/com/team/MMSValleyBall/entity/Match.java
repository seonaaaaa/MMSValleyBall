package com.team.MMSValleyBall.entity;

import com.team.MMSValleyBall.enums.MailStatus;
import com.team.MMSValleyBall.enums.MatchStadium;
import com.team.MMSValleyBall.enums.MatchStatus;
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
@Table(name = "match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private MatchStadium matchStadium;

    @Column
    private int matchSetScore;

    @ManyToOne
    @JoinColumn(name = "match_opponent_team_id")
    private Team matchOpponentTeam;

    @Column
    private int matchOpponentTeamSetScore;

    @ManyToOne
    @JoinColumn(name = "match_season_id", nullable = false)
    private Season matchSeason;

    @Column(nullable = false)
    private int matchRoundId;

    @OneToMany(mappedBy = "ticketMatch", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'DEFAULT'")
    @Column(nullable = false)
    private MailStatus matchMailStatus;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'DEFAULT'")
    @Column(nullable = false)
    private MatchStatus matchStatus;

    @Override
    public String toString() {
        return "Match{" +
                "Id=" + matchId +
                ", Date=" + matchDate +
                ", Stadium=" + matchStadium +
                ", SetScore=" + matchSetScore +
                ", OpponentTeam=" + matchOpponentTeam.getTeamName() +
                ", OpponentTeamSetScore=" + matchOpponentTeamSetScore +
                ", Season=" + matchSeason.getSeasonName() +
//                ", RoundId=" + matchRoundId +
                ", tickets=" + tickets +
//                ", " + matchMailStatus +
//                ", " + matchStatus +
                '}'+'\n';
    }
}
