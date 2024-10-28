<template>
  <div class="greeting-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />
  
    <!-- 인사말, 연혁, CI, 스폰서 메뉴 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/greeting' }" @click="navigateTo('/mms/greeting')">인사말</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/history' }" @click="navigateTo('/mms/history')">연혁</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/ci' }" @click="navigateTo('/mms/ci')">CI</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/sponsor' }" @click="navigateTo('/mms/sponsor')">스폰서</div>
    </div>
  
    <!-- 인사말 페이지 내용 -->
    <div class="greeting-content">
      <!-- 슬로건 -->
      <div class="slogan">
        <h1>Together, We Spike!</h1>
        <p>GS MMS Volleyball Club</p>
      </div>
      <!-- 인사말 텍스트 -->
      <div class="message">
        <p>안녕하세요, GS MMS 배구단을 사랑해주시는 팬 여러분!</p>
        <p>항상 뜨거운 응원과 성원 보내주셔서 진심으로 감사드립니다. 여러분의 열정과 사랑이 저희 팀의 가장 큰 원동력입니다. 그 덕분에 매 경기 최선을 다해 뛰고 있으며, 선수들 모두가 여러분께 멋진 경기를 선사하기 위해 열심히 준비하고 있습니다.</p>
        <p>올 시즌 저희 팀은 한층 더 성장하고 발전된 모습을 보여드리고자 합니다. 선수들은 각자의 자리에서 최선을 다해 팀워크를 강화하며, 더 높은 목표를 위해 끊임없이 도전하고 있습니다.</p>
        <p>앞으로도 저희와 함께 기쁨과 감동을 나눠주시길 바랍니다. 팬 여러분의 응원이 저희에게 큰 힘이 됩니다. 경기장에서 여러분과 함께할 날을 기대하며, 항상 행복하고 건강한 나날 보내시길 바랍니다.</p>
        <p class="message-ceo">MMS 프로배구단 구단주 김형민</p>
      </div>
      <div>
        <img src="@/assets/img/mms/mms-greeting-img.png" class="greeting-img" alt="greeting message img">
      </div>
    </div>
  </div>
  </template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
    name: 'AppGreeting',
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
  .greeting-page {
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
  
  /* 인사말 */
  .greeting-content {
    margin-top: 20px;
    padding-bottom: 78px;
  }
  
  .slogan {
    margin-bottom: 40px;
  }
  
  .slogan h1 {
    font-size: 60px;
    color: #4f8578;
    font-weight: bold;
    text-transform: uppercase;
    letter-spacing: 2px;
  }
  
  .slogan p {
    font-size: 24px;
    color: #393939;
    margin-top: 10px;
  }
  
  .message {
    text-align: left;
    max-width: 800px;
    margin: 0 auto;
    padding-bottom: 30px;
    font-size: 18px;
    line-height: 1.8;
    color: #333;
  }
  
  .message p {
    margin-bottom: 10px;
  }
  
  .message-ceo {
    text-align: right;
    font-weight: bold;
    margin-top: 30px;
  }
  
  .greeting-img {
    width: 100%;
    max-width: 800px;
    height: auto;
    margin: 20px 0;
  }
  </style>