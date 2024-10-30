package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
import com.team.MMSValleyBall.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/games")
public class AdminGameController {

    private final GameService gameService;

    public AdminGameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public String showGames(Model model) {
        List<MatchWithTeamDTO> mathList = gameService.findAllMatchesWithTeams();
        int totalMatches = mathList.size();

        model.addAttribute("matchList", mathList);
        model.addAttribute("totalMatches", totalMatches);

        return "admin/gameList";
    }
}
