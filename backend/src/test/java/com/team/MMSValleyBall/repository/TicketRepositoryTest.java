package com.team.MMSValleyBall.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class TicketRepositoryTest {
    @Autowired
    TicketRepository ticketRepository;

    @Test
    void findAvailableSeatsByMatch() {
        Long matchId = 45L;
        List<Object[]> resultList = ticketRepository.findAvailableSeatsByMatch(matchId);

        // 결과 출력
        resultList.forEach(result -> {
            String seatZone = (String) result[0];
            String seatSection = (String) result[1];
            Integer availableSeats = (Integer) result[2];
            System.out.printf("Zone: %s, Section: %s, Available Seats: %d%n", seatZone, seatSection, availableSeats);
        });

        // 검증 예제
        Assertions.assertNotNull(resultList, "Result list should not be null");
        Assertions.assertTrue(resultList.size() > 0, "There should be available seats data for the match");

        // 특정 결과 검증 (예: 특정 구역, 섹션의 잔여 좌석이 예상 값과 일치하는지 확인)
        // Assertions.assertEquals(expectedAvailableSeats, actualAvailableSeats, "Expected available seats do not match");
    }
}