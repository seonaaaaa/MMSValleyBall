<template>
  <div class="history-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />
  
    <!-- 인사말, 연혁, CI, 스폰서 메뉴 -->
    <div class="menu">
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/greeting' }" @click="navigateTo('/mms/greeting')">인사말</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/history' }" @click="navigateTo('/mms/history')">연혁</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/ci' }" @click="navigateTo('/mms/ci')">CI</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/sponsor' }" @click="navigateTo('/mms/sponsor')">스폰서</div>
    </div>
  
    <!-- 연혁 페이지 내용 -->
    <div class="history-content">
        <div class="year">
            <h3>2024</h3>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> 2023-2024 V리그 정규리그 3위</p>
        </div>
  
        <div class="year">
            <h3>2023</h3>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> 2022-2023 V리그 정규리그 4위</p>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> 홈 경기장 리빌딩 및 'GS MMS SPIKE HI STADIUM'으로 명명</p>
        </div>
  
        <div class="year">
            <h3>2022</h3>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> 2021-2022 V리그 정규리그 5위</p>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> 2022 순천 도드람컵 프로배구대회 2위</p>
        </div>
  
        <div class="year">
            <h3>2021</h3>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> 2020-2021 V리그 정규리그 6위</p>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> 2021 의정부·도드람컵 프로배구대회 4위</p>
        </div>
  
        <div class="year">
            <h3>2020</h3>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> GS MMS 배구단 공식 창단</p>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> 종로체육관(현 서울 하이체육관)을 홈 구장으로 지정</p>
            <p><img class="icon" src="@/assets/img/mms/mms-history-volleyball-icon.png" alt="icon"> 첫 공식 경기 개최</p>
        </div>
    </div>
  </div>
  </template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
    name: 'AppHistory',
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
  .history-page {
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
  
  /* 연혁 */
  .history-content {
    max-width: 600px;
    margin: 0 auto;
    padding-top: 35px;
    padding-bottom: 70px;
    padding-left: 30px;
    padding-right: 30px;
    text-align: left;
  }
  
  .year {
    margin-bottom: 30px;
  }
  
  .year h3 {
    font-size: 24px;
    color: #333;
    margin-bottom: 10px;
  }
  
  .year p {
    font-size: 18px;
    color: #555;
    margin-left: 20px;
  }
  
  .icon {
    width: 13px;
    height: auto;
    vertical-align: center; /* 텍스트와 수평으로 맞추기 */
    margin-right: 10px;
  }
  </style>