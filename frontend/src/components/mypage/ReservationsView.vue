<template>
    <div class="reservations-view-page">
      <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
      <LogoHeader />
  
      <!-- 예매 내역, 멤버십 내역, 개인정보 수정, 회원 탈퇴 메뉴 -->
      <div class="menu">
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/reservations' }" @click="navigateTo('/mypage/reservations')">예매 내역</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/membership' }" @click="navigateTo('/mypage/membership')">멤버십 내역</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/edit-profile' }" @click="navigateTo('/mypage/edit-profile')">개인정보 수정</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/delete-account' }" @click="navigateTo('/mypage/delete-account')">회원 탈퇴</div>
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
                <th>취소 가능 기한</th>
                <th>상태</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="reservation in paginatedReservations" :key="reservation.id">
                <td>{{ reservation.eventName }}</td>
                <td>{{ reservation.date }}</td>
                <td>{{ reservation.time }}</td>
                <td>{{ reservation.seat }}</td>
                <td><button @click="cancelReservation(reservation.id)" class="cancel-button">예매 취소</button></td>
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
    data() {
      return {
        activeMenu: this.$route.path, // 현재 활성화된 경로
        reservations: [
          // 예시 데이터
          { id: 1, eventName: '배구 경기', date: '2024-11-10', time: '15:00', seat: 'A3-12' },
          { id: 2, eventName: '친선 경기', date: '2024-11-15', time: '19:00', seat: 'B1-08' },
          { id: 3, eventName: '리그 경기', date: '2024-11-20', time: '18:00', seat: 'C2-05' },
          { id: 4, eventName: '결승전', date: '2024-11-25', time: '20:00', seat: 'D4-10' },
          // 추가 데이터
        ],
        currentPage: 1,
        itemsPerPage: 2,
      };
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
      navigateTo(route) {
        this.$router.push(route);
        this.activeMenu = route; // 메뉴를 클릭할 때 활성화된 메뉴 업데이트
      },
      cancelReservation(id) {
        // 예매 취소 로직
        this.reservations = this.reservations.filter(reservation => reservation.id !== id);
        alert('예매가 취소되었습니다.');
        if (this.currentPage > this.totalPages) {
          this.currentPage = this.totalPages;
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
    }
  }
  </script>
  
  <style>
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
    bottom: -5px; /* 밑줄이 텍스트 아래에 표시되도록 간격을 조정 */
    left: 0;
    width: 100%;  /* 밑줄을 전체 div 너비에 맞춤 */
    height: 3px;
    background-color: black;
    transform: scaleX(0); /* 기본적으로 밑줄을 숨김 */
    transition: transform 0.3s ease;
  }
  
  .active-menu-item {
    color: black;  /* 선택된 메뉴의 텍스트 색상을 black으로 설정 */
  }
  
  .active-menu-item::after {
    transform: scaleX(1);  /* 활성화된 메뉴에만 밑줄 표시 */
  }
  
  .menu-item:hover::after {
    transform: scaleX(1);  /* 마우스를 올리면 밑줄 표시 */
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
    width: 80%;
    max-width: 800px;
    border-collapse: collapse;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    margin: 0 auto;
  }

  .reservation-table th, .reservation-table td {
    border: 1px solid #ccc;
    padding: 15px;
    text-align: center;
  }

  .reservation-table th {
    background-color: #f5f5f5;
    font-weight: bold;
  }

  .cancel-button {
    background-color: #d9534f;
    color: white;
    border: none;
    padding: 10px 20px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .cancel-button:hover {
    background-color: #c9302c;
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
    padding: 10px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .pagination-controls button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }

  .pagination-controls span {
    font-size: 16px;
    font-weight: bold;
  }
  </style>