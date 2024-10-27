package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
import com.team.MMSValleyBall.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // MAIN - 경기 일정 (최근 한달) API
    @GetMapping("/schedule/main")
    public ResponseEntity<List<MatchDTO>> getGameScheduleMain() {
        List<MatchDTO> matchList = gameService.findAllMatches();
        return ResponseEntity.ok(matchList);
    }

    // GAME - 경기 일정 (전체) API
    @GetMapping("/schedule/total")
    public ResponseEntity<List<MatchWithTeamDTO>> getGameScheduleTotal() {
        List<MatchWithTeamDTO> matchList = gameService.findAllMatchesWithTeams();
        return ResponseEntity.ok(matchList);
    }
}
