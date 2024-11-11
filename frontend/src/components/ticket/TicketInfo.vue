<template>
  <div class="ticket-info-page">
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
    <div class="ticket-info-content">
      <div>
        <h2>🏐 좌석 안내 🏐</h2>
      </div>

      <!-- 티켓이미지 섹션 -->
      <div class="section ticket">
        <img src="@/assets/img/stadium/stadium-main.jpg" alt="stadium-img" class="stadium-image" />
      </div>
      <hr class="divider" />

      <!-- 티켓 안내 페이지 내용2  -->
      <div>
        <h2>MMS 2024-2025시즌 V-리그 홈경기 권종 및 가격</h2>
      </div>

      <!-- 티켓 안내 가격 표  -->
      <div>
        <table class="ticket-info-price">
          <thead>
            <tr>
              <th>구분</th>
              <th>층</th>
              <th>좌석 위치</th>
              <th>총 좌석 수</th>
              <th>입장권 가격</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>골드 [GOLD ZONE]</td>
              <td :rowspan="3">1층</td>
              <td>GA ~ GC</td>
              <td>90석</td>
              <td>21,000원 (골드 멤버쉽 할인 30% 적용)</td>
            </tr>
            <tr>
              <td>그레이(원정석) [AWAY ZONE]</td>
              <td>AA ~ AC</td>
              <td>90석</td>
              <td>30,000원</td>
            </tr>
            <tr>
              <td>블루 [BLUE ZONE]</td>
              <td>BA ~ BF</td>
              <td>180석</td>
              <td>20,000원</td>
            </tr>
            <tr>
              <td>그린 [GREEN ZONE]</td>
              <td :rowspan="2">2층</td>
              <td>NC ~ SC</td>
              <td>900석</td>
              <td>10,000원</td>
            </tr>
            <tr>
              <td>브라운(원정석) [BROWN ZONE]</td>
              <td>EA ~ EC</td>
              <td>300석</td>
              <td>10,000원</td>
            </tr>
          </tbody>
        </table>
      </div>
      <hr class="divider" />


      <!-- 티켓 환불과 기타 규정 설명 -->
      <div>
        <h2>🏐 안내 및 주의사항 🏐</h2>
      </div>

      <div class="rule-box">
        <div>
          <h3>※ 티켓 예매 유의사항</h3>

          <div>
            - 1인 최대 4매까지 구매 가능<br>
            - 예매 오픈시간 : 경기 7일전 13시 시작<br>
            <div class="sub-text">
              * 멤버쉽 선예매는 경기 10일전 13시 시작<br>
            </div>
            - 예매 가능시간 : 경기당일 경기시작 시간 1시간 전 마감<br>
            - 현장 매표소 운영시간 : 경기 시작 2시간 전 시작 (변경 시 별도 공지)<br>
            <div class="sub-text">
              * 경기 당일 매표소에서 현장 예매 가능
            </div>
          </div>

          <div>
            <h3>※ 경기 당일 안내사항</h3>
            <div>
              - 경기당일 매표소가 혼잡하오니, 모바일티켓을 활용하여 편안한 입장을 권장드립니다.<br>
              - 모바일 티켓은 바로 출입구에서 입장이 가능합니다.<br>
              - 모바일 티켓 캡쳐 혹은 이미지는 입장 불가 (어플에서 예약번호 혹은 바코드가 실시간으로 움직이는 티켓만 입장 가능)<br>
            </div>

            <div>
              <h3>※ 티켓 환불 규정</h3>
              <div>
                - 경기 시작 12시간 이내 취소 불가<br>
                - 경기 시작 24시간 이내 취소 50% 환불<br>
                - 경기 시작 48시간 이내 취소 70% 환불<br>
                - 경기 시작 72시간 이내 취소 100% 환불<br>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div>
        <button class="ticket-button"
          onclick="window.location.href='/ticket/purchase';">예매하기</button>
      </div>
    </div>
  </div>
</template>

<script>
import LogoHeader from '../common/LogoHeader.vue';

export default {
  name: 'TicketInfo',
  components: {
    LogoHeader
  },
  data() {
    return {
      activeMenu: this.$route.path // 현재 활성화된 경로
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
    }
  }
}
</script>

<style scoped>
.ticket-button {
  background-color: #60a191;
  /* 버튼 배경색 (녹색) */
  border: none;
  /* 테두리 없음 */
  color: white;
  /* 글자 색 (흰색) */
  padding: 8px 16px;
  /* 버튼 내부 여백 (위아래 12px, 좌우 24px) */
  text-align: center;
  /* 글자 가운데 정렬 */
  text-decoration: none;
  /* 밑줄 없음 */
  display: inline-block;
  /* 기본 인라인 블록 설정 */
  font-size: 22px;
  /* 글자 크기 */
  border-radius: 8px;
  /* 모서리를 둥글게 */
  cursor: pointer;
  /* 마우스를 올렸을 때 포인터 모양 */
  transition: background-color 0.3s;
  /* 배경색이 바뀌는 효과 추가 */
  margin-top: 50px;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 50px;

  width: 140px;
  /* 버튼의 너비 */
  height: 50px;
  /* 버튼의 높이 */
}

.ticket-button:hover {
  background-color: #4d7e74;
  /* 마우스를 올렸을 때 배경색 변경 */
}

.sub-text {
  font-size: 16px;
  /* 글자 크기를 기본보다 작게 설정 */
  margin-left: 20px;
  /* 왼쪽에 여백 추가 */
  color: #666;
  /* 글자 색상도 더 부드럽게 설정 (선택 사항) */
}

.rule-box {
  background-color: #eef1ee;

  border-radius: 15px;
  /* 모서리 둥글게 */
  max-width: 1000px;
  /* 가로 길이 */
  height: auto;
  /* 세로 길이 */
  border: 2px solid #60a191;
  /* 경계선 설정 (2px 두께, 검정색) */
  display: flex;
  /* 중앙 정렬을 위한 flexbox 사용 */
  align-items: center;
  /* 세로 중앙 정렬 */
  justify-content: center;
  /* 가로 중앙 정렬 */
  font-size: 18px;
  /* 글자 크기 */
  text-align: left;
  /* 글자 왼쪽 정렬 */
  margin-left: auto;
  margin-right: auto;
  padding: 40px;
}

.ticket-info-price {
  width: 100%;
  /* 표 전체 너비 설정 */
  max-width: 1000px;
  border-collapse: collapse;
  /* 경계선 합치기 */
  font-size: 18px;
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

.stadium-image {
  display: flex;
  max-width: 1000px;
  justify-content: center;
  align-items: center;
  margin-bottom: 40px;
  margin-left: auto;
  margin-right: auto;
}

.ticket-info-page {
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

h2 {
  margin-bottom: 30px;
}

.divider{
  width: 75%;
}
/* 티켓 안내 */
</style>