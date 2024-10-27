<template>
    <div class="game-schedule-page">
      <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
      <LogoHeader />
  
      <!-- 경기 일정, 경기 결과 메뉴 -->
      <div class="menu">
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/game/schedule' }" @click="navigateTo('/game/schedule')">경기 일정</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/game/results' }" @click="navigateTo('/game/results')">경기 결과</div>
      </div>
    
      <!-- 경기 일정 페이지 내용 -->
      <div class="game-schedule-content">
      <CalendarTotal :events="events" />
  
  
      </div>
    </div>
  </template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  import CalendarTotal from './CalendarTotal.vue';
  
  export default {
      name: 'AppSchedule',
      components: {
        LogoHeader,
        CalendarTotal
    },    
    data() {
      return {
        // 현재 활성화된 경로
        activeMenu: this.$route.path,

        // Axios로 API와 연동하기 전에 화면 테스트
        // Calendar에 전달할 경기 일정 데이터
        events: [
          {
            id: 1,
            team: 'blueFangs',
            location: '서울',
            time: '14:00',
            result: '승',
            score: '3:1',
            date: '2024-10-19',
            isHomeGame: true // 홈 경기
          },
          {
            id: 2,
            team: 'jumbos',
            location: '수원',
            time: '17:00',
            result: '승',
            score: '2:5',
            date: '2024-10-23',
            isHomeGame: false // 원정 경기
          },
          {
            id: 3,
            team: 'ok',
            location: '부산',
            time: '14:00',
            result: '패',
            score: '0:0',
            date: '2024-10-27',
            isHomeGame: false
          },
          {
            id: 4,
            team: 'vixtorm',
            location: '서울',
            time: '19:00',
            result: '',
            score: '0:0',
            date: '2024-10-31',
            isHomeGame: true
          },
          {
            id: 5,
            team: 'walkers',
            location: '서울',
            time: '16:00',
            result: '',
            score: '0:0',
            date: '2024-11-05',
            isHomeGame: true
          },
          {
            id: 6,
            team: 'won',
            location: '서울',
            time: '19:00',
            result: '',
            score: '0:0',
            date: '2024-11-08',
            isHomeGame: false
          }
        ]
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
  
  <style>
  .game-schedule-page {
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
  
  /* 경기 일정 */
  .game-schedule-content {
    padding-top: 50px;
    padding-bottom: 80px;
  }
  </style>