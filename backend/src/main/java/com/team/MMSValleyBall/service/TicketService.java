package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.AvailableSeatDTO;
import com.team.MMSValleyBall.dto.SectionInfo;
import com.team.MMSValleyBall.dto.TicketSalesDTO;
import com.team.MMSValleyBall.enums.SeatSection;
import com.team.MMSValleyBall.repository.TicketRepository;
import com.team.MMSValleyBall.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    //티켓 넘버 생성 메서드
    public String createTicketNumber(TicketSalesDTO ticketSalesDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDate = ticketSalesDTO.getTicketCreateAt().format(formatter);
        Long userId = userRepository.findByUserEmail(ticketSalesDTO.getUserEmail()).getUserId();
        return "T" + formattedDate
                + ticketSalesDTO.getMatchId()
                + ticketSalesDTO.getTicketDetailAmount()
                + userId;
    }

    public List<AvailableSeatDTO> getAvailableSeatsByMatch(Long matchId) {
        // section별 잔여좌석 조회
        List<Object[]> resultList = ticketRepository.findAvailableSeatsByMatch(matchId);

        // zone별 잔여 좌석 수를 저장할 맵
        Map<String, Long> zoneSeatMap = new HashMap<>();

        // section별 좌석수 dto 추가 및 zone별 잔여 좌석 수 집계
        for (Object[] result : resultList) {
            String seatName = ((String) result[0]) + "/" + ((SeatSection) result[1]).toString(); // enum 타입인 SeatSection을 문자열로 변환
            Long availableSeats = (Long) result[2]; // 잔여석 수

            // zone 이름 추출
            String zoneName = seatName.split("/")[0]; // seatName에서 zone 이름 추출

            // zone별 잔여 좌석 수 집계
            zoneSeatMap.merge(zoneName, availableSeats, Long::sum);
        }

        // object를 dto로 변환
        List<AvailableSeatDTO> dtoList = new ArrayList<>();

        // zone별 좌석 수를 dtoList에 추가
        for (Map.Entry<String, Long> entry : zoneSeatMap.entrySet()) {
            AvailableSeatDTO dto = new AvailableSeatDTO();
            dto.setZoneName(entry.getKey()); // zone 이름
            dto.setAvailableSeatAmount(entry.getValue()); // zone별 잔여석 수
            dto.setSections(new ArrayList<>()); // sections 초기화
            dtoList.add(dto);
        }

        // section별 좌석수 dtoList에 추가
        for (Object[] result : resultList) {
            SectionInfo sectionDto = new SectionInfo();
            sectionDto.setSectionName(((String) result[0]) + "/" + ((SeatSection) result[1]).toString()); // 섹션 이름
            sectionDto.setAvailableSeatAmount((Long) result[2]); // 잔여석 수

            // 해당 zone에 섹션 추가
            for (AvailableSeatDTO x : dtoList) {
                if (((String) result[0]).equals(x.getZoneName())) { // 문자열 비교 수정
                    if (x.getSections() == null) {
                        x.setSections(new ArrayList<>()); // 섹션 리스트 초기화
                    }
                    x.getSections().add(sectionDto); // 섹션 추가
                }
            }
        }

        System.out.println("ticket service - available seats : " + dtoList);
        return dtoList;
    }


}
