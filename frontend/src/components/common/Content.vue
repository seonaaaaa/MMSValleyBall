<template>
  <div class="content">
    <!-- 유저 정보 박스 -->
    <div class="control-box">
      <div class="user-info-box">
        <div v-if="isLoggedIn">
          <!-- 관리자 -->
          <div v-if="role == 'ADMIN'" class="admin"> 
            <div class="image-container">
              <img class="admin-icon" :src="require('@/assets/img/anyImg/admin.png')" alt="관리자 아이콘" />
              <h2 class="admin-header">관리자<span class="admin-name"> {{ name }} </span> 님</h2>
            </div>
            <button class="btn-AdminPage" @click="goToAdminPage">관리자 모드</button><br>
            <button class="btn-logout2" @click="logout">로그아웃</button>
          </div>
          <!-- 사용자 -->
          <div v-if="role == 'USER'"> 
            <span class="image-container">
              <img class="membershipLevel-image" :src="membershipImage()" alt="멤버십 등급 이미지" />
              <p><strong>{{ name }}</strong> 님</p>
            </span>
            <div class="money-box">
              <p>잔액: <strong>{{ new Intl.NumberFormat('ko-KR').format(balance) }}</strong>원</p><button class="btn-charge-main" @click="openRechargeWindow">충전하기</button>
            </div>
            <button class="btn-myPage" @click="goToMyPage">My Page</button>&nbsp;<button class="btn-logout" @click="logout">로그아웃</button>
          </div>
        </div>
        <div v-if="!isLoggedIn" class="login-signup-box">
          <h2 class="welcome">WELCOME MMS</h2>
          <button @click="goToLogin" class="btn-login">로그인</button><br>
          <button @click="goToSignup" class="btn-signup">회원가입</button>
        </div>
      </div>
    </div>
    <!-- 본문 내용 -->
    <div class="main-content">
      <!-- 상단 슬라이드 배너 -->
      <div class="slider-container">
        <div class="slides" :style="{ transform: `translateX(-${currentSlide * 100}%)` }">
          <div v-for="(slide, index) in slides" :key="index" class="slide">
            <img :src="slide.img" :alt="'Slide ' + (index + 1)" /> <!-- 셀프 클로징 -->
          </div>
        </div>
        <button class="prev" @click="prevSlide">
          <img src="@/assets/img/common/content-slide-icon-left-arrow.png" alt="Previous Slide" />
        </button>
        <button class="next" @click="nextSlide">
          <img src="@/assets/img/common/content-slide-icon-right-arrow.png" alt="Next Slide" />
        </button>
        <div class="indicators">
          <img v-for="(slide, index) in slides" 
            :key="index" 
            :src="currentSlide === index ? require('@/assets/img/common/content-slide-icon-filled-circle.png') : require('@/assets/img/common/content-slide-icon-empty-circle.png')"
            @click="goToSlide(index)"
            alt="Slide Indicator"
            class="indicator-img" />
        </div>
      </div>

      <!-- 경기 하이라이트 섹션 -->
      <div class="section">
        <h2>경기 하이라이트</h2>
        <div class="highlight-slider-container">
          <div class="highlight-slides" :style="{ transform: `translateX(-${currentHighlightSlide * 100}%)` }">
            <div v-for="(slide, index) in highlightSlides" :key="index" class="highlight-slide">
              <img :src="slide.img" :alt="'Highlight Slide ' + (index + 1)" />
              <img class="highlight-play-button" src="@/assets/img/common/content-slide-icon-play-button.png" alt="Play Button" />
              <div class="highlight-overlay"></div>
            </div>
          </div>
          <button class="highlight-prev" @click="prevHighlightSlide">
            <img src="@/assets/img/common/content-slide-icon-left-arrow.png" alt="Previous Slide" />
          </button>
          <button class="highlight-next" @click="nextHighlightSlide">
            <img src="@/assets/img/common/content-slide-icon-right-arrow.png" alt="Next Slide" />
          </button>
          <div class="highlight-indicators">
            <img v-for="(slide, index) in highlightSlides" 
              :key="index" 
              :src="currentHighlightSlide === index ? require('@/assets/img/common/content-slide-icon-filled-circle.png') : require('@/assets/img/common/content-slide-icon-empty-circle.png')"
              @click="goToHighlightSlide(index)"
              alt="Slide Indicator"
              class="highlight-indicator-img" />
          </div>
        </div>
      </div>

      <!-- 경기 일정 섹션 -->
      <div class="section">
        <h2>경기 일정</h2>
        <!-- Calendar 컴포넌트를 사용 -->
        <CalendarMain :events="events" calendarMode="main" />
      </div>

    </div>
  </div>
</template>

<script>
import axios from 'axios';
import CalendarMain from '../common/Calendar.vue';

export default {
  name: 'AppContent',
  components: {
    CalendarMain,
  },
  props:{
    isLoggedIn: {
      type: Boolean,
      required: true
    },
    balance: {
      type: Number,
      required: true
    },
    membership: {
      type: String,
      required: true
    },
  },
  async mounted(){
    const token = sessionStorage.getItem('token');
    if(token!=null){
      this.role = sessionStorage.getItem('role');
      this.name = sessionStorage.getItem('name');
      try{
        const userData = await axios.post('/main', null, {
          params: {
              email: sessionStorage.getItem('email')
          }
        })
        this.$emit("getBalance", userData.data.balance);
        this.$emit("getMembership", userData.data.membership);
      }catch(error){
        console.error("email: " + sessionStorage.getItem('email'), error);
      }
    }
    await this.fetchEvents();
  },
  data() {
    return {
      role: 'guest',
      name: null,
      // 상단 슬라이드 배너
      currentSlide: 0, // 현재 보여지는 슬라이드의 인덱스
      slides: [
        { img: require('@/assets/img/common/content-top-slide-001.png') },
        { img: require('@/assets/img/common/content-top-slide-002.png') },
        { img: require('@/assets/img/common/content-top-slide-004.png') }
      ],
      // 경기 하이라이트 슬라이드 배너
      currentHighlightSlide: 0, // 현재 보여지는 경기 하이라이트 슬라이드의 인덱스
      highlightSlides: [
        { img: require('@/assets/img/common/content-highlight-slide-009.png') },
        { img: require('@/assets/img/common/content-highlight-slide-006.png') },
        { img: require('@/assets/img/common/content-highlight-slide-002.png') }
      ],
      // CalendarMain에 전달할 경기 일정 데이터
      events: [],
    };
  },
  methods: {
    // 페이지 이동 버튼
    goToMyPage() {
      this.$router.push('/mypage/reservations');
    },
    goToLogin(){
      this.$router.push('/login');
    },
    goToSignup(){
      this.$router.push('/signup');
    },
    goToAdminPage(){
      const targetUrl = `http://localhost:4000/admin/userList?adminName=${encodeURIComponent(this.name)}`;
      window.location.href = targetUrl;
    },
    // 로그아웃
    logout() {
      // 로컬스토리지에 저장된 토큰과 사용자 정보 삭제
      sessionStorage.removeItem('token');
      sessionStorage.removeItem('name');
      sessionStorage.removeItem('email');
      sessionStorage.removeItem('role');
      sessionStorage.removeItem('address');
      sessionStorage.removeItem('phone');
      const token = sessionStorage.getItem('token'); 
      if (token === null) {
        console.log('토큰이 성공적으로 삭제되었습니다.');
        this.$emit('logoutSuccess');
        alert("로그아웃이 되었습니다.");
      } else {
        console.log('Content에서 토큰 삭제에 실패했습니다.', token);
      }
      this.$router.push('/');
    },

    // 상단 슬라이드 배너
    // 왼쪽 슬라이드로 이동
    prevSlide() {
    if (this.currentSlide === 0) {
      this.currentSlide = this.slides.length - 1; // 첫 슬라이드에서 마지막 슬라이드로 순환
    } else {
      this.currentSlide--;
    }
    },
    // 오른쪽 슬라이드로 이동
    nextSlide() {
      if (this.currentSlide === this.slides.length - 1) {
        this.currentSlide = 0; // 마지막 슬라이드에서 첫 슬라이드로 순환
      } else {
        this.currentSlide++;
      }
    },
    // 특정 슬라이드로 이동
    goToSlide(index) {
      this.currentSlide = index;
    },

    // 경기 하이라이트 슬라이드 배너
    prevHighlightSlide() {
      if (this.currentHighlightSlide === 0) {
        this.currentHighlightSlide = this.highlightSlides.length - 1;
      } else {
        this.currentHighlightSlide--;
      }
    },
    nextHighlightSlide() {
      if (this.currentHighlightSlide === this.highlightSlides.length - 1) {
        this.currentHighlightSlide = 0;
      } else {
        this.currentHighlightSlide++;
      }
    },
    goToHighlightSlide(index) {
      this.currentHighlightSlide = index;
    },
    async fetchEvents() {
      try {
        const response = await axios.get('/game/schedule/main');
        this.events = response.data; 
      } catch (error) {
        console.error("Error fetching events:", error);
      }
    },
    membershipImage(){
      return  require(`@/assets/img/membershipImg/${this.membership}.png`);
    },
    openRechargeWindow() {
      const width = 570;
      const height = 275;
      const left = (window.screen.width / 2) - (width / 2); // 화면 중앙에 위치
      const top = (window.screen.height / 2) - (height / 2);
      window.open(`/myPage/recharge`, '충전하기', 
      `width=${width},height=${height},,top=${top},left=${left},
      toolbar=no,menubar=no,scrollbars=no,resizable=no,fullscreen=no`);
    },
  },
};
</script>

<style scoped>
.content {
  flex-grow: 1; /* 콘텐츠가 화면의 나머지 공간을 차지하도록 설정 */
  padding-top: var(--header-height);  /* Header 높이만큼 패딩 */
  padding-bottom: var(--footer-height);  /* Footer 높이만큼 패딩 */
  /* max-width: 1200px; */
  /* margin: 30px auto; */
  /* 상하 마진 제거, 하단에만 마진 30px */
  margin: 0 auto;
  margin-bottom: 100px;
  position: relative;
}

a {
  color: #393939;
}

/* 유저 정보 박스 */
.user-info-box {
  position: fixed;
  top: 200px;
  right: 50px;
  background-color: #e6ebe5dc;
  width: 300px;
  height: 250px;
  padding: 0px 15px;
  border: 2px solid #bfccbdde;
  border-radius: 20px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: center;
  z-index: 100;
  text-align: center;
  justify-content: center;
}

.btn-myPage, .btn-logout {
  background-color: #60a191a3;
  color: white;
  border: none;
  padding: 10px;
  margin-top: 20px;
  border-radius: 5px;
  cursor: pointer;
  width: 48%;
}
.btn-myPage{
  margin-right: 5px;
}

.user-info-box button:hover {
  background-color: #4f8578;
}

.user-info-box p {
  margin: 5px 20px;
}

.user-info-box a:hover {
  text-decoration: underline;
}

/* 금액충전 창 */
.money-box {
  margin-top: 15px;
  display: flex;
  align-items: center; /* 수직 정렬 */
  justify-content: center;
  gap: 1px; /* 간격 조정 */
  width: 99%;
  height: 70px;
  border: solid color(srgb rgb(68, 68, 68) green blue);
  border-radius: 8px;
  background-color: #d2e5d0de;
  text-align: left;
}

.money-box p {
  margin-left: 15px;
  font-size: large;
}

.btn-charge-main {
  width: 80px;
  height: 45px;
  background-color: #adcf69d2;
  border: none;
  color: white;
  border-radius: 10px;
}
.btn-charge-main:hover{
  background-color: #92c06c;
}

/* 멤버십 로고 */
.image-container {
  display: flex;
  align-items: center; /* 이미지와 텍스트를 수직 중앙 정렬 */
  gap: 2px; /* 이미지와 텍스트 간격 조절 */
  font-size: 25px;
}

.membershipLevel-image {
  width: 50px;
  height: 50px;
  margin-left: 10px;
}

.welcome{
  color: #3c6259;
}
.btn-login, .btn-signup,
.btn-logout2,.btn-AdminPage{
  background-color: #60a191a3;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 90%;
  height: 60px;
  margin-bottom: 10px;
  font-size: large;
  margin-top: 10px;
}
.admin-header{
  font-size: large;
  color: #3c6259;
  margin-left: 10px;
}
.admin-icon {
  width: 50px;
  height: 50px;
  margin-left: 25px;
}
.admin-name{
  margin-left: 10px;
  font-size: larger;
  color: #223631;
}

.Admin-notice{
  color: #504f4f;
  font-size: 18px;
  text-align: center;
}
/* 상단 슬라이드 배너 */
.slider-container {
  position: relative;
  width: 100%;
  height: 400px;
  margin: 0;
  overflow: hidden;
}

.slides {
  display: flex;
  transition: transform 0.5s ease-in-out;
}

.slide {
  position: relative;
  min-width: 100%;
  height: 400px;
}

.slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.prev img,
.next img {
  width: 30px;
  height: 30px;
}

.prev, .next {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: transparent;
  border: none;
  padding: 10px;
  cursor: pointer;
}

.prev {
  left: 10px;
}

.next {
  right: 10px;
}

.indicators {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
}

.indicator-img {
  width: 10px;
  height: 10px;
  margin: 0 5px;
  cursor: pointer;
}

/* 섹션 */
.section {
  margin-top: 50px;
  text-align: center;
}

/* 경기 하이라이트 슬라이드 배너 */
.highlight-slider-container {
  position: relative;
  max-width: 1200px;
  height: auto;
  margin: 0 auto;
  overflow: hidden;
  border-radius: 15px;
}

.highlight-slides {
  display: flex;
  transition: transform 0.5s ease-in-out;
}

.highlight-slide {
  position: relative;
  min-width: 100%;
  height: auto;
}

.highlight-slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 15px;
}

/* 이미지 위에 어두운 오버레이 */
.highlight-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); 
}

/* 재생 버튼 */
.highlight-play-button {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50px !important;
  height: 50px !important;
  z-index: 10;
  cursor: pointer;
}

.highlight-prev img,
.highlight-next img {
  width: 30px;
  height: 30px;
}

.highlight-prev, .highlight-next {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: transparent;
  border: none;
  padding: 10px;
  cursor: pointer;
}

.highlight-prev {
  left: 10px;
}

.highlight-next {
  right: 10px;
}

.highlight-indicators {
  position: absolute;
  bottom: 10px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
}

.highlight-indicator-img {
  width: 10px;
  height: 10px;
  margin: 0 5px;
  cursor: pointer;
}
.admin-url {
  text-decoration: none;
  color: inherit; /* 원래 텍스트 색상 유지 */
}

.admin-url:hover {
  text-decoration: none; /* hover 시 밑줄 없음 */
  color: inherit; /* hover 시 색상 변화 없음 */
}
</style>