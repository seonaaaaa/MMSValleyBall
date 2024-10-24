<template>
  <div class="ticket-purchase-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />

    <!-- 티켓 안내, 티켓 구매 메뉴 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/ticket/info' }"
        @click="navigateTo('/ticket/info')">티켓 안내</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/ticket/purchase' }"
        @click="navigateTo('/ticket/purchase')">티켓 구매</div>
    </div>

    <!-- 티켓 안내 페이지 내용 -->
    <div class="ticket-purchase-content">

      <div class="info-box">
        <div class="icon">
          <img src="@/assets/img/anyImg/bell-icon.png" alt="alert" />
        </div>
        <div class="info-content">
          <h4>예매 전 반드시 확인해 주세요!</h4>
          <ul>
            <li> - 경기 일반예매는 경기일 7일 전 13시에 오픈됩니다. (※GS MMS: 경기일 7일 전 13시)</li>
            <li> - 경기 선예매는 혜택이 포함된 멤버십 상품 구매 회원에 한해 제공되며, 해당 계정에 자동으로 선예매 권한이 부여되어 선예매 기간 내 예매 페이지 접속이 가능합니다.</li>
            <li> - PC에서 티켓 예매가 가능합니다.</li>
          </ul>
          <p>※ 반드시 [TICKET] - [티켓안내] 내 안내사항을 확인하신 후 예매를 진행하시기 바랍니다.
          </p>
        </div>
      </div>

      <!-- 티켓 예약표  -->
      <div>
        <table class="ticket-purchase">
          <thead>
            <tr>
              <th>경기 일시</th>
              <th>경기 정보</th>
              <th>경기장</th>
              <th>예매 일정</th>
              <th>티켓 예매</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                2024.10.24(목)<br>
                19:00
              </td>
              <td>GS MMS VS 한국전력</td>
              <td>서울 하이체육관</td>
              <td>
                [멤버쉽 선예매]<br>
                2024.10.14(월)<br>
                13:00<br>
                <br>
                [일반 예매]<br>
                2024.10.17(목)<br>
                13:00
              </td>
              <!-- 티켓 예매 버튼 클릭 시 모달 열기 -->
              <td>
                <button class="ticket-Modal" @click="openModal">예매하기</button>

                <!-- 모달 컴포넌트 -->
                <Modal :visible="isModalVisible" @close="closeModal" />
                
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import LogoHeader from '../common/LogoHeader.vue';
import Modal from './TicketModal.vue';

export default {
  name: 'TicketPurchase',
  components: {
    LogoHeader,
    Modal,
  },
  data() {
    return {
      activeMenu: this.$route.path, // 현재 활성화된 경로
      isModalVisible: false // 모달 표시 여부
    };
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

    // 모달 열기 닫기
    openModal() {
      this.isModalVisible = true;
    },
    closeModal() {
      this.isModalVisible = false;
    },
  }
}
</script>


<style>
.ticket-purchase {
  width: 62%;
  /* 표 전체 너비 설정 */
  border-collapse: collapse;
  /* 경계선 합치기 */
  font-size: 20px;
  /* 폰트 크기 설정 */
  background-color: #f9f9f9;
  /* 표의 배경색 설정 */
  justify-content: center;
  /* 가로 방향 중앙 정렬 */
  align-items: center;
  /* 세로 방향 중앙 정렬 */
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 40px;
}

th,
td {
  border: 1px solid #ddd;
  /* 셀의 경계선 설정 */
  text-align: center;
  /* 텍스트 가운데 정렬 */
  padding: 12px 5px;
  /* 위, 아래는 12px / 좌, 우는 5px */
}

th {
  background-color: #f2f2f2;
  /* 머리글의 배경색 설정 */
  color: #333;
  /* 머리글 텍스트 색상 */
  font-weight: bold;
  /* 머리글 텍스트 굵게 */
}


.ticket-Modal {
  background-color: #4CAF50;
  /* 버튼 배경색 (녹색) */

  border: 1px;
  border-color: #000000;

  color: white;
  /* 글자 색 (흰색) */
  padding: 12px 24px;
  /* 버튼 내부 여백 (위아래 12px, 좌우 24px) */
  text-align: center;
  /* 글자 가운데 정렬 */
  text-decoration: none;
  /* 밑줄 없음 */
  display: inline-block;
  /* 기본 인라인 블록 설정 */
  font-size: 20px;
  /* 글자 크기 */
  border-radius: 8px;
  /* 모서리를 둥글게 */
  cursor: pointer;
  /* 마우스를 올렸을 때 포인터 모양 */
  transition: background-color 0.3s;
  /* 배경색이 바뀌는 효과 추가 */
  margin: 20px;
  width: 150px;
  /* 버튼의 너비 */
  height: 80px;
  /* 버튼의 높이 */
}

.ticket-buy:hover {
  background-color: #45a049;
  /* 마우스를 올렸을 때 배경색 변경 */
}


.info-box {
  display: flex;
  /* 아이콘과 텍스트를 나란히 배치 */
  align-items: center;
  /* 아이템들을 수직으로 가운데 정렬 */
  background-color: #f7f7f7;
  /* 배경색 설정 */
  border: 1px solid #ccc;
  /* 경계선 설정 */
  border-radius: 10px;
  /* 모서리를 둥글게 설정 */
  padding: 20px;
  /* 내부 여백 */
  margin-left: 300px;
  margin-right: 300px;
  margin-bottom: 30px;

}

.icon {
  margin-right: 20px;
  /* 아이콘과 텍스트 사이의 간격 */
}

.icon img {
  width: 120px;
  /* 아이콘 크기 */
  height: 120px;
  /* 아이콘 크기 */
}

.info-content {
  text-align: left;
  /* 글자 왼쪽 정렬 */
  color: #000000;
  /* 글자 색상 */
  font-size: 16px;
  /* 일반 문단 글자 크기 */
  list-style-type: disc;
  /* 목록 스타일 */
  padding-left: 20px;
  /* 목록 왼쪽 여백 */
}

.info-content p {
  font-size: 13px;
  /* 일반 문단 글자 크기 */
  color: #1a1a1a;
  /* 부드러운 색상 */
  margin-top: 10px;
  /* 위쪽 여백 */
}

.ticket-purchase-page {
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
  /* 밑줄이 텍스트 아래에 표시되도록 간격을 조정 */
  left: 0;
  width: 100%;
  /* 밑줄을 전체 div 너비에 맞춤 */
  height: 3px;
  background-color: black;
  transform: scaleX(0);
  /* 기본적으로 밑줄을 숨김 */
  transition: transform 0.3s ease;
}

.active-menu-item {
  color: black;
  /* 선택된 메뉴의 텍스트 색상을 black으로 설정 */
}

.active-menu-item::after {
  transform: scaleX(1);
  /* 활성화된 메뉴에만 밑줄 표시 */
}

.menu-item:hover::after {
  transform: scaleX(1);
  /* 마우스를 올리면 밑줄 표시 */
}

/* 티켓 안내 */
</style>