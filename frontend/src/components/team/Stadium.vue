<template>
  <div class="stadium-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />

    <!-- 선수, 스태프, 구장 메뉴 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/team/players' }" @click="navigateTo('/team/players')">선수</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/team/staff' }" @click="navigateTo('/team/staff')">스태프</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/team/stadium' }" @click="navigateTo('/team/stadium')">구장</div>
    </div>

    <!-- 구장 페이지 내용 -->
    <div class="stadium-content">
      <!-- 구장 소개 -->
      <div class="stadium-intro-container">
        <div class="stadium-intro">
          <h1>GS MMS SPIKE HI STADIUM</h1>
          <p>
            MMS의 홈 경기장인 서울 하이체육관은 최신 시설을 자랑하는 배구 전용 경기장입니다. <br>
            탁월한 접근성과 쾌적한 관람 환경을 제공하며, 선수들의 숨소리까지 생생하게 느낄 수 있는 <br>
            가까운 관람석 배치가 특징입니다. 관객들은 경기장의 열기를 직접 느끼며, <br>
            배구의 역동성과 짜릿함을 온전히 즐길 수 있습니다.
          </p>
        </div>
      </div>

      <!-- 구장 정보 -->
      <div class="stadium-info-container">
        <h2>구장 안내</h2>
        <div class="stadium-info">
        <div class="stadium-img">
          <img src="@/assets/img/team/team-stadium-out.png" alt="stadium img"/>
        </div>
        <div class="info-list">
          <ul>
            <li><strong>주소 :</strong> 서울 종로구 인사동길 12 (지번: 서울 종로구 인사동 43)</li>
            <li><strong>관람석 :</strong> 5,450석</li>
            <li><strong>수용인원 :</strong> 6,000명</li>
            <li><strong>부대시설 :</strong> 매점, 카페테리아, 체력단련장 등</li>
          </ul>
        </div>
      </div>
      </div>

      <!-- 교통 안내 -->
      <div class="transport-info">
        <h2>교통 안내</h2>
        <div class="transport-flex">
          <div class="transport-bus">
            <h3>버스</h3>
            <p>시내버스: 101, 103, 143, 150, 160, 201, 260, 262, 270</p>
            <p>직행좌석버스: 1101, 1102, 7101, 9301</p>
            <p>광역버스: M4101, M4102</p>
          </div>
          <div class="transport-subway">
            <h3>지하철</h3>
            <p>시내버스: 101, 103, 143, 150, 160, 201, 260, 262, 270</p>
            <p>직행좌석버스: 1101, 1102, 7101, 9301</p>
            <p>광역버스: M4101, M4102</p>
          </div>
        </div>
      </div>

      <!-- 주차 안내 -->
      <div class="parking-info">
        <h2>주차 안내</h2>
        <p>하이체육관 내 주차 가능하나 인근 도로가 혼잡하므로 대중교통 이용 바랍니다.</p>
      </div>

      <!-- 찾아오시는 길 -->
      <div class="directions-map">
        <h2>찾아오시는 길</h2>
        <!-- 구글 지도 -->
        <!-- <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3162.171804369904!2d126.98236254092268!3d37.574571922152806!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x357ca2c2ea3fdf6b%3A0x5723ef2aa5dbaa07!2z7ISc7Jq47Yq567OE7IucIOyiheuhnOq1rCDsnbjsgqzrj5kxMuq4uA!5e0!3m2!1sko!2skr!4v1729786353973!5m2!1sko!2skr" 
        width="800" height="400" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe> -->
        <!-- 카카오 지도 API -->
        <div class="kakao-map-container">
          <KakaoMap />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import LogoHeader from '../common/LogoHeader.vue';
import KakaoMap from './KakaoMap.vue';

export default {
  name: 'AppStadium',
  components: {
    LogoHeader,
    KakaoMap
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
.stadium-page {
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

/* 구장 소개 */
.stadium-intro-container {
  position: relative;
  background-image: url("@/assets/img/team/team-stadium-out.png");
  background-size: cover;
  background-position: center;
  width: 100%;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 흰색 필터(오버레이) 추가 */
.stadium-intro-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.1);
  z-index: 1;
}

.stadium-intro {
  position: absolute;
  z-index: 2;
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 40px;
  border-radius: 10px;
  text-align: center;
  max-width: 900px;
}

/* 구장 정보 */
.stadium-info-container {
  margin-top: 70px;
}

.stadium-info {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 60px;
  margin-top: 40px;
}

.stadium-img img{
  border-radius: 6px;
  width: 400px;
}

.info-list {
  width: 400px;
  text-align: left;
}

.info-list ul {
  padding-left: 0;
}

.info-list li {
  margin-bottom: 15px;
  font-size: 1.1rem;
}

/* 교통 안내 */
.transport-info {
  margin-top: 60px;
}

.transport-flex {
  display: flex;
  justify-content: center;
  gap: 60px;
}

.transport-bus,
.transport-subway {
  text-align: left;
  width: 400px;
}

/* 주차 안내 */
.parking-info {
  margin-top: 60px;
}

.parking-info h2 {
  margin-bottom: 30px;
}

/* 찾아오시는 길 */
.directions-map {
  margin-top: 60px;
  margin-bottom: 80px;
}

.directions-map h2 {
  margin-bottom: 30px;
}

.kakao-map-container {
    display: flex;
    justify-content: center; /* 가로 가운데 정렬 */
    align-items: center; /* 세로 가운데 정렬 */
}
</style>