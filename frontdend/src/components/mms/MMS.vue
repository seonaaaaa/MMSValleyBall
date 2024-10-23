<template>
  <div class="mms-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />
  
    <div class="spacer"></div>
  
    <!-- 여기는 콘텐츠 없음, 주소창에서 /메인메뉴이름 들어가면 나오는 페이지, 따로 연결 링크 달아둔 것 없음 -->
  </div>
  </template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
      name: 'AppMMS',
      components: {
      LogoHeader
    },
  }
  </script>
  
  <style scoped>
  .mms-page {
    padding-top: var(--header-height);
    padding-bottom: var(--footer-height);
    text-align: center;
  }
  
  .menu {
    list-style: none;
    display: flex;
    justify-content: center;
    gap: 20px;
  }
  
  .menu li {
    display: inline-block;
  }
  
  .spacer {
    height: 400px;
  }
  </style>