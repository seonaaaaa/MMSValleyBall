package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.TicketSalesDTO;
import com.team.MMSValleyBall.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    //티켓 넘버 생성 메서드
    public String createTicketNumber(TicketSalesDTO ticketSalesDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDate = ticketSalesDTO.getTicketCreateAt().format(formatter);

        return "T" + formattedDate
                + ticketSalesDTO.getMatchId()
                + ticketSalesDTO.getTicketDetailAmount()
                + ticketSalesDTO.getUserId();
    }
}
