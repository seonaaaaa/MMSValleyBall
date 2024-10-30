<template>
  <div class="content">
    <!-- 유저 정보 박스 -->
     <div class="control-box">
      <div class="user-info-box">
      <div v-if="user.isLoggedIn">
        <div v-if="user.role == 'USER'"> 
          <span class="membership-image-container">
        <span v-if="membershipLevel == 'GOLD'">
          <img :src="goldImage" alt="골드 등급" class="membershipLevel-image" />
        </span>
         <span v-else-if="membershipLevel == 'SILVER'">
           <img :src="silverImage" alt="실버 등급" class="membershipLevel-image" />
        </span>
        <span v-else>
          <img :src="bronzeImage" alt="브론즈 등급" class="membershipLevel-image" />
        </span>
        <p><strong>{{ user.name }}</strong> 님</p>
        </span>

        <div class="money-box">
          <p>잔액: <strong>{{ balance }}</strong>원</p><button class="btn-charge" @click="goToRecharge">충전</button>
        </div>
          <button class="btn-myPage" @click="goToMyPage">My Page</button>&nbsp;<button class="btn-logout" @click="logout">로그아웃</button>
        </div>


        <div v-else> 
          <p class="Admin-notice">****관리자 모드입니다****</p>
          <p><strong>{{ user.name }}</strong> 님</p>
          <button class="btn-AdminPage" @click="goToAdminPage">ADMIN Page</button><br>
          <button class="btn-logout2" @click="logout">로그아웃</button>
        </div>
        </div>
      
      <div v-else>
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
        <CalendarMain :events="events" />
      </div>

    </div>
  </div>
</template>

<script>
import axios from 'axios';
import CalendarMain from '../common/CalendarMain.vue';

export default {
  name: 'AppContent',
  components: {
    CalendarMain,
  },
  props: {
    user: {
      type: Object,
      default: () => ({ name: '', role: 'guest', email: '', isLoggedIn: false })
    }
  },
  async mounted(){
    this.fetchEvents(); // 컴포넌트가 로드될 때 데이터를 가져옴
  },

  data() {
    return {
      // 유저 정보 저장할 곳
      balance: 0,
      membershipLevel: 'GOLD',
      // 골드 등급 이미지 경로
      goldImage: require('@/assets/img/membershipImg/gold.png'),
      // 실버 등급 이미지 경로
      silverImage: require('@/assets/img/membershipImg/silver.png'),
      // 브론즈 등급 이미지 경로
      bronzeImage: require('@/assets/img/membershipImg/bronze.png'),

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

    goToRecharge(){
      this.$router.push('/myPage/info/recharge');
    },

    goToLogin(){
      this.$router.push('/login');
    },

    goToSignup(){
      this.$router.push('/signup');
    },

    goToAdminPage(){
      this.$router.push('admin/user-list');
    },

    // 로그아웃
    logout() {
    // localStorage에서 토큰 삭제
    localStorage.removeItem('accessToken');

    // 삭제 여부 확인을 위한 로그 출력
    const token = localStorage.getItem('accessToken');
    if (token === null) {
      console.log('토큰이 성공적으로 삭제되었습니다.');
      alert("로그아웃이 되었습니다.");
    } else {
      console.log('토큰 삭제에 실패했습니다.', token);
    }

    // 메인 페이지로 이동
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
        // 데이터 확인
        // Vue 개발 모드에서 컴포넌트 초기화를 두 번 함 -> 초기 메인 페이지 띄울 때 console.log 2번 뜨니 참고
        // Vue 배포 모드에서는 자동으로 한 번만 렌더링하도록 동작하므로 별도의 설정 넣지 않음
        // console.log(response.data); 
        this.events = response.data; // 응답 데이터 설정

        // 이메일 정보 보내서 유저정보 받아오기
        console.log(this.user);
        const userData = await axios.post('/main', null, {
          params: {
              email: this.user.email
          },
          headers: {
              "Content-Type": "application/json"
          }
        })
        this.balance = userData.data.balance;
        this.membershipLevel = userData.data.membership;
      } catch (error) {
        console.error("Error fetching events:", error);
      }
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
  top: 595px;
  right: 22px;
  background-color: #f8f9fa;
  width: 300px;
  height: 300px;
  padding: 0px 15px;
  border: 2px solid #60a191;
  border-radius: 20px;
  text-align: right;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: center;
  z-index: 100;
}

.btn-myPage, .btn-logout {
  background-color: #60a191;
  color: white;
  border: none;
  padding: 10px;
  margin-top: 20px;
  border-radius: 5px;
  cursor: pointer;
  width: 48%;
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
  margin-left: 10px;
  display: flex;
  align-items: center; /* 수직 정렬 */
  gap: 1px; /* 간격 조정 */
  width: 250px;
  height: 80px;
  border: solid color(srgb rgb(68, 68, 68) green blue);
  border-radius: 8px;
  background: #c7dfd9;
  text-align: left;
}

.money-box p {
  margin-left: 15px; /* 기본 여백 제거 */
}

.btn-charge {
  margin-left: 25px; /* 오른쪽으로 버튼 밀기 */
  width: 80px;
  background-color: #f0efc3;
  border: solid color(srgb rgb(224, 224, 224) green blue);
}

/* 멤버십 로고 */
.membership-image-container {
  display: flex;
  align-items: center; /* 이미지와 텍스트를 수직 중앙 정렬 */
  gap: 2px; /* 이미지와 텍스트 간격 조절 */
}

.membershipLevel-image {
  width: 50px;
  /* 아이콘 크기 */
  height: 50px;
  /* 아이콘 크기 */
  margin-left: 10px;
}

/* 로그아웃시 버튼 */
.btn-login, .btn-signup{
  background-color: #60a191;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
  height: 50px;
  margin-top: 25px;
}

/* 관리자 박스 */
.btn-AdminPage, .btn-logout2{
  background-color: #60a191;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
  height: 45px;
  margin-top: 20px;
}

.Admin-notice{
  color: #504f4f;
  font-size: 18px;
  text-align: center;
  padding-bottom: 20px;
}


/* 상단 슬라이드 배너 */
.slider-container {
  position: relative;
  width: 100%;
  height: 400px;
  margin: 0 auto;
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
  width: 1200px;
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
</style>