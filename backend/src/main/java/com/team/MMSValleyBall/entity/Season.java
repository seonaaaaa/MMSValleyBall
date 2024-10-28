package com.team.MMSValleyBall.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "season")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seasonId;

    @Column(nullable = false, length = 50)
    private String seasonName;

    @Column(nullable = false)
    private LocalDate seasonStartDate;

    @Column(nullable = false)
    private LocalDate seasonEndDate;

    @OneToMany(mappedBy = "matchSeason", fetch = FetchType.EAGER)
    private List<Match> matches;

    @OneToMany(mappedBy = "membershipSeason", fetch = FetchType.EAGER)
    private List<Membership> memberships;
}
