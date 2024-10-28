package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.AvailableSeatDTO;
import com.team.MMSValleyBall.dto.MatchTableDTO;
import com.team.MMSValleyBall.dto.MoneyDTO;
import com.team.MMSValleyBall.dto.TicketSalesDTO;
import com.team.MMSValleyBall.service.MatchService;
import com.team.MMSValleyBall.service.TicketService;
import com.team.MMSValleyBall.service.UsersBalanceService;
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
    private final UsersBalanceService usersBalanceService;

    public TicketController(TicketService ticketService, MatchService matchService, UsersBalanceService usersBalanceService) {
        this.ticketService = ticketService;
        this.matchService = matchService;
        this.usersBalanceService = usersBalanceService;
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

        //3. 경기 정보, 잔여석 정보 보내기
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
    public ResponseEntity<?> viewTicketPurchasePayment(@RequestBody Map<String, Object> selectedSeat) {
        // 1. 로그인 정보로 사용자 정보 가져오기
        String userEmail = "kimka@cbc.com";

        // 2. 선택된 좌석 정보를 처리하는 로직 추가
        Long matchId = Long.valueOf(selectedSeat.get("matchId").toString());
        Long seatId = Long.valueOf(selectedSeat.get("seatId").toString());
        int ticketAmount = Integer.parseInt(selectedSeat.get("ticketAmount").toString());

        // TicketSalesDTO 생성
        TicketSalesDTO dto = new TicketSalesDTO();
        dto.setUserEmail(userEmail);
        dto.setMatchId(matchId);
        // 좌석 ID
        dto.setTicketDetailSeat(seatId);
        // 예매 좌석 수
        dto.setTicketDetailAmount(ticketAmount);

        // 디버깅을 위한 로그 출력
        System.out.println("Match ID: " + matchId + ", Seat ID: " + seatId + ", Ticket Amount: " + ticketAmount);

        // 3. 경기 정보 가져오기
        MatchTableDTO match = matchService.getOneMatch(matchId);

        // 4. 로그인 정보로 사용자의 충전금액 찾기
        MoneyDTO moneyDTO = usersBalanceService.getUsersBalance(userEmail);

        // 5. 응답으로 보낼 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("ticketSalesDto", dto);
        response.put("userBalance", moneyDTO);
        response.put("matchInfo", match);

        // 예약이 성공적으로 처리되었음을 나타내는 응답
        return ResponseEntity.ok(response);
    }

    //티켓 예매 모달 - 결제하기(2-post)
    @PostMapping("/purchase/payment/completed")
    public ResponseEntity<String> viewTicketPurchasePaymentCompleted(@RequestBody TicketSalesDTO ticketSalesDTO) {
        // 1. 티켓번호 생성
        String ticketNumber = ticketService.createTicketNumber(ticketSalesDTO);
        ticketSalesDTO.setTicketNumber(ticketNumber);
        // 2. 티켓 판매 정보 저장
        ticketService.reserveTickets(ticketSalesDTO);
        return ResponseEntity.ok("Reservation successful");
    }

}
