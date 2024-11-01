package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
import com.team.MMSValleyBall.service.GameService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // GAME - RESULTS - 시즌별 경기 일정
    @GetMapping("/results")
    public ResponseEntity<Page<MatchWithTeamDTO>> getMatches(
            @RequestParam("seasonId") Long seasonId,
            // matchRoundId는 선택적으로 전달 가능하며, 전달되지 않을 경우 null로 처리
            @RequestParam(name = "matchRoundId", required = false) Integer matchRoundId,
            Pageable pageable) {
        Page<MatchWithTeamDTO> matchPage = gameService.getMatchesBySeasonAndRound(seasonId, matchRoundId, pageable);
        return ResponseEntity.ok(matchPage);
    }
}
