<template>
  <div class="content">
    <!-- 유저 정보 박스 -->
    <div class="user-info-box">
      <div v-if="isLoggedIn">
        <p><strong>{{ userId }}</strong></p>
        <p>멤버십: <strong>{{ membershipLevel }}</strong></p>
        <p>충전 금액: <strong>{{ balance }}</strong>원</p>
        <p><a href="#"><strong>My Page</strong></a></p>
        <button @click="logout">로그아웃</button>
      </div>
      <div v-else>
        <button @click="login">로그인</button>
        <button @click="signup">회원가입</button>
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
              <img :src="slide.img" :alt="'Highlight Slide ' + (index + 1)" /> <!-- 셀프 클로징 -->
              <img class="highlight-play-button" src="@/assets/img/common/content-slide-icon-play-button.png" alt="Play Button" /> <!-- 셀프 클로징 -->
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
        <Calendar :events="events" />
      </div>

    </div>
  </div>
</template>

<script>
import Calendar from '../game/Calendar.vue';

export default {
  name: 'AppContent',
  components: {
    Calendar
  },
  data() {
    return {
      // 유저 정보 박스
      isLoggedIn: true,  // 로그인 여부를 확인하는 변수
      userId: 'user@mail.com',
      membershipLevel: 'Gold', // <--- 멤버십 레벨 아이콘 삽입
      balance: 10000,
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


      // Calendar에 전달할 경기 일정 데이터
      events: [
        {
          id: 1,
          logo: require('@/assets/img/game/blueFangs.png'),
          location: '인천',
          time: '18:30',
          result: '승',
          score: '6:3',
          date: '2024-10-23'
        },
        // 다른 이벤트 데이터 추가 가능
      ]


    };
  },
  methods: {
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
    }
  }
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
  position: absolute;
  top: 630px;
  right: 100px;
  background-color: #f8f9fa;
  width: 220px;
  height: 200px;
  padding: 10px 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  text-align: right;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: center;
  z-index: 100;
}

.user-info-box button {
  background-color: #60a191;
  color: white;
  border: none;
  padding: 10px;
  margin: 5px;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
}

.user-info-box button:hover {
  background-color: #4f8578;
}

.user-info-box p {
  margin: 5px 5px;
}

.user-info-box a:hover {
  text-decoration: underline;
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
  width: 500px;
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