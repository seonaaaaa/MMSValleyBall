<template>
  <div class="players-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />
  
    <!-- 선수, 스태프, 구장 메뉴 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/team/players' }" @click="navigateTo('/team/players')">선수</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/team/staff' }" @click="navigateTo('/team/staff')">스태프</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/team/stadium' }" @click="navigateTo('/team/stadium')">구장</div>
    </div>
  
    <!-- 선수 페이지 내용 -->
    <div class="players-content">
        <!-- 아포짓 스파이커 (레프트) -->
        <div class="players-group">
          <div class="position-title"><h2>아포짓 스파이커 (레프트)</h2></div>
          <div class="player-cards">
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-001.png" alt="Player Image" />
                <span class="player-number-background">No.11</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.11</p>
                <p class="player-name">이민재</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-002.png" alt="Player Image" />
                <span class="player-number-background">No.3</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.3</p>
                <p class="player-name">유현우</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-003.png" alt="Player Image" />
                <span class="player-number-background">No.6</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.6</p>
                <p class="player-name">박민중</p>
              </div>
            </div>
          </div>
        </div>
  
        <!-- 아웃사이드 히터 (라이트) -->
        <div class="players-group">
          <div class="position-title"><h2>아웃사이드 히터 (라이트)</h2></div>
          <div class="player-cards">
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-010.png" alt="Player Image" />
                <span class="player-number-background">No.9</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.9</p>
                <p class="player-name">박준형</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-006.png" alt="Player Image" />
                <span class="player-number-background">No.15</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.15</p>
                <p class="player-name">김도영</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-007.png" alt="Player Image" />
                <span class="player-number-background">No.4</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.4</p>
                <p class="player-name">경희민</p>
              </div>
            </div>
          </div>
        </div>
  
        <!-- 세터 -->
        <div class="players-group">
          <div class="position-title"><h2>세터</h2></div>
          <div class="player-cards">
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-008.png" alt="Player Image" />
                <span class="player-number-background">No.8</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.8</p>
                <p class="player-name">김영호</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-007.png" alt="Player Image" />
                <span class="player-number-background">No.19</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.19</p>
                <p class="player-name">최승규</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-010.png" alt="Player Image" />
                <span class="player-number-background">No.13</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.13</p>
                <p class="player-name">정지언</p>
              </div>
            </div>
          </div>
        </div>
  
        <!-- 미들 블로커 -->
        <div class="players-group">
          <div class="position-title"><h2>미들 블로커</h2></div>
          <div class="player-cards">
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-001.png" alt="Player Image" />
                <span class="player-number-background">No.1</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.1</p>
                <p class="player-name">정우성</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-002.png" alt="Player Image" />
                <span class="player-number-background">No.7</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.7</p>
                <p class="player-name">한지훈</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-003.png" alt="Player Image" />
                <span class="player-number-background">No.10</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.10</p>
                <p class="player-name">최진환</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-005.png" alt="Player Image" />
                <span class="player-number-background">No.20</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.20</p>
                <p class="player-name">김재형</p>
              </div>
            </div>
          </div>
        </div>
  
        <!-- 리베로 -->
        <div class="players-group">
          <div class="position-title"><h2>리베로</h2></div>
          <div class="player-cards">
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-008.png" alt="Player Image" />
                <span class="player-number-background">No.12</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.12</p>
                <p class="player-name">최강훈</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-007.png" alt="Player Image" />
                <span class="player-number-background">No.14</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.14</p>
                <p class="player-name">이윤민</p>
              </div>
            </div>
            <div class="player-card">
              <div class="player-image">
                <img src="@/assets/img/team/team-players-player-006.png" alt="Player Image" />
                <span class="player-number-background">No.17</span>
              </div>
              <div class="player-info">
                <p class="player-number">No.17</p>
                <p class="player-name">박인기</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
    name: 'AppPlayers',
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
  .players-page {
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
  
  /* 선수 */
  .players-content {
    max-width: 1000px;
    margin: 0 auto;
    padding-top: 27px;
    padding-bottom: 60px;
    text-align: center;
  }

  .position-title h2 {
    text-align: center;
    font-size: 24px;
    margin:40px 0;
    color: #333;
  }
  
  .player-cards {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-bottom: 60px;
  }
  
  .player-card {
    text-align: center;
    position: relative;
    margin: 0;
    max-width: 300px; /* 카드의 최대 너비 */
  }
  
  .player-image {
    position: relative;
    width: 13vw;
    height: 13vw;
    min-width: 190px;
    min-height: 190px; 
    border-radius: 50%;
    overflow: hidden;
    margin: 0 auto;
    display: flex; /* flexbox로 변경 */
    justify-content: center;
    align-items: center; /* 이미지를 가운데 정렬 */
  }
  
  .player-image img {
    width: 100%;
    height: auto;
  }
  
  .player-number-background {
    position: absolute;
    top: 50%;
    left: 70%;
    transform: translate(-50%, -50%);
    font-size: 2.9vw;
    color: rgba(218, 215, 215, 0.5);
    font-weight: bold;
    z-index: -1;
  }
  
  .player-info {
    margin-top: 5px;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
  }
  
  .player-number {
    font-size: 16px;
    font-weight: bold;
    color: #888;
  }
  
  .player-name {
    font-size: 18px;
    font-weight: bold;
    color: #000;
  }
  </style>