<template>
  <div class="reservations-view-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />

    <!-- 예매 내역, 나의 멤버십, 개인정보 수정 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/reservations' }" @click="navigateTo('/mypage/reservations')">예매 내역</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/membership' }" @click="navigateTo('/mypage/membership')">나의 멤버십</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/edit-profile' }" @click="navigateTo('/mypage/edit-profile')">나의 정보</div>
    </div>
  
    <!-- 예매 내역 페이지 내용 -->
    <div class="reservations-view-content">
      <h2>예매 내역</h2>
      <div v-if="reservations.length === 0" class="no-reservations">
        예매 내역이 없습니다.
      </div>
      <div v-else class="reservation-table-wrapper">
        <table class="reservation-table">
          <thead>
            <tr>
              <th>예매일</th>
              <th>예매번호</th>
              <th>경기</th>
              <th>취소 기한</th>
              <th>상태</th>
            </tr>
          </thead>
          <tbody v-for="reservation in paginatedReservations" :key="reservation.ticket.ticketId">
              <tr>
                <td>{{ formattedDate(reservation.ticket.ticketCreateAt) }}</td>
                <td>
                  <button class="ticket-number" @click="ticketDetailOpen(reservation.ticket.ticketId)">
                    {{ reservation.ticket.ticketNumber }}
                  </button>
                </td>
                <td v-html="matchInfo(reservation)"></td>
                <td>{{ printDay(reservation.matchDate) }} 00:00:00까지</td>
                <td>
                  <span v-if="reservation.ticket.ticketStatus=='BOOKED'" 
                        @click="cancelReservation(reservation.ticket.ticketId)" class="cancel-button">
                    예매 취소
                  </span>
                  <span v-if="reservation.ticket.ticketStatus=='CONFIRMED'" class="status-confirmed">
                    구매 확정
                  </span>
                  <div v-if="reservation.ticket.ticketStatus=='CANCELLED'">
                    <span>{{ formattedDate(reservation.ticket.ticketUpdateAt) }}</span><br>
                    <span v-if="reservation.ticket.ticketStatus=='CANCELLED'" class="status-cancelled">
                      취소 완료
                    </span>
                  </div>
                </td>
              </tr>
              <tr v-if="openedTicketId === reservation.ticket.ticketId" class="ticket-detail">
                <td colspan="5" >
                  <div class="ticket-detail-wrapper">
                    <img :src="stadiumImage(reservation.seatSection)" alt="좌석위치.jpg" class="stadium-image">
                    <table class="ticket-detail-table">
                      <thead>
                        <tr>
                          <th colspan="2">티켓 상세보기</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>경기장소</td>
                          <td>{{ stadium(reservation) }}</td>
                        </tr>
                        <tr>
                          <td>경기시간</td>
                          <td>{{ matchDate(reservation.matchDate) }}</td>
                        </tr>
                        <tr>
                          <td>좌석</td>
                          <td>{{ reservation.seatSection }}</td>
                        </tr>
                        <tr>
                          <td>매수</td>
                          <td>{{ reservation.ticket.ticketDetails[0].ticketDetailAmount }} 장</td>
                        </tr>
                        <tr>
                          <td colspan="2" class="payment-amount">
                            <span class="payment-label">결제 금액 = <span class="payment-value" :class="{ 'line-through': reservation.ticket.ticketStatus == 'CANCELLED' }">
                              {{ new Intl.NumberFormat('ko-KR').format(reservation.ticket.ticketPaidPrice) }}</span>원</span>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </td>
              </tr>
          </tbody>
        </table>
        <div class="pagination-controls">
          <button @click="prevPage" :disabled="currentPage === 1">이전</button>
          <span>{{ currentPage }} / {{ totalPages }}</span>
          <button @click="nextPage" :disabled="currentPage === totalPages">다음</button>
        </div>
      </div>
    </div>
  </div>
</template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
      name: 'ReservationsView',
      components: {
      LogoHeader
    },
    props: {
      user: {
        type: Object,
        default: () => ({ name: '', role: 'guest', email: '', isLoggedIn: false })
      }
    },
    data() {
      return {
        activeMenu: this.$route.path, // 현재 활성화된 경로
        reservations: [
          
        ],
        currentPage: 1,
        itemsPerPage: 5,
        openedTicketId : null,
        stadiumImg: {
          'AWAY-A': require('@/assets/img/stadium/stadium-AA.jpg'),
          'AWAY-B': require('@/assets/img/stadium/stadium-AB.jpg'),
          'AWAY-C': require('@/assets/img/stadium/stadium-AC.jpg'),
          'BLUE-A': require('@/assets/img/stadium/stadium-BA.jpg'),
          'BLUE-B': require('@/assets/img/stadium/stadium-BB.jpg'),
          'BLUE-C': require('@/assets/img/stadium/stadium-BC.jpg'),
          'BLUE-D': require('@/assets/img/stadium/stadium-BD.jpg'),
          'BLUE-E': require('@/assets/img/stadium/stadium-BE.jpg'),
          'BLUE-F': require('@/assets/img/stadium/stadium-BF.jpg'),
          'EAST-A': require('@/assets/img/stadium/stadium-EA.jpg'),
          'EAST-B': require('@/assets/img/stadium/stadium-EB.jpg'),
          'EAST-C': require('@/assets/img/stadium/stadium-EC.jpg'),
          'GOLD-A': require('@/assets/img/stadium/stadium-GA.jpg'),
          'GOLD-B': require('@/assets/img/stadium/stadium-GB.jpg'),
          'GOLD-C': require('@/assets/img/stadium/stadium-GC.jpg'),
          'NORTH-A': require('@/assets/img/stadium/stadium-NA.jpg'),
          'NORTH-B': require('@/assets/img/stadium/stadium-NB.jpg'),
          'NORTH-C': require('@/assets/img/stadium/stadium-NC.jpg'),
          'SOUTH-A': require('@/assets/img/stadium/stadium-SA.jpg'),
          'SOUTH-B': require('@/assets/img/stadium/stadium-SB.jpg'),
          'SOUTH-C': require('@/assets/img/stadium/stadium-SC.jpg'),
          'WEST-A': require('@/assets/img/stadium/stadium-WA.jpg'),
          'WEST-B': require('@/assets/img/stadium/stadium-WB.jpg'),
          'WEST-C': require('@/assets/img/stadium/stadium-WC.jpg')
        },
      };
    },
    mounted(){
      this.getReservationList(sessionStorage.getItem('email'));
    },
    computed: {
      totalPages() {
        return Math.ceil(this.reservations.length / this.itemsPerPage);
      },
      paginatedReservations() {
        const start = (this.currentPage - 1) * this.itemsPerPage;
        const end = start + this.itemsPerPage;
        return this.reservations.slice(start, end);
      },
    },
    watch: {
      // 경로가 변경될 때마다 activeMenu를 업데이트
      $route(to) {
        this.activeMenu = to.path;
      }
    },
    methods: {
      async getReservationList(email){
        try {
          const list = await this.$axios.get('/myPage/ticket', {
            params: {
              email: email,
            }
          });
          this.reservations = list.data;
        } catch (error) {
          console.log("티켓 리스트 불러오기 실패", error);
        }
      },
      navigateTo(route) {
        this.$router.push(route);
        this.activeMenu = route; // 메뉴를 클릭할 때 활성화된 메뉴 업데이트
      },
      cancelReservation(id) {
        if(window.confirm("예매를 취소하시겠습니까?")){
          const params = new URLSearchParams();
          params.append('id', id);
          this.$axios.post("/myPage/ticket/cancel", null, {params:params})
          .then((response)=>{
            alert(response.data);
          })
        }
      },
      nextPage() {
        if (this.currentPage < this.totalPages) {
          this.currentPage++;
        }
      },
      prevPage() {
        if (this.currentPage > 1) {
          this.currentPage--;
        }
      },
      formattedDate(date) {
        return `${date.split("T")[0]} ${date.split("T")[1]}`;
      },
      printDay(date){
        return date.split("T")[0];
      },
      matchDate(date){
        return `${date.split("T")[0]} ${date.split("T")[1]}`;
      },
      matchInfo(reservation){
        const month = reservation.matchDate.split("T")[0].split("-")[1];
        const day = reservation.matchDate.split("T")[0].split("-")[2];
        if(reservation.where=='HOME'){
          return `${parseInt(month)}월 ${parseInt(day)}일<br>MMS  vs ${reservation.opponentTeam}`;
        }
        return `${parseInt(month)}월 ${parseInt(day)}일<br>${reservation.opponentTeam} vs MMS`;
      },
      ticketDetailOpen(id){
        // 티켓 상세창 열려있으면 닫고 닫혀있으면 열도록
        this.openedTicketId = this.openedTicketId === id ? null : id;
      },
      stadium(reservation){
        if(reservation.where=='HOME'){
          return '서울 GS MMS 하이체육관';
        }
        return `${reservation.opponentTeamStadium}`;
      },
      stadiumImage(seatSection){
        return this.stadiumImg[seatSection];
      },
    }
  }
  </script>
  
  <style scoped>
  .reservations-view-page {
      padding-top: var(--header-height);
      padding-bottom: var(--footer-height);
      text-align: center;
  }
  
  /* 메뉴 */
  .menu {
    display: flex;
    justify-content: center;
    gap: 50px;
    padding-bottom: 20px;
  }
  
  .menu-item {
    width: 120px;
    padding: 10px 0;
    position: relative;
    cursor: pointer;
    font-weight: bold;
    color: #565656;
  }
  
  .menu-item::after {
    content: "";
    position: absolute;
    bottom: -5px;
    left: 0;
    width: 100%;
    height: 3px;
    background-color: black;
    transform: scaleX(0);
    transition: transform 0.3s ease;
  }
  
  .active-menu-item {
    color: black;
  }
  
  .active-menu-item::after {
    transform: scaleX(1);
  }
  
  .menu-item:hover::after {
    transform: scaleX(1);
  }
  
  /* 예매 내역 */
  .no-reservations {
    font-size: 18px;
    color: #999;
  }

  .reservation-table-wrapper {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
  }

  .reservation-table {
    width: 100%;
    max-width: 1150px;
    border-collapse: separate;
    border-spacing: 0;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    margin: 0 auto;
    overflow: hidden;
    border-radius: 8px; /* 테두리를 둥글게 */
  }

  .reservation-table th, .reservation-table td {
    padding: 15px 20px;
    text-align: center;
    font-size: 16px;
    border-left: 1px solid #e0e0e0;
  }

  .reservation-table th {
    background-color: #f1f1f1;
    color: #333;
    font-weight: bold;
    text-transform: uppercase;
    letter-spacing: 1px;
    border-bottom: 2px solid #ddd;
  }

  .reservation-table tr {
    transition: background-color 0.3s ease;
  }

  .reservation-table tr:nth-child(even) {
    background-color: #fafafa; /* 짝수 행에 다른 배경색을 추가 */
  }

  .reservation-table td {
    border-bottom: 1px solid #eee;
    font-weight: 500;
    color: #555;
  }

  .ticket-number {
    color: #007bff;
    cursor: pointer;
    text-decoration: underline;
    border: none;
    background: none;
    padding: 0;
    font-size: inherit;
    transition: color 0.3s ease;
  }

  .ticket-number:hover {
    color: green; 
  }

  .cancel-button {
    background-color: #f3aca9;
    color: white;
    border: none;
    padding: 10px 20px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .cancel-button:hover {
    background-color: #ee3029;
  }

  .status-confirmed {
    color: #5cb85c; /* 붉은 색으로 강조 (Bootstrap의 danger 색상) */
    font-weight: bold;
    font-size: large;
  }

  .status-cancelled {
    color: #d9534f; /* 녹색으로 표시 (Bootstrap의 success 색상) */
    font-weight: bold;
    font-size: large;

  }

  .ticket-detail-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: stretch;
    margin-top: 20px;
    background-color: #ffffff;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  }

  .ticket-detail-table {
    width: 45%;
    height: 100%;
    border-collapse: collapse;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    margin: 0 auto; 
    border-radius: 8px;
    overflow: hidden;
  }

  .ticket-detail-table th {
    background-color: #f7f7f7;
    color: #333;
    font-weight: bold;
    font-size: 20px; 
    text-align: center;
    padding: 15px;
    border-left: 1px solid #e0e0e0;
  }

  .ticket-detail-table td {
    border-bottom: 1px solid #ddd;
    padding: 15px;
    text-align: center;
    font-size: 16px; 
    font-weight: bold; 
    color: #555;
    border-left: 1px solid #e0e0e0;
  }

  .payment-amount {
    padding-right: 15px;
    font-weight: bold;
  }

  .payment-label {
    float: right;
    color:#a82823;
    font-size: 18px; 
  }

  .payment-value {
    color:rgb(255, 70, 70);
    font-size: 24px; 
    font-weight: bold;
    margin: 5px;
  }
  
  .line-through {
    text-decoration: line-through;
    color: rgb(235, 170, 170);
  }

  .stadium-image {
    flex-shrink: 0;
    width: 50%;
    height: 100%;
    object-fit: cover;
    border: 1px solid #ddd; 
    border-radius: 8px;
  }

  .pagination-controls {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
    margin-top: 20px;
    margin-bottom: 20px;
  }

  .pagination-controls button {
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    padding: 10px 15px;
    cursor: pointer;
    font-weight: bold;
    transition: background-color 0.3s ease, box-shadow 0.3s ease;
  }

  .pagination-controls button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }

  .pagination-controls button:hover:not(:disabled) {
    background-color: #0056b3;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  }

  .pagination-controls span {
    font-size: 16px;
    font-weight: bold;
  }
  </style>