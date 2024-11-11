<template>
  <div class="ticket-purchase-page">
    <LogoHeader />
    <div class="ticket-purchase-page-detail">
      <!-- 티켓 안내, 티켓 구매 메뉴 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/ticket/info' }"
        @click="navigateTo('/ticket/info')">티켓 안내</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/ticket/purchase' }"
        @click="navigateTo('/ticket/purchase')">티켓 구매</div>
    </div>

    <!-- 티켓 안내 페이지 내용 -->
    <div class="ticket-purchase-content">
      <div class="ticket-purchase-layout">
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
              <li> - 1인 최대 4매 예매 가능합니다.</li>
            </ul>
            <p>&nbsp; ※ 반드시 [TICKET] - [티켓안내] 내 안내사항을 확인하신 후 예매를 진행하시기 바랍니다.
            </p>
          </div>
        </div>

        <!-- 티켓 예약표  -->
        <div class="table-container">
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
              <tr v-for="match in paginatedMatches" :key="match.matchId">
                <td v-html="formatDate(match.matchDate)"></td>
                <td>{{ formatMatchInfo(match.matchTeam, thisTeam) }}</td>
                <td>{{ match.matchStadium }}</td>
                <td>
                  <p>[선예매]</p>
                  <p v-html="formatDatePreBook(match.matchDate)"></p>
                  <p>[일반예매]</p>
                  <p v-html="formatDateBook(match.matchDate)"></p>
                </td>
                <td>
                  <button class="ticket-Modal" @click="handleButtonClick(match)">
                    예매하기
                  </button>
                  <Modal v-if="modalStatus"
                    :receivedModalData="receivedModalData" :user="user"
                    @closeModal="closeModal" @getBalanceByModal="updateBalance"/>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
          <!-- 페이지네이션 추가 -->
          <div class="pagination">
            <button class="page-btn" @click="prevPage" :disabled="currentPage === 1">이전</button>
            <span>페이지 {{ currentPage }} / {{ totalPages }}</span>
            <button class="page-btn" @click="nextPage" :disabled="currentPage === totalPages">다음</button>
          </div>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import axios from 'axios';
import LogoHeader from '../common/LogoHeader.vue';
import Modal from './TicketModal.vue';

export default {
  name: 'TicketPurchase',
  components: {
    LogoHeader,
    Modal,
  },
  props: {
    receivedModalData: {
      type: Object,
      required: false,
    },
    user: {
      type: Object,
      required: true,
    },
    modalStatus:{
      type: Boolean,
      required: true,
    }
  },
  async mounted(){
    this.fetchEvents();
  },
  data() {
    return {
      activeMenu: this.$route.path, // 현재 활성화된 경로
      thisTeam : "GS ITM",
      isModalVisible: false, // 모달 표시 여부
      //서버에서 가져온 경기 정보 배열
      matches : [], 
      pageSize: 5, // 페이지당 행 수
      currentPage: 1 // 현재 페이지 번호
    };
  },
  watch: {
    // 경로가 변경될 때마다 activeMenu를 업데이트
    $route(to) {
      this.activeMenu = to.path;
    }
  },
  computed: {
    upcomingMatches(){
      const today = new Date();
      return this.matches.filter(match => new Date(match.matchDate) >= today);
    },
    totalPages() {
      return Math.ceil(this.upcomingMatches.length / this.pageSize);
    },
    paginatedMatches() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.upcomingMatches.slice(start, end);
    }
  },
  methods: {
    navigateTo(route) {
      this.$router.push(route);
      this.activeMenu = route; // 메뉴를 클릭할 때 활성화된 메뉴 업데이트
    },
    // 매치 데이터를 가져오는 메서드
    async fetchEvents() {
      try {
        this.matches = (await axios.get('/ticket/purchase')).data;
      } catch (error) {
        console.log("Error fetching matches:", error);
        // 에러 발생 시 matches를 빈 배열로 초기화
        this.matches = [];
      }
    },
    // 경기 정보 형식 지정 메서드
    formatMatchInfo(matchTeam, thisTeam) {
      return matchTeam === "서울하이체육관" ? `${thisTeam} VS ${matchTeam}` : `${matchTeam} VS ${thisTeam}`;
    },
    // 날짜 포맷팅 메서드
    formatDate(date) {
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        const dateObj = new Date(date);
        
        // 날짜 포맷
        let formattedDate = dateObj.toLocaleDateString('ko-KR', options);

        // 마지막 점 제거
        if (formattedDate.endsWith('.')) {
          formattedDate = formattedDate.slice(0, -1);
        }
        
        // 요일 가져오기
        const days = ['일', '월', '화', '수', '목', '금', '토'];
        const dayName = days[dateObj.getDay()];

        // 시간 포맷
        const formattedTime = dateObj.toLocaleTimeString('ko-KR', {
          hour: '2-digit',
          minute: '2-digit'
        });

        return `${formattedDate} (${dayName})<br>${formattedTime}`;
      },
      // 7일 전 오전 11시 날짜 포맷팅 메서드
      formatDatePreBook(date) {
        const dateObj = new Date(date);
        dateObj.setDate(dateObj.getDate() - 7); // 7일 전
        dateObj.setHours(11, 0, 0, 0); // 시간 설정: 11시 0분 0초
        
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        
        // 날짜 포맷
        let formattedDate = dateObj.toLocaleDateString('ko-KR', options);

        // 마지막 점 제거
        if (formattedDate.endsWith('.')) {
          formattedDate = formattedDate.slice(0, -1);
        }
        
        // 요일 가져오기
        const days = ['일', '월', '화', '수', '목', '금', '토'];
        const dayName = days[dateObj.getDay()];

        // 시간 포맷
        const formattedTime = dateObj.toLocaleTimeString('ko-KR', {
          hour: '2-digit',
          minute: '2-digit'
        });

        return `${formattedDate} (${dayName})<br>${formattedTime}`;
      },

      // 5일 전 오전 11시 날짜 포맷팅 메서드
      formatDateBook(date) {
        const dateObj = new Date(date);
        dateObj.setDate(dateObj.getDate() - 5); // 5일 전
        dateObj.setHours(11, 0, 0, 0); // 시간 설정: 11시 0분 0초

        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        
        // 날짜 포맷
        let formattedDate = dateObj.toLocaleDateString('ko-KR', options);

        // 마지막 점 제거
        if (formattedDate.endsWith('.')) {
          formattedDate = formattedDate.slice(0, -1);
        }
        
        // 요일 가져오기
        const days = ['일', '월', '화', '수', '목', '금', '토'];
        const dayName = days[dateObj.getDay()];

        // 시간 포맷
        const formattedTime = dateObj.toLocaleTimeString('ko-KR', {
          hour: '2-digit',
          minute: '2-digit'
        });

        return `${formattedDate} (${dayName})<br>${formattedTime}`;
      },
      // 이전 페이지로 이동
      prevPage() {
        if (this.currentPage > 1) {
          this.currentPage--;
        }
      },
      // 다음 페이지로 이동
      nextPage() {
        if (this.currentPage < this.totalPages) {
          this.currentPage++;
        }
      },
    openModal(matchId) {
      this.$emit("openMadal", matchId);
    },
    // 모달에서 온 이벤트 app.vue에 보내기
    updateBalance(balance){
      console.log("모달에서 온 잔액"+balance);
      this.$emit("getBalance", balance);
    },
    closeModal() {
      this.$emit("closeModal");
    },
    handleButtonClick(match) {
      if(sessionStorage.getItem('token')==null){
        alert('로그인 후 예매가능합니다.\n로그인 페이지로 이동합니다.')
        this.$router.push('/login');
      }
      const today = new Date();
      const matchDate = new Date(match.matchDate);
      const preBookStartDate = new Date(match.matchDate);
      preBookStartDate.setDate(preBookStartDate.getDate() - 7);
      preBookStartDate.setHours(11, 0, 0, 0);
      const generalBookStartDate = new Date(match.matchDate);
      generalBookStartDate.setDate(generalBookStartDate.getDate() - 5);
      generalBookStartDate.setHours(11, 0, 0, 0);
      const userMembership = sessionStorage.getItem('membership')
      if (generalBookStartDate <= today && today < matchDate) {
        // 오늘 날짜가 일반 예매 시작일과 경기일 사이
        this.openModal(match.matchId);
      } else if ( preBookStartDate < today && today < generalBookStartDate) {
        // 오늘 날짜가 일반 예매 시작일과 경기일 사이
        if (userMembership == 'bronze') {
          this.openModal(match.matchId);
        } else {
          alert('브론즈 회원님은 선예매가 불가능합니다.');
          return;
        }
      } else {
        // 오늘 날짜가 선예매 시작일 이후
        alert(`아직 예매 일정이 아닙니다. 예매 일정은 다음과 같습니다:\n${this.formatDatePreBook(match.matchDate).replace('<br>', '\n')}`);
        return;
      }
      return;
    },
  },
    
}
</script>

<style scoped>
.ticket-purchase-page-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  width: 100%;
}

/* 메뉴 중앙 정렬 설정 */
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

/* 티켓 안내 레이아웃 중앙 정렬 설정 */
.ticket-purchase-layout {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  max-width: 1400px;
}

/* 테이블 컨테이너 중앙 정렬 설정 */
.table-container {
  display: flex;
  justify-content: center;
  width: 100%;
}

.ticket-purchase {
  width: 100%;
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

th, td {
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

.ticket-purchase p {
  margin: 5px;
  text-align: left;
  margin-left: 20px;
}

.ticket-Modal {
  background-color: #60a191;
  /* 버튼 배경색 (녹색) */
  border: none;
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
  margin: 15px;
  width: 140px;
  /* 버튼의 너비 */
  height: 50px;
  /* 버튼의 높이 */
}

.ticket-buy:hover {
  background-color: #4d7e74;
  /* 마우스를 올렸을 때 배경색 변경 */
}

/* info-box 중앙 정렬 설정 */
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
  margin-bottom: 30px;
  width: 100%;
  max-width: 1000px; /* 최대 너비 설정으로 반응형 지원 */
  justify-content: center;
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
/* pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 10px 10px;
  margin-bottom: 50px;
  font-size: 20px;
}
.page-btn {
  padding: 10px 15px;
  margin: 10px 15px;
  border: none;
  font-size: 20px;

}
</style>
