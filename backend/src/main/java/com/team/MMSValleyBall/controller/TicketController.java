package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.AvailableSeatDTO;
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
        List<AvailableSeatDTO> availableSeats = ticketService.getAvailableSeatsByMatch(matchId);
        System.out.println("ticket controller - zone available seat : " + availableSeats.subList(0,1));

        //4. 보내기
        Map<String, Object> response = new HashMap<>();
        response.put("matchInfo", match);
        response.put("availableSeatList", availableSeats);

        return ResponseEntity.ok(response);
    }

    //티켓 예매 모달 - 구역 선택(1-get)
    @GetMapping("/purchase/selection/{matchId}")
    ResponseEntity<Map<String, Object>> viewTicketPurchaseSelection(@PathVariable Long matchId) {
        //1. 경기 정보 가져오기
        MatchTableDTO match = matchService.getOneMatch(matchId);

        //2. 잔여석 정보 가져오기, zone별 잔여석 수 계산
        List<AvailableSeatDTO> availableSeats = ticketService.getAvailableSeatsByMatch(matchId);
        System.out.println("ticket controller - available seats : " + availableSeats);

        //4. 경기 정보, 잔여석 정보, TicketSalesDTO 보내기
        Map<String, Object> response = new HashMap<>();
        response.put("matchInfo", match);
        response.put("availableSeatList", availableSeats);

        return ResponseEntity.ok(response);
    }
//
//    //티켓 예매 모달 - 구역 선택(2-post)
//    @PostMapping("/purchase/selection/select")
//    public ResponseEntity<String> viewTicketPurchaseSelected() {
//
//        return ResponseEntity.ok("Ticket purchase modal section selected");
//    }

    // 티켓 예매 모달 - 결제하기 (1-get)
    @PostMapping("/purchase/payment")
    public ResponseEntity<?> viewTicketPurchasePayment(@RequestBody List<Map<String, Object>> selectedSeats) {
        // 선택된 좌석 정보를 처리하는 로직 추가
        for (Map<String, Object> seat : selectedSeats) {
            Long matchId = Long.valueOf(seat.get("matchId").toString());
            String sectionName = seat.get("sectionName").toString();
            int ticketAmount = Integer.parseInt(seat.get("ticketAmount").toString());

            // 여기서 해당 정보를 사용하여 ticketSalesDTO 생성 로직 구현
            String userEmail = "kimka@cbc.com";
            ticketService.makeTicketSalesDTO(userEmail, matchId, sectionName, ticketAmount);

            // 디버깅을 위한 로그 출력
            System.out.println("Match ID: " + matchId + ", Section Name: " + sectionName + ", Ticket Amount: " + ticketAmount);
        }

        // 예약이 성공적으로 처리되었음을 나타내는 응답
        return ResponseEntity.ok("Reservation successful");
    }


    //티켓 예매 모달 - 결제하기(2-post)
    @PostMapping("/purchase/payment/completed")
    public ResponseEntity<String> viewTicketPurchasePaymentCompleted(@RequestBody TicketSalesDTO ticketSalesDTO) {
        //티켓번호 생성
        String ticketNumber = ticketService.createTicketNumber(ticketSalesDTO);
        ticketService.reserveTickets(ticketSalesDTO);
        return ResponseEntity.ok("Reservation successful");
    }

}
