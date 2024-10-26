package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.repository.GameRepository;
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

    // 경기 일정 페이지
    @GetMapping("/schedule")
    public ResponseEntity<List<MatchDTO>> getGameSchedule() {
        List<MatchDTO> matchList = gameService.findAllMatches();
        return ResponseEntity.ok(matchList);
    }

}
