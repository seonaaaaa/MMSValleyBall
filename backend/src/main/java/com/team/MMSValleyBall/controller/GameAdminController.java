package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
import com.team.MMSValleyBall.dto.SeasonDTO;
import com.team.MMSValleyBall.dto.TeamDTO;
import com.team.MMSValleyBall.enums.MatchStatus;
import com.team.MMSValleyBall.service.GameAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/game")
public class GameAdminController {

    private final GameAdminService gameAdminService;

    public GameAdminController(GameAdminService gameAdminService) {
        this.gameAdminService = gameAdminService;
    }

    @GetMapping("/user")
    public String showUserList() {
        return "userList";
    }

    // 경기 관리 페이지
    @GetMapping("")
    public String showGameList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortField", defaultValue = "matchDate") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection,
            Model model) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MatchWithTeamDTO> matchPage = gameAdminService.findAllFiltered(null, null, null, null, null, null, pageable);

        List<Integer> rounds = Arrays.asList(1, 2, 3, 4, 5, 6);
        model.addAttribute("rounds", rounds);

        model.addAttribute("matchList", matchPage.getContent());
        model.addAttribute("totalMatches", matchPage.getTotalElements());
        model.addAttribute("totalPages", matchPage.getTotalPages());
        model.addAttribute("currentPage", page);

        List<SeasonDTO> seasons = gameAdminService.getAllSeasons();
        List<TeamDTO> teams = gameAdminService.getAllTeams();
        model.addAttribute("seasons", seasons);
        model.addAttribute("teams", teams);

        return "gameList";
    }

    // 신규 경기 등록 페이지
    @GetMapping("/insert")
    public String insertGameForm(Model model) {
        List<SeasonDTO> seasons = gameAdminService.getAllSeasons();
        List<TeamDTO> teams = gameAdminService.getAllTeams();
        model.addAttribute("seasons", seasons);
        model.addAttribute("teams", teams);

        return "gameInsert";
    }

    // 신규 경기 등록 처리
    @PostMapping("/insert")
    public String insertGame(@ModelAttribute MatchWithTeamDTO matchDTO) {
        gameAdminService.insertMatch(matchDTO);
        return "redirect:/admin/game";
    }

    // 경기 수정 페이지
    @GetMapping("/update/{matchId}")
    public String updateGameForm(@PathVariable Long matchId, Model model) {
        MatchWithTeamDTO matchDTO = gameAdminService.getMatchById(matchId);
        model.addAttribute("match", matchDTO);

        List<SeasonDTO> seasons = gameAdminService.getAllSeasons();
        List<TeamDTO> teams = gameAdminService.getAllTeams();
        model.addAttribute("seasons", seasons);
        model.addAttribute("teams", teams);

        return "gameUpdate";
    }

    // 경기 수정 처리
    @PostMapping("/update/{matchId}")
    public String updateGame(@PathVariable Long matchId, @ModelAttribute MatchWithTeamDTO matchDTO) {
        gameAdminService.updateMatch(matchId, matchDTO);
        return "redirect:/admin/game";
    }

    // 경기 논리적 삭제 (비활성화)
    @PatchMapping("/delete/{matchId}")
    @ResponseBody
    public String deactivateMatch(@PathVariable Long matchId) {
        return gameAdminService.deactivateMatch(matchId);
    }

    // 경기 상태 업데이트
    @PatchMapping("/update-match-status/{matchId}")
    @ResponseBody
    public String updateMatchStatus(@PathVariable Long matchId, @RequestBody String status) {
        return gameAdminService.updateMatchStatus(matchId, status);
    }

    // 메일 상태 변경
    @PatchMapping("/update-mail-status/{matchId}")
    @ResponseBody
    public String updateMailStatus(@PathVariable Long matchId) {
        return gameAdminService.updateMailStatus(matchId, "DEFAULT");
    }

    // 메일 발송
    @PostMapping("/{matchId}/sendEmail")
    @ResponseBody
    public String sendEmails(@PathVariable Long matchId) {
        return gameAdminService.sendEmailsForMatch(matchId);
    }
}