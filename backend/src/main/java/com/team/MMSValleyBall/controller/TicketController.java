package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.*;
import com.team.MMSValleyBall.entity.Users;
import com.team.MMSValleyBall.repository.SeatRepository;
import com.team.MMSValleyBall.service.MatchService;
import com.team.MMSValleyBall.service.TicketService;
import com.team.MMSValleyBall.service.UserService;
import com.team.MMSValleyBall.service.UsersBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketController {
    private final TicketService ticketService;
    private final MatchService matchService;
    private final UsersBalanceService usersBalanceService;
    private final UserService userService;
    private final SeatRepository seatRepository;

    public TicketController(TicketService ticketService, MatchService matchService, UsersBalanceService usersBalanceService, UserService userService, SeatRepository seatRepository) {
        this.ticketService = ticketService;
        this.matchService = matchService;
        this.usersBalanceService = usersBalanceService;
        this.userService = userService;
        this.seatRepository = seatRepository;
    }

    //티켓 안내 페이지
    @GetMapping("/info")
    public ResponseEntity<String> viewTicket() {
        return ResponseEntity.ok("Ticket Info");
    }

    //티켓 구매 페이지
    @GetMapping("/purchase")
    public ResponseEntity<?> viewTicketPurchase() {
        try {
            // 경기 정보 가져오기
            List<MatchTableDTO> matches = matchService.convertMatchToString();

            // matches가 비어있는지 체크
            if (matches.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No matches available");
            }

            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            System.err.println("Error fetching matches: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching match data");
        }
    }

//    @GetMapping("/purchase/m")
//    public ResponseEntity<?> viewTicketPurchase(@RequestParam("email") String email){
//
//    }

    // 티켓 구매 모달창 하나로 처리
    @GetMapping("/purchase/modal")
    public ResponseEntity<?> viewTicketPurchaseModal(
            @RequestParam("matchId") Long matchId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String email = authentication.getName();

            log.info("### ticket controller - request param: "  + matchId);
            // 사용자 정보 - 충전금액
            MoneyDTO moneyDTO = usersBalanceService.getUsersBalance(email);
            // 사용자 정보 - 멤버십
            Map<String, Object> userMembership = userService.findMembership(email);

            // 경기 정보
            MatchTableDTO match = matchService.getOneMatch(matchId);

            // 잔여석 정보
            List<AvailableSeatDTO> availableSeats = ticketService.getAvailableSeatsByMatch(matchId);

            // ticketSalesDTO
            TicketSalesDTO dto = new TicketSalesDTO();
            dto.setUserEmail(email);

            // 좌석 가격 정보
            List<SeatDTO> seatDTOList = ticketService.findSeatAll();

            TicketPurchaseResponseDTO responseDTO = new TicketPurchaseResponseDTO();
            responseDTO.setTicketSalesDto(dto);
            responseDTO.setMatchInfo(match);
            responseDTO.setUserBalance(moneyDTO.getLeftMoney());
            responseDTO.setUserMembership(userMembership);
            responseDTO.setSeatDTOList(seatDTOList);
            responseDTO.setAvailableSeatsList(availableSeats);

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);  // 응답을 반환합니다.
        }
        catch (Exception e) {
            log.error("Error fetching user data", e);  // 예외 로그 추가
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching user data"));
        }
    }

    // 티켓 구매 완료 POST API
    @PostMapping("/purchase/completed")
    public ResponseEntity<?> membershipPurchase(@RequestBody TicketSalesDTO ticketSalesDTO) {
        // ticketNumber 생성
        ticketSalesDTO.setTicketNumber(ticketService.createTicketNumber(ticketSalesDTO));
        try {
            // 로그 출력
            log.info("Match ID: {}", ticketSalesDTO.getMatchId());
            log.info("User ID: {}", ticketSalesDTO.getUserEmail());

            // 멤버십 구매 정보 저장
            ticketService.reserveTickets(ticketSalesDTO);

            return ResponseEntity.ok("Purchase completed successfully!");
        } catch (DataAccessException dae) {
            log.error("Data access error while completing purchase: {}", dae.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error completing purchase");
        } catch (Exception e) {
            log.error("Unexpected error while completing purchase: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error completing purchase");
        }
    }

}
