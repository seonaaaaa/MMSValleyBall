package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchTableDTO;
import com.team.MMSValleyBall.dto.TicketSalesDTO;
import com.team.MMSValleyBall.service.MatchService;
import com.team.MMSValleyBall.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final MatchService matchService;

    public TicketController(TicketService ticketService, MatchService matchService) {
        this.ticketService = ticketService;
        this.matchService = matchService;
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

            System.out.println("service - match size : " + matches.size()); // matches.size()로 전체 개수를 출력
            return ResponseEntity.ok(matches); // matches를 JSON으로 반환
        } catch (Exception e) {
            System.err.println("Error fetching matches: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching match data");
        }
    }

    //티켓 예매 모달 - 메인
    @GetMapping("/purchase/main/{matchId}")
    ResponseEntity<Map<String, Object>> viewTicketPurchaseMain(@PathVariable Long matchId) {
        //1. 로그인 세션 확인 -> 로그인 안된 상태면 로그인 페이지로 이동

        //2. 경기 정보 가져오기
        MatchTableDTO match = matchService.getOneMatch(matchId);

        //3. 잔여석 정보 가져오기
        List<Object[]> leftSeatList = ticketService.getAvailableSeatsByMatch(matchId);

        //4. 보내기
        Map<String, Object> response = new HashMap<>();
        response.put("matchInfo", match);
        response.put("availableSeatList", leftSeatList);

        return ResponseEntity.ok(response);
    }

    //티켓 예매 모달 - 구역 선택(1-get)
    @GetMapping("/purchase/selection/{matchId}")
    ResponseEntity<Map<String, Object>> viewTicketPurchaseSelection(@PathVariable Long matchId) {
        //1. 경기 정보 가져오기
        MatchTableDTO match = matchService.getOneMatch(matchId);

        //2. 잔여석 정보 가져오기
        List<Object[]> leftSeatList = ticketService.getAvailableSeatsByMatch(matchId);

        //3. 보내기
        Map<String, Object> response = new HashMap<>();
        response.put("matchInfo", match);
        response.put("availableSeatList", leftSeatList);

        return ResponseEntity.ok(response);
    }
    //티켓 예매 모달 - 구역 선택(2-post)
    @PostMapping("/purchase/selection/select")
    public ResponseEntity<String> viewTicketPurchaseSelected() {
        return ResponseEntity.ok("Ticket purchase modal section selected");
    }
    //티켓 예매 모달 - 결제하기(1-get)
    @GetMapping("/purchase/payment")
    public ResponseEntity<String> viewTicketPurchasePayment() {
        return ResponseEntity.ok("Ticket purchase modal payment");
    }
    //티켓 예매 모달 - 결제하기(2-post)
    @PostMapping("/purchase/payment/completed")
    public ResponseEntity<String> viewTicketPurchasePaymentCompleted(@RequestBody TicketSalesDTO ticketSalesDTO) {
        //티켓번호 생성
        String ticketNumber = ticketService.createTicketNumber(ticketSalesDTO);
        return ResponseEntity.ok("Ticket purchase modal payment completed");
    }

}
