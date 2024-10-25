package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.MatchDTO;
import com.team.MMSValleyBall.dto.TicketSalesDTO;
import com.team.MMSValleyBall.service.MatchService;
import com.team.MMSValleyBall.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            List<MatchDTO> matches = matchService.findAllMatches();
            System.out.println("service - match size : " + matches.get(45));

            return ResponseEntity.ok(matches); // matches를 JSON으로 반환
        } catch (Exception e) {
            System.err.println("Error fetching matches: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching match data");
        }
    }

    //티켓 예매 모달 - 메인
    @GetMapping("/purchase/main")
    public ResponseEntity<String> viewTicketPurchaseMain() {
        //잔여석 정보 가져오기
        return ResponseEntity.ok("Ticket purchase modal main");
    }
    //티켓 예매 모달 - 구역 선택(1-get)
    @GetMapping("/purchase/selection")
    public ResponseEntity<String> viewTicketPurchaseSelection() {
        return ResponseEntity.ok("Ticket purchase modal selection");
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
