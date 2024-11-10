package com.team.MMSValleyBall.service;

import com.team.MMSValleyBall.dto.AvailableSeatDTO;
import com.team.MMSValleyBall.dto.SeatDTO;
import com.team.MMSValleyBall.dto.SectionInfo;
import com.team.MMSValleyBall.dto.TicketSalesDTO;
import com.team.MMSValleyBall.entity.*;
import com.team.MMSValleyBall.enums.SeatSection;
import com.team.MMSValleyBall.enums.TicketStatus;
import com.team.MMSValleyBall.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final SeatRepository seatRepository;
    private final TicketDetailRepository ticketDetailRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, MatchRepository matchRepository, SeatRepository seatRepository, TicketDetailRepository ticketDetailRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.matchRepository = matchRepository;
        this.seatRepository = seatRepository;
        this.ticketDetailRepository = ticketDetailRepository;
    }

    //티켓 넘버 생성 메서드
    public String createTicketNumber(String email, Long matchId, int ticketDetailAmount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDate = LocalDateTime.now().format(formatter);
        String userId = String.valueOf(userRepository.findByUserEmail(email).getUserId());
        String formattedMatchId = String.valueOf(matchId);
        // userId와 matchId를 포맷팅하여 한 자리수일 경우 0을 붙임
        if(userId.length()==1){
            userId = String.format("%02d", userId);
        }
        if(String.valueOf(matchId).length()==1){
            formattedMatchId = String.format("%02d", matchId);
        }

        return "T" + formattedDate
                + formattedMatchId
                + ticketDetailAmount
                + userId;
    }

    public List<AvailableSeatDTO> getAvailableSeatsByMatch(Long matchId) {
        // section별 잔여좌석 조회
        List<Object[]> resultList = ticketRepository.findAvailableSeatsByMatch(matchId);

        // seatId 기준으로 resultList 정렬
        resultList.sort(Comparator.comparing(result -> (Long) result[0]));

        // zone별 잔여 좌석 수를 저장할 맵
        Map<String, Long> zoneSeatMap = new HashMap<>();

        // section별 좌석수 dto 추가 및 zone별 잔여 좌석 수 집계
        for (Object[] result : resultList) {
            String zoneName = (String) result[1]; // zone 이름
            String sectionName = ((SeatSection) result[2]).toString(); // section 이름
            Long availableSeats = ((Number) result[3]).longValue(); // 잔여석 수 안전 변환

            // zone별 잔여 좌석 수 집계
            zoneSeatMap.merge(zoneName, availableSeats, Long::sum);
        }

        // DTO 리스트로 변환
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
            sectionDto.setSeatId((Long) result[0]);
            sectionDto.setSectionName((String) result[1] + "/" + ((SeatSection) result[2]).toString()); // 섹션 이름
            sectionDto.setAvailableSeatAmount(((Number) result[3]).longValue()); // 잔여석 수 변환

            // 해당 zone에 섹션 추가
            for (AvailableSeatDTO zoneDto : dtoList) {
                if (zoneDto.getZoneName().equals((String) result[1])) { // zone 이름 비교
                    zoneDto.getSections().add(sectionDto); // 섹션 추가
                }
            }
        }

        // zoneName 순서에 따라 dtoList 정렬
        List<String> zoneOrder = Arrays.asList("GOLD", "BLUE", "AWAY", "NORTH", "WEST", "SOUTH", "EAST");
        dtoList.sort(Comparator.comparingInt(zoneDto -> zoneOrder.indexOf(zoneDto.getZoneName())));


        System.out.println("ticket service - available seats : " + dtoList);
        return dtoList;
    }

    public void reserveTickets(String email,TicketSalesDTO dto) {
        // Match와 Seat 정보 조회
        Match match = matchRepository.findById(dto.getMatchId()).orElseThrow(() ->
                new RuntimeException("Match not found for ID: " + dto.getMatchId()));
        Seat seat = seatRepository.findById(dto.getTicketDetailSeat()).orElseThrow(() ->
                new RuntimeException("Seat not found for ID: " + dto.getTicketDetailSeat()));
        Users user = userRepository.findByUserEmail(email);

        // Ticket 객체 생성
        Ticket newTicket = new Ticket();
        newTicket.setTicketMatch(match);
        newTicket.setTicketPaidPrice(dto.getTicketPaidPrice());
        newTicket.setTicketNumber(createTicketNumber(email, dto.getMatchId(), dto.getTicketDetailAmount()));
        newTicket.setTicketUser(user);
        newTicket.setTicketStatus(TicketStatus.BOOKED);
        newTicket.setTicketCreateAt(LocalDateTime.now());

        // TicketDetail 객체 생성
        TicketDetail newTicketDetail = new TicketDetail();
        newTicketDetail.setTicketDetailAmount(dto.getTicketDetailAmount());
        newTicketDetail.setTicketDetailTicket(newTicket);
        newTicketDetail.setTicketDetailSeat(seat);

        // Ticket 저장
        if (ObjectUtils.isEmpty(newTicket.getTicketDetails())) {
            List<TicketDetail> detailList = new ArrayList<>();
            detailList.add(newTicketDetail);
            newTicket.setTicketDetails(detailList);
        }

        ticketRepository.save(newTicket);
        ticketDetailRepository.save(newTicketDetail);
    }

    public List<SeatDTO> findSeatAll() {
        return seatRepository.findAll().stream().map(SeatDTO::fromEntity).toList();
    }
}
