package com.team.MMSValleyBall.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false, unique = true)
    private String teamName;

    @Column(length = 100)
    private String teamStadium;

    @OneToMany(mappedBy = "matchOpponentTeam", fetch = FetchType.EAGER)
    private List<Match> matches;
}
