<template>
  <header>
    <!-- 로그인, 회원가입 링크 상단 -->
    <div class="auth-links">
      <a href="#" class="login">로그인</a> | <a href="#" class="signup">회원가입</a>
    </div>
  
    <!-- 네비게이션 바 -->
    <nav class="navbar">
      <div class="nav-menu">
        <!-- 좌측 메뉴 -->
        <ul class="nav-left">
          <li class="nav-item" v-for="(item, index) in leftMenu" :key="index">
            <router-link :to="item.path">{{ item.title }}</router-link>
            <ul class="dropdown">
              <li v-for="subItem in item.submenu" :key="subItem">
                <router-link :to="subItem.path">{{ subItem.title }}</router-link>
              </li>
            </ul>
          </li>
        </ul>
  
        <!-- 중앙 로고 -->
        <div class="logo">
          <router-link to="/">
            <img src="@/assets/logo-mms.png" alt="Team Logo">
          </router-link>
        </div>
  
        <!-- 우측 메뉴 -->
        <ul class="nav-right">
          <li class="nav-item" v-for="(item, index) in rightMenu" :key="index">
            <router-link :to="item.path">{{ item.title }}</router-link>
            <ul class="dropdown">
              <li v-for="subItem in item.submenu" :key="subItem.path">
                <router-link :to="subItem.path">{{ subItem.title }}</router-link>
              </li>
            </ul>
          </li>
        </ul>
      </div>
    </nav>
  </header>
  </template>
  
  <script>
  
  export default {
    name: 'AppHeader',
  
    data() {
      return {
        leftMenu: [
        { 
          title: 'MMS', 
          path: '/mms/greeting',  // 첫 번째 서브메뉴의 경로
          submenu: [
            { title: '인사말', path: '/mms/greeting' }, 
            { title: '연혁', path: '/mms/history' }, 
            { title: 'CI', path: '/mms/ci' }, 
            { title: '스폰서', path: '/mms/sponsor' }
          ] 
        },
        { 
          title: 'TEAM',
          path: '/team/players', 
          submenu: [
            { title: '선수', path: '/team/players' }, 
            { title: '스태프', path: '/team/staff' }, 
            { title: '구장', path: '/team/stadium' }
          ] 
        },
        {
          title: 'GAME', 
          path: '/game/schedule',
          submenu: [
            { title: '경기 일정', path: '/game/schedule' },
            { title: '경기 결과', path: '/game/results' },
          ]
        }
      ],
      rightMenu: [
        { 
          title: 'TICKET', 
          path: '/ticket/info',
          submenu: [
            { title: '티켓 안내', path: '/ticket/info' }, 
            { title: '티켓 구매', path: '/ticket/purchase' }
          ] 
        },
        { 
          title: 'MEMBERSHIP', 
          path: '/membership/info',
          submenu: [
            { title: '멤버십 안내', path: '/membership/info' }, 
            { title: '멤버십 구매', path: '/membership/purchase' }
          ] 
        },
        { 
          title: 'MY PAGE', 
          path: '/mypage/reservations',
          submenu: [
            { title: '예매 내역', path: '/mypage/reservations' }, 
            { title: '나의 멤버십', path: '/mypage/membership' }, 
            { title: '개인정보 수정', path: '/mypage/edit-profile' }, 
          ] 
        }
      ]
    };
  }
}
</script>

<style scoped>
header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

/* 로그인, 회원가입 링크 상단 */
.auth-links {
  display: flex;
  justify-content: flex-end;
  padding: 10px 20px;
  background-color: white;
  /* border: 10px solid blue; */
}

.auth-links a {
  color: #565656;
  margin: 0 10px;
}

.auth-links a:hover {
  color: #000000;
}

/* 네비게이션 바 */
.navbar {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-bottom: 20px;
  background-color: white;
  border-bottom: 1px solid #ddd;
  width: 100%;
  box-shadow: 0px 8px 8px -4px rgba(0, 0, 0, 0.1);
  /* border: 10px solid red;	 */
}

/* 메뉴 전체를 감싸는 컨테이너 */
.nav-menu {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 0;
  margin: 0;
}

/* 중앙 로고 */
.logo {
  flex-grow: 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo img {
  height: 100px;
}

/* 좌측 메뉴 */
.nav-left {
  display: flex;
  justify-content: center; 
  flex-grow: 1;
}

/* 우측 메뉴 */
.nav-right {
  display: flex;
  justify-content: center; 
  flex-grow: 1;
}

/* 1차 메뉴 */
.nav-item {
  position: relative;
  font-size: 18px;
  /* border: 10px solid green; */
}

.nav-item a {
  color: #565656;
  font-weight: 600;
  padding: 0 50px;
}

/* 2차 메뉴 */
.dropdown {
  display: none;
  position: absolute;
  top: 100%;  /* 2차 메뉴를 1차 메뉴의 바로 아래에 위치시킴 */
  left: 0;  /* 2차 메뉴를 1차 메뉴의 왼쪽 벽에 붙임 */
  width: 100%;  /* 부모의 너비를 그대로 받아옴 */
  padding: 0;
  background-color: white;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  font-size: 16px;
}

.nav-item:hover .dropdown {
  display: block;
}

.dropdown li a {
  padding: 10px 20px;
  display: block;
  color: #565656;
}

.dropdown li a:hover {
  color: #000000;
  background-color: #f2f2f2;
  border-radius: 5px;
}
</style>