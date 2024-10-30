package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
import com.team.MMSValleyBall.dto.SeasonDTO;
import com.team.MMSValleyBall.dto.TeamDTO;
import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.service.GameService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "http://localhost:8080")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // MAIN - 경기 일정
    @GetMapping("/schedule/main")
    public ResponseEntity<List<MatchWithTeamDTO>> getGameScheduleMain() {
        List<MatchWithTeamDTO> matchList = gameService.findAllMatchesWithTeams();
        return ResponseEntity.ok(matchList);
    }

    // GAME - 경기 일정
    @GetMapping("/schedule/total")
    public ResponseEntity<List<MatchWithTeamDTO>> getGameScheduleTotal() {
        List<MatchWithTeamDTO> matchList = gameService.findAllMatchesWithTeams();
        return ResponseEntity.ok(matchList);
    }

    // GAME - 경기 결과
    @GetMapping("/results")
    public ResponseEntity<Page<MatchWithTeamDTO>> getMatches(
            @RequestParam("seasonId") Long seasonId,
            // matchRoundId는 선택적으로 전달 가능하며, 전달되지 않을 경우 null로 처리
            @RequestParam(name = "matchRoundId", required = false) Integer matchRoundId,
            Pageable pageable) {
        Page<MatchWithTeamDTO> matchPage = gameService.getMatchesBySeasonAndRound(seasonId, matchRoundId, pageable);
        return ResponseEntity.ok(matchPage);
    }

    // ADMIN - MAIN - 전체 경기 페이지네이션
    @GetMapping("/admin")
    public ResponseEntity<Page<MatchWithTeamDTO>> getAllMatches(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("matchId").ascending()); // 경기 ID 오름차순 정렬
        Page<MatchWithTeamDTO> matchList = gameService.findAllMatches(pageable);
        return ResponseEntity.ok(matchList);
    }

    // ADMIN - INSERT - 신규 경기 등록
    @PostMapping("/admin/insert")
    public ResponseEntity<String> insertMatch(@RequestBody MatchWithTeamDTO matchDTO) {
        String result = gameService.insertMatch(matchDTO);
        return ResponseEntity.ok(result);
    }

    // ADMIN - 시즌 정보 가져오기 (셀렉트 박스에 사용)
    @GetMapping("/admin/seasons")
    public ResponseEntity<List<SeasonDTO>> getAllSeasons() {
        List<SeasonDTO> seasons = gameService.getAllSeasons();
        return ResponseEntity.ok(seasons);
    }

    // ADMIN - 팀 정보 가져오기 (셀렉트 박스에 사용)
    @GetMapping("/admin/teams")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = gameService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    // ADMIN - 경기 논리적 삭제 (match_status, mail_status 변경)
    @PatchMapping("/admin/delete/{matchId}")
    public ResponseEntity<String> deleteMatch(@PathVariable("matchId") Long matchId) {
        String result = gameService.deactiveMatch(matchId);
        return ResponseEntity.ok(result);
    }

    // ADMIN - 경기 상태 업데이트 (match_status 변경)
    @PatchMapping("/admin/update-status/{matchId}")
    public ResponseEntity<String> updateMatchStatus(@PathVariable("matchId") Long matchId, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        String result = gameService.updateMatchStatus(matchId, status);
        return ResponseEntity.ok(result);
    }

    // ADMIN - 메일 상태 변경 (mail_status 변경)
    @PatchMapping("/admin/update-mail-status/{matchId}")
    public ResponseEntity<String> updateMailStatus(@PathVariable("matchId") Long matchId, @RequestBody Map<String, String> body) {
        String mailStatus = body.get("mailStatus");
        String result = gameService.updateMailStatus(matchId, mailStatus);
        return ResponseEntity.ok(result);
    }

    // ADMIN - 메일 발송
    @PostMapping("/admin/{matchId}/sendEmail")
    public ResponseEntity<String> sendEmailsForMatch(@PathVariable("matchId") Long matchId) {
        String result = gameService.sendEmailsForMatch(matchId);
        return ResponseEntity.ok(result);
    }

    // ADMIN - UPDATE - 경기 조회 (Update 페이지에 보여줄 데이터 조회)
    @GetMapping("/admin/{matchId}")
    public ResponseEntity<MatchWithTeamDTO> getMatchById(@PathVariable("matchId") Long matchId) {
        MatchWithTeamDTO matchDTO = gameService.getMatchById(matchId);
        return ResponseEntity.ok(matchDTO);
    }

    // ADMIN - UPDATE - 경기 수정
    @PutMapping("/admin/update/{matchId}")
    public ResponseEntity<String> updateMatch(@PathVariable("matchId") Long matchId, @RequestBody MatchWithTeamDTO matchDTO) {
        String result = gameService.updateMatch(matchId, matchDTO);
        return ResponseEntity.ok(result);
    }
}
