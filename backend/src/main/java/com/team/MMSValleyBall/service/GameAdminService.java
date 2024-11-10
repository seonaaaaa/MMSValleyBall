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
import com.team.MMSValleyBall.service.EmailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class GameAdminService {

    private final GameRepository gameRepository;
    private final SeasonRepository seasonRepository;
    private final TeamRepository teamRepository;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    public GameAdminService(GameRepository gameRepository, SeasonRepository seasonRepository, TeamRepository teamRepository, TicketRepository ticketRepository, EmailService emailService) {
        this.gameRepository = gameRepository;
        this.seasonRepository = seasonRepository;
        this.teamRepository = teamRepository;
        this.ticketRepository = ticketRepository;
        this.emailService = emailService;
    }

    // 필터링 및 정렬된 경기 데이터 조회
    public Page<MatchWithTeamDTO> findAllFiltered(
            Long seasonId, Integer matchRoundId, MatchStadium matchStadium,
            Long teamId, MailStatus matchMailStatus, MatchStatus matchStatus, Pageable pageable) {

        return gameRepository.findAllFiltered(seasonId, matchRoundId, matchStadium, teamId, matchMailStatus, matchStatus, pageable)
                .map(MatchWithTeamDTO::fromEntity); // 람다식으로 수정
    }

    // 신규 경기 등록
    public String insertMatch(MatchWithTeamDTO matchDTO) {
        Match newMatch = new Match();
        newMatch.setMatchDate(matchDTO.getMatchDate());
        newMatch.setMatchStadium(matchDTO.getMatchStadium());
        newMatch.setMatchSetScore(matchDTO.getMatchSetScore());
        newMatch.setMatchOpponentTeamSetScore(matchDTO.getMatchOpponentTeamSetScore());

        newMatch.setMatchSeason(
                seasonRepository.findById(matchDTO.getSeasonId())
                        .orElseThrow(() -> new NoSuchElementException("시즌을 찾을 수 없습니다.")));

        newMatch.setMatchOpponentTeam(
                teamRepository.findById(matchDTO.getTeamId())
                        .orElseThrow(() -> new NoSuchElementException("팀을 찾을 수 없습니다.")));

        newMatch.setMatchRoundId(matchDTO.getMatchRoundId());

        newMatch.setMatchStatus(MatchStatus.DEFAULT);
        newMatch.setMatchMailStatus(MailStatus.DEFAULT);

        gameRepository.save(newMatch);
        return "신규 경기가 등록되었습니다.";
    }

    // 시즌 정보 가져오기
    public List<SeasonDTO> getAllSeasons() {
        return seasonRepository.findAll()
                .stream()
                .map(SeasonDTO::fromEntity)
                .toList();
    }

    // 팀 정보 가져오기
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(TeamDTO::fromEntity)
                .toList();
    }

    // 경기 논리적 삭제
    public String deactivateMatch(Long matchId) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new NoSuchElementException("해당 경기가 존재하지 않습니다."));

        match.setMatchStatus(MatchStatus.DELETED);
        match.setMatchMailStatus(MailStatus.REQUIRED);

        gameRepository.save(match);
        return "경기가 삭제되었습니다.";
    }

    // 경기 상태 업데이트
    public String activateMatch(Long matchId) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new NoSuchElementException("경기를 찾을 수 없습니다."));

        match.setMatchStatus(MatchStatus.DEFAULT);  // 상태를 DEFAULT로 변경
        gameRepository.save(match);

        return "경기 상태가 DEFAULT로 변경되었습니다.";
    }

    // 메일 상태 업데이트
    public String updateMailStatus(Long matchId, String mailStatus) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new NoSuchElementException("해당 경기가 존재하지 않습니다."));

        try {
            match.setMatchMailStatus(MailStatus.valueOf(mailStatus.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 메일 상태입니다.");
        }

        gameRepository.save(match);
        return "메일 상태가 " + mailStatus + "로 변경되었습니다.";
    }

    // 메일 발송 및 상태 업데이트
    public String sendEmailsForMatch(Long matchId) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new NoSuchElementException("해당 경기가 존재하지 않습니다."));

        String emailMessage = createEmailMessage(match);

        List<Ticket> tickets = ticketRepository.findByTicketMatch_MatchId(matchId);

        for (Ticket ticket : tickets) {
            Users user = ticket.getTicketUser();
            if (user != null && user.getUserEmail() != null) {
                try {
                    emailService.sendEmail(user.getUserEmail(), "[GS MMS Volleyball] 경기 변동 안내", emailMessage);
                } catch (Exception e) {
                    System.out.println("Failed to send email to: " + user.getUserEmail());
                    e.printStackTrace();
                }
            }
        }

        match.setMatchMailStatus(MailStatus.DEFAULT);
        gameRepository.save(match);

        return "메일 발송 및 상태 업데이트를 완료했습니다.";
    }

    // 메일 내용 작성
    private String createEmailMessage(Match match) {
        String teamName = match.getMatchOpponentTeam().getTeamName();
        String date = match.getMatchDate().toString();

        if (match.getMatchStatus() == MatchStatus.DELETED) {
            return "안녕하세요, [GS MMS Volleyball]입니다.\n\n"
                    + "고객님께서 예매하신 경기가 취소되어 안내드립니다.\n\n"
                    + "- 경기명: GS MMS VS " + teamName + "\n"
                    + "- 원래 일정: " + date + "\n\n"
                    + "불편을 드려 죄송하며, 취소된 경기의 티켓은 전액 환불됩니다.\n"
                    + "환불 절차에 대한 자세한 안내는 [고객센터 070-7544-7000]로 문의해 주시기 바랍니다.\n"
                    + "감사합니다.\n\n[GS MMS Volleyball 드림]";
        } else {
            return "안녕하세요, [GS MMS Volleyball]입니다.\n\n"
                    + "고객님이 예매하신 경기 일정에 변동이 있어 안내드립니다.\n\n"
                    + "- 경기명: GS MMS VS " + teamName + "\n"
                    + "- 변경 후 일정: " + date + "\n\n"
                    + "변경된 일정에 맞춰 관람을 원치 않으실 경우 환불을 요청하실 수 있습니다.\n"
                    + "환불 절차는 [고객센터 070-7544-7000]로 문의해 주시기 바랍니다.\n"
                    + "감사합니다.\n\n[GS MMS Volleyball 드림]";
        }
    }

    // 경기 조회 (수정 페이지)
    public MatchWithTeamDTO getMatchById(Long matchId) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new NoSuchElementException("경기를 찾을 수 없습니다."));

        return MatchWithTeamDTO.fromEntity(match);
    }

    // 경기 수정
    public String updateMatch(Long matchId, MatchWithTeamDTO matchDTO) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new NoSuchElementException("경기를 찾을 수 없습니다."));

        match.setMatchDate(matchDTO.getMatchDate());
        match.setMatchStadium(matchDTO.getMatchStadium());
        match.setMatchSetScore(matchDTO.getMatchSetScore());
        match.setMatchOpponentTeamSetScore(matchDTO.getMatchOpponentTeamSetScore());

        match.setMatchSeason(seasonRepository.findById(matchDTO.getSeasonId())
                .orElseThrow(() -> new NoSuchElementException("시즌을 찾을 수 없습니다.")));

        match.setMatchOpponentTeam(teamRepository.findById(matchDTO.getTeamId())
                .orElseThrow(() -> new NoSuchElementException("팀을 찾을 수 없습니다.")));

        match.setMatchRoundId(matchDTO.getMatchRoundId());

        match.setMatchMailStatus(MailStatus.REQUIRED);

        gameRepository.save(match);
        return "경기 정보 업데이트에 성공했습니다.";
    }
}