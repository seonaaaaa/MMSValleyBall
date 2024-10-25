package com.team.MMSValleyBall.controller;

import com.team.MMSValleyBall.dto.TicketSalesDTO;
import com.team.MMSValleyBall.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    //티켓 안내 페이지
    @GetMapping("/info")
    public ResponseEntity<String> viewTicket() {
        return ResponseEntity.ok("Ticket Info");
    }

    //티켓 구매 페이지
    @GetMapping("/purchase")
    public ResponseEntity<String> viewTicketPurchase() {
        //
        return ResponseEntity.ok("Ticket purchase Info");
    }
    //티켓 예매 모달 - 메인
    @GetMapping("/purchase/main")
    public ResponseEntity<String> viewTicketPurchaseMain() {
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
