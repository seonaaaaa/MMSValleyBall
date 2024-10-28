<template>
    <div class="reservations-view-page">
      <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
      <LogoHeader />
  
      <!-- 예매 내역, 나의 멤버십, 개인정보 수정 -->
      <div class="menu">
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/reservations' }" @click="navigateTo('/mypage/reservations')">예매 내역</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/membership' }" @click="navigateTo('/mypage/membership')">나의 멤버십</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/edit-profile' }" @click="navigateTo('/mypage/edit-profile')">나의 정보 수정</div>
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
}
</script>

<style>

</style>