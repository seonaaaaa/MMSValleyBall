package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchWithTeamDTO;
import com.team.MMSValleyBall.dto.SeasonDTO;
import com.team.MMSValleyBall.dto.TeamDTO;
import com.team.MMSValleyBall.entity.Match;
import com.team.MMSValleyBall.enums.MailStatus;
import com.team.MMSValleyBall.enums.MatchStadium;
import com.team.MMSValleyBall.enums.MatchStatus;
import com.team.MMSValleyBall.repository.SeasonRepository;
import com.team.MMSValleyBall.repository.TeamRepository;
import com.team.MMSValleyBall.service.GameAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin/game")
public class GameAdminController {

    private final GameAdminService gameAdminService;

    public GameAdminController(GameAdminService gameAdminService, SeasonRepository seasonRepository, TeamRepository teamRepository) {
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
            @RequestParam(name = "seasonId", required = false) Long seasonId,
            @RequestParam(name = "matchRoundId", required = false) Integer matchRoundId,
            @RequestParam(name = "matchStadium", required = false) MatchStadium matchStadium,
            @RequestParam(name = "teamId", required = false) Long teamId,
            @RequestParam(name = "matchMailStatus", required = false) MailStatus matchMailStatus,
            @RequestParam(name = "matchStatus", required = false) MatchStatus matchStatus,
            Model model) {

        // Sort 객체 설정
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 필터링 및 정렬된 경기 데이터 조회
        Page<MatchWithTeamDTO> matchPage = gameAdminService.findAllFiltered(seasonId, matchRoundId, matchStadium, teamId, matchMailStatus, matchStatus, pageable);

        // 라운드 데이터 설정
        List<Integer> rounds = Arrays.asList(1, 2, 3, 4, 5, 6);
        model.addAttribute("rounds", rounds);

        // 페이지네이션 데이터 설정
        model.addAttribute("matchList", matchPage.getContent());
        model.addAttribute("totalMatches", matchPage.getTotalElements());
        model.addAttribute("totalPages", matchPage.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size); // 인덱스 계산용

        // 시즌과 팀 데이터 설정
        List<SeasonDTO> seasons = gameAdminService.getAllSeasons();
        List<TeamDTO> teams = gameAdminService.getAllTeams();
        model.addAttribute("seasons", seasons);
        model.addAttribute("teams", teams);

        return "gameList";
    }

//    // 시즌 데이터 조회 API
//    @GetMapping("/game/seasons")
//    public ResponseEntity<List<SeasonDTO>> getSeasons() {
//        List<SeasonDTO> seasons = gameAdminService.getAllSeasons();
//        return ResponseEntity.ok(seasons);
//    }
//
//    // 팀 데이터 조회 API
//    @GetMapping("/game/teams")
//    public ResponseEntity<List<TeamDTO>> getTeams() {
//        List<TeamDTO> teams = gameAdminService.getAllTeams();
//        return ResponseEntity.ok(teams);
//    }

    // 신규 경기 등록 페이지
    @GetMapping("/insert")
    public String insertGameForm(Model model) {
        List<SeasonDTO> seasons = gameAdminService.getAllSeasons();
        List<TeamDTO> teams = gameAdminService.getAllTeams();
        model.addAttribute("seasons", seasons);
        model.addAttribute("teams", teams);

        // 라운드 데이터 설정
        List<Integer> rounds = Arrays.asList(1, 2, 3, 4, 5, 6);
        model.addAttribute("rounds", rounds);

        return "gameInsert";
    }

    // 신규 경기 등록
    @PostMapping("/insert")
    public ResponseEntity<String> insertGame(@RequestBody MatchWithTeamDTO matchDTO) {
        String result = String.valueOf(gameAdminService.insertMatch(matchDTO));
        return ResponseEntity.ok(result);
    }

    // 경기 수정 페이지
    @GetMapping("/update/{matchId}")
    public String updateGameForm(@PathVariable("matchId") Long matchId, Model model) {
        MatchWithTeamDTO matchDTO = gameAdminService.getMatchById(matchId);
        System.out.println("matchRoundId: " + matchDTO.getMatchRoundId());  // 디버깅용 출력
        model.addAttribute("match", matchDTO);

        // 날짜와 시간 분리
        if (matchDTO.getMatchDate() != null) {
            model.addAttribute("matchDatePart", matchDTO.getMatchDate().toLocalDate());
            model.addAttribute("matchTimePart", matchDTO.getMatchDate().toLocalTime().withNano(0)); // 초 제거
        }

        // 라운드 데이터 설정
        List<Integer> rounds = Arrays.asList(1, 2, 3, 4, 5, 6);
        model.addAttribute("rounds", rounds);

        List<SeasonDTO> seasons = gameAdminService.getAllSeasons();
        List<TeamDTO> teams = gameAdminService.getAllTeams();
        model.addAttribute("seasons", seasons);
        model.addAttribute("teams", teams);

        return "gameUpdate";
    }

    // 경기 수정
    @PostMapping("/update/{matchId}")
    public ResponseEntity<String> updateGame(@PathVariable("matchId") Long matchId, @RequestBody MatchWithTeamDTO matchDTO) {
        String result = gameAdminService.updateMatch(matchId, matchDTO);
        return ResponseEntity.ok(result);
    }

    // 경기 논리적 삭제 (비활성화)
    @PatchMapping("/delete/{matchId}")
    @ResponseBody
    public String deactivateMatch(@PathVariable("matchId") Long matchId) {
        return gameAdminService.deactivateMatch(matchId);
    }

    // 경기 상태 업데이트 (활성화)
    @PatchMapping("/activate/{matchId}")
    @ResponseBody
    public String activateMatch(@PathVariable("matchId") Long matchId) {
        return gameAdminService.activateMatch(matchId);
    }

    // 메일 상태 변경
    @PatchMapping("/update-mail-status/{matchId}")
    @ResponseBody
    public String updateMailStatus(@PathVariable("matchId") Long matchId) {
        return gameAdminService.updateMailStatus(matchId, "DEFAULT");
    }

    // 메일 발송
    @PostMapping("/{matchId}/sendEmail")
    @ResponseBody
    public String sendEmails(@PathVariable("matchId") Long matchId) {
        return gameAdminService.sendEmailsForMatch(matchId);
    }
}