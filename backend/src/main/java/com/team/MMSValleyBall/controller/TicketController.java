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
    public ResponseEntity<?> viewTicketPurchase(@RequestParam("email") String email) {
        try {
            // 경기 정보 가져오기
            List<MatchTableDTO> matches = matchService.convertMatchToString();

            // matches가 비어있는지 체크
            if (matches.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No matches available");
            }

            // 사용자의 멤버십 정보 가져오기
            Map<String, Object> userMembership = userService.findMembership(email);

            // response에 보내기
            Map<String, Object> response = new HashMap<>();
            response.put("matches", matches);
            response.put("userMembership", userMembership);

            return ResponseEntity.ok(response); // matches를 JSON으로 반환
        } catch (Exception e) {
            System.err.println("Error fetching matches: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching match data");
        }
    }

    // 티켓 구매 모달창 하나로 처리
    @GetMapping("/purchase/modal")
//    public ResponseEntity<MultiValueMap<String,Object>> viewTicketPurchaseModal(
    public ResponseEntity<?> viewTicketPurchaseModal(
            @RequestParam("email") String email,
            @RequestParam("matchId") Long matchId) {
        try {
            // 1. 세션에서 보낸 RequestParam으로 email 받아옴.
//            if (email == null || email.isEmpty()) {
//                return ResponseEntity.badRequest().body(Map.of("error", "Invalid email parameter"));
//            }
            log.info("### ticket controller - request param: " + email + "/" + matchId);

            // 사용자 정보 - 충전금액
            MoneyDTO moneyDTO = usersBalanceService.getUsersBalance(email);
//            if (moneyDTO == null) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Could not fetch user balance"));
//            }

            // 사용자 정보 - 멤버십
            Map<String, Object> userMembership = userService.findMembership(email);
//            if (userMembership == null) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Could not fetch user membership"));
//            }

            // 경기 정보
            MatchTableDTO match = matchService.getOneMatch(matchId);
//            if (match == null) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Could not fetch match information"));
//            }

            // 잔여석 정보
            List<AvailableSeatDTO> availableSeats = ticketService.getAvailableSeatsByMatch(matchId);
//            if (availableSeats == null) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Could not fetch available seats"));
//            }

            // ticketSalesDTO
            TicketSalesDTO dto = new TicketSalesDTO();
            dto.setUserEmail(email);

            // 좌석 가격 정보
            List<SeatDTO> seatDTOList = ticketService.findSeatAll();

            // API 응답 데이터
//            MultiValueMap<String, Object> response = new LinkedMultiValueMap<>();
//            response.put("setTicketSalesDto", (List<Object>) dto);
//            response.put("setAvailableSeatsList", Collections.singletonList(availableSeats));
            TicketPurchaseResponseDTO responseDTO = new TicketPurchaseResponseDTO();
            responseDTO.setTicketSalesDto(dto);
            responseDTO.setMatchInfo(match);
            responseDTO.setUserBalance(moneyDTO.getLeftMoney());
            responseDTO.setUserMembership(userMembership);
            responseDTO.setSeatDTOList(seatDTOList);
            responseDTO.setAvailableSeatsList(availableSeats);

//            Map<String, Object> response = new HashMap<>();
//            response.put("response")

            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);  // 응답을 반환합니다.
//            return response;  // 응답을 반환합니다.
        }
        catch (Exception e) {
            log.error("Error fetching user data", e);  // 예외 로그 추가
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching user data"));
//            return null;
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
