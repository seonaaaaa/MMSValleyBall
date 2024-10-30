package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
import com.team.MMSValleyBall.dto.SeasonDTO;
import com.team.MMSValleyBall.dto.TeamDTO;
import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.entity.Ticket;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.enums.MailStatus;
import com.team.MMSValleyBall.enums.MatchStatus;
import com.team.MMSValleyBall.repository.GameRepository;

import com.team.MMSValleyBall.repository.SeasonRepository;
import com.team.MMSValleyBall.repository.TeamRepository;
import com.team.MMSValleyBall.repository.TicketRepository;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

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

    // MAIN 페이지 경기 일정 조회 (MatchDTO 반환)
    public List<MatchDTO> findAllMatches() {
        return gameRepository.findAll()
                .stream()
                .map(entity-> MatchDTO.fromEntity(entity))
                .toList();
    }

    // 전체 경기 일정 조회 (MatchWithTeamDTO 반환)
    public List<MatchWithTeamDTO> findAllMatchesWithTeams() {
        List<MatchWithTeamDTO> matchList = gameRepository.findAll()
                                            .stream()
                                            .map(entity -> MatchWithTeamDTO.fromEntity(entity))
                                            .toList();
        // 리스트 크기 출력
        // System.out.println("matchList.size() : " + matchList.size());
        return matchList;
    }

    // 시즌별 페이지 단위 경기 일정 조회 (Page<MatchWithTeamDTO> 반환)
    public Page<MatchWithTeamDTO> getMatchesBySeasonAndRound(Long seasonId, Integer matchRoundId, Pageable pageable) {
        return gameRepository.findBySeasonAndRound(seasonId, matchRoundId, pageable)
                                .map(entity -> MatchWithTeamDTO.fromEntity(entity));
    }

    // 전체 경기 일정 조회 (Page<MatchWithTeamDTO> 반환)
    public Page<MatchWithTeamDTO> findAllMatches(Pageable pageable) {
        return gameRepository.findAll(pageable)
        .map(entity -> MatchWithTeamDTO.fromEntity(entity));
    }

    // 논리적 삭제
    public String deactiveMatch(Long matchId) {
        Match deactiveMatch = gameRepository.findById(matchId).orElse(null);

        if(ObjectUtils.isEmpty(deactiveMatch)){
            return "해당 경기가 존재하지 않습니다.";
        }
        deactiveMatch.setMatchStatus(MatchStatus.DELETED); // 논리적 삭제 처리
        deactiveMatch.setMatchMailStatus(MailStatus.REQUIRED);  // 메일 상태 설정
        gameRepository.save(deactiveMatch); // 변경 사항 저장
        return "경기 삭제 성공";
    }

    // 메일 상태 변경 (mail_status를 'DEFAULT' 또는 'REQUIRED'로 설정)
    public String updateMailStatus(Long matchId, String mailStatus) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("해당 경기가 존재하지 않습니다."));

        // 전달된 mailStatus 값에 따라 설정
        MailStatus status = MailStatus.valueOf(mailStatus.toUpperCase());
        match.setMatchMailStatus(status);
        gameRepository.save(match);

        return "메일 상태가 " + mailStatus + "로 변경되었습니다.";
    }

    // 메일 발송과 상태 업데이트
    public String sendEmailsForMatch(Long matchId) {
        Match match = gameRepository.findById(matchId).orElse(null);
        if (match == null) {
            return "해당 경기가 존재하지 않습니다.";
        }

        // matchId에 해당하는 모든 티켓 가져옴
        List<Ticket> tickets = ticketRepository.findByTicketMatch_MatchId(matchId);

        // 각 티켓의 사용자에게 이메일 전송
        for (Ticket ticket : tickets) {
            Users user = ticket.getTicketUser();
            if (user != null && user.getUserEmail() != null) {
                emailService.sendEmail(user.getUserEmail(), "경기 안내", "경기 일정이 업데이트되었습니다.");
            }
        }

        // 메일 상태를 DEFAULT로 변경
        match.setMatchMailStatus(MailStatus.DEFAULT);
        gameRepository.save(match);

        return "메일 발송 완료 및 상태 업데이트 완료";
    }




    // 관리자 페이지 - 특정 matchId에 해당하는 경기 정보 조회
    public MatchWithTeamDTO getMatchById(Long matchId) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("경기를 찾을 수 없습니다."));
        return MatchWithTeamDTO.fromEntity(match);
    }

    // 관리자 페이지 - 모든 시즌 정보 조회
    public List<SeasonDTO> getAllSeasons() {
        return seasonRepository.findAll()
                .stream()
                .map(entity -> SeasonDTO.fromEntity(entity))
                .toList();
    }

    // 관리자 페이지 - 모든 팀 정보 조회
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(entity -> TeamDTO.fromEntity(entity))
                .toList();
    }

    // 경기 정보 업데이트
    public String updateMatch(Long matchId, MatchWithTeamDTO matchDTO) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("경기를 찾을 수 없습니다."));
        match.setMatchDate(matchDTO.getMatchDate());
        match.setMatchStadium(matchDTO.getMatchStadium());
        match.setMatchSetScore(matchDTO.getMatchSetScore());
        match.setMatchOpponentTeamSetScore(matchDTO.getMatchOpponentTeamSetScore());

        // 연관된 시즌과 팀 정보 설정
        match.setMatchSeason(seasonRepository.findById(matchDTO.getSeasonId())
                .orElseThrow(() -> new RuntimeException("시즌을 찾을 수 없습니다.")));
        match.setMatchOpponentTeam(teamRepository.findById(matchDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다.")));
        match.setMatchRoundId(matchDTO.getMatchRoundId());

        gameRepository.save(match);
        return "경기 정보 업데이트 성공";
    }
}
