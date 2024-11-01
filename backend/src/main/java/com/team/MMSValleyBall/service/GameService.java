package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
import com.team.MMSValleyBall.dto.SeasonDTO;
import com.team.MMSValleyBall.dto.TeamDTO;
import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.entity.Ticket;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.MailStatus;
import com.team.MMSValleyBall.enums.MatchStadium;
import com.team.MMSValleyBall.enums.MatchStatus;
import com.team.MMSValleyBall.repository.GameRepository;

import com.team.MMSValleyBall.repository.SeasonRepository;
import com.team.MMSValleyBall.repository.TeamRepository;
import com.team.MMSValleyBall.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final SeasonRepository seasonRepository;
    private final TeamRepository teamRepository;

    public GameService(GameRepository gameRepository, TicketRepository ticketRepository, EmailService emailService, SeasonRepository seasonRepository, TeamRepository teamRepository) {
        this.gameRepository = gameRepository;
        this.ticketRepository = ticketRepository;
        this.emailService = emailService;
        this.seasonRepository = seasonRepository;
        this.teamRepository = teamRepository;
    }

    // MAIN & GAME 공통 사용 CALENDAR - 경기 일정
    public List<MatchWithTeamDTO> findAllMatchesWithTeams() {
        List<MatchWithTeamDTO> matchList = gameRepository.findAllDefaultMatches()
                                            .stream()
                                            .map(entity -> MatchWithTeamDTO.fromEntity(entity))
                                            .toList();
        return matchList;
    }

    // GAME - RESULTS - 시즌별 경기 일정
    public Page<MatchWithTeamDTO> getMatchesBySeasonAndRound(Long seasonId, Integer matchRoundId, Pageable pageable) {
        return gameRepository.findBySeasonAndRound(seasonId, matchRoundId, MatchStatus.DEFAULT, pageable)
                                .map(entity -> MatchWithTeamDTO.fromEntity(entity));
    }
}