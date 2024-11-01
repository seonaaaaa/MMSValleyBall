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

    // ADMIN - MAIN - (필터링 및 정렬)
    public Page<MatchWithTeamDTO> findAllFiltered(
            Long seasonId, Integer matchRoundId, MatchStadium matchStadium,
            Long teamId, MailStatus matchMailStatus, MatchStatus matchStatus, Pageable pageable) {
        return gameRepository.findAllFiltered(seasonId, matchRoundId, matchStadium, teamId, matchMailStatus, matchStatus, pageable)
                            .map(entity -> MatchWithTeamDTO.fromEntity(entity));
    }

    // ADMIN - INSERT - 신규 경기 등록
    public String insertMatch(MatchWithTeamDTO matchDTO) {
        Match newMatch = new Match();
        newMatch.setMatchDate(matchDTO.getMatchDate());
        newMatch.setMatchStadium(matchDTO.getMatchStadium());
        newMatch.setMatchSetScore(matchDTO.getMatchSetScore());
        newMatch.setMatchOpponentTeamSetScore(matchDTO.getMatchOpponentTeamSetScore());

        // 연관된 시즌과 팀 정보 설정
        newMatch.setMatchSeason(seasonRepository.findById(matchDTO.getSeasonId())
                .orElseThrow(() -> new RuntimeException("시즌을 찾을 수 없습니다.")));
        newMatch.setMatchOpponentTeam(teamRepository.findById(matchDTO.getTeamId())
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다.")));
        newMatch.setMatchRoundId(matchDTO.getMatchRoundId());

        // 기본값 설정
        newMatch.setMatchStatus(MatchStatus.DEFAULT);
        newMatch.setMatchMailStatus(MailStatus.DEFAULT);
        
        gameRepository.save(newMatch);
        return "신규 경기가 등록되었습니다.";
    }

    // ADMIN - 시즌 정보 가져오기 (셀렉트 박스에 사용)
    public List<SeasonDTO> getAllSeasons() {
        return seasonRepository.findAll()
                .stream()
                .map(entity -> SeasonDTO.fromEntity(entity))
                .toList();
    }

    // ADMIN - 팀 정보 가져오기 (셀렉트 박스에 사용)
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll()
                .stream()
                .map(entity -> TeamDTO.fromEntity(entity))
                .toList();
    }

    // ADMIN - 경기 논리적 삭제 (match_status, mail_status 변경)
    public String deactiveMatch(Long matchId) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("해당 경기가 존재하지 않습니다."));
        match.setMatchStatus(MatchStatus.DELETED); // 논리적 삭제 처리
        match.setMatchMailStatus(MailStatus.REQUIRED);  // 메일 상태 설정
        gameRepository.save(match); // 변경 사항 저장
        return "경기가 삭제되었습니다.";
    }

    // ADMIN - 경기 상태 업데이트 (match_status 변경)
    public String updateMatchStatus(Long matchId, String status) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("경기를 찾을 수 없습니다."));
        match.setMatchStatus(MatchStatus.valueOf(status)); // status를 MatchStatus enum으로 변환하여 설정
        gameRepository.save(match);
        return "경기 상태가 " + status + "(으)로 변경되었습니다.";
    }

    // ADMIN - 메일 상태 변경 (mail_status 변경)
    public String updateMailStatus(Long matchId, String mailStatus) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("해당 경기가 존재하지 않습니다."));

        // 전달된 mailStatus 값에 따라 설정
        MailStatus status = MailStatus.valueOf(mailStatus.toUpperCase());
        match.setMatchMailStatus(status);
        gameRepository.save(match);

        return "메일 상태가 " + mailStatus + "로 변경되었습니다.";
    }

    // ADMIN - 메일 발송 및 상태 변경
    public String sendEmailsForMatch(Long matchId) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("해당 경기가 존재하지 않습니다."));

        // 메일 내용 설정
        String emailMessage;
        if (match.getMatchStatus() == MatchStatus.DELETED) {
            emailMessage = "안녕하세요, [GS MMS Volleyball]입니다.\n\n"
                    + "고객님께서 예매하신 경기가 취소되어 안내드립니다.\n\n"
                    + "- 경기명: " + "GS MMS VS " + match.getMatchOpponentTeam().getTeamName() + "\n"
                    + "- 원래 일정: " + match.getMatchDate() + "\n\n"
                    + "불편을 드려 죄송하며, 취소된 경기의 티켓은 전액 환불됩니다.\n"
                    + "환불 절차에 대한 자세한 안내는 [고객센터 070-7544-7000]로 문의해 주시기 바랍니다.\n\n"
                    + "감사합니다.\n[GS MMS Volleyball 드림]";
        } else {
            emailMessage = "안녕하세요, [GS MMS Volleyball]입니다.\n\n"
                    + "고객님이 예매하신 경기 일정에 변동이 있어 안내드립니다.\n\n"
                    + "- 경기명: " + "GS MMS VS " + match.getMatchOpponentTeam().getTeamName() +"\n"
                    + "- 변경 후 일정: " + match.getMatchDate() + "\n\n"
                    + "변경된 일정에 맞춰 관람을 원치 않으실 경우 환불을 요청하실 수 있습니다.\n"
                    + "환불 절차는 [고객센터 070-7544-7000]로 문의해 주시기 바랍니다.\n\n"
                    + "감사합니다.\n[GS MMS Volleyball 드림]";
        }

        // matchId에 해당하는 모든 티켓 가져옴
        List<Ticket> tickets = ticketRepository.findByTicketMatch_MatchId(matchId);

        // 각 티켓의 사용자에게 이메일 전송
        for (Ticket ticket : tickets) {
            Users user = ticket.getTicketUser();
            if (user != null && user.getUserEmail() != null) {
                try {
                    System.out.println("Attempting to send email to: " + user.getUserEmail());
                    emailService.sendEmail(user.getUserEmail(), "[GS MMS Volleyball] 경기 변동 안내", emailMessage);
                    System.out.println("Email sent successfully to: " + user.getUserEmail());
                } catch (Exception e) {
                    System.out.println("Failed to send email to: " + user.getUserEmail());
                    e.printStackTrace();
                }
            }
        }

        // 메일 상태를 DEFAULT로 변경
        match.setMatchMailStatus(MailStatus.DEFAULT);
        gameRepository.save(match);

        return "메일 발송 및 상태 업데이트를 완료했습니다.";
    }

    // ADMIN - UPDATE - 경기 조회 (Update 페이지에 보여줄 데이터 조회)
    public MatchWithTeamDTO getMatchById(Long matchId) {
        Match match = gameRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("경기를 찾을 수 없습니다."));
        return MatchWithTeamDTO.fromEntity(match);
    }

    // ADMIN - UPDATE - 경기 수정
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
        return "경기 정보 업데이트에 성공했습니다.";
    }
}