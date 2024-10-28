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
          <a href="#">{{ item.title }}</a>
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
          <a href="#">{{ item.title }}</a>
          <ul class="dropdown">
            <li v-for="subItem in item.submenu" :key="subItem">
              <a href="#">{{ subItem }}</a>
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
        submenu: [
          { title: '인사말', path: '/mms/greeting' }, 
          { title: '연혁', path: '/mms/history' }, 
          { title: 'CI', path: '/mms/ci' }, 
          { title: '스폰서', path: '/mms/sponsor' }
        ] 
        },
        { 
          title: 'TEAM', 
          submenu: [
            { title: '선수', path: '/team/players' }, 
            { title: '스태프', path: '/team/staff' }, 
            { title: '구장', path: '/team/stadium' }
          ] 
        },
        { // 메뉴 임의로 추가 ***
          title: 'GAME', 
          path: '/game',  // 서브메뉴 대신 경로만 설정
          submenu: []  // 서브메뉴가 없으므로 빈 배열로 처리
        }
      ],
      rightMenu: [
        { title: 'TICKET', submenu: ['티켓 안내', '티켓 구매', '멤버십'] },
        { title: 'CONTENTS', submenu: ['공지사항', '자유게시판'] }, // 메뉴 임의로 추가 ***
        { title: 'MY PAGE', submenu: ['예매 내역', '멤버십 내역', '개인정보 수정', '회원 탈퇴'] }
      ]
    }
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
  color: #393939;
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
  color: #393939;
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
  color: #393939;
}

.dropdown li a:hover {
  color: #000000;
  background-color: #f2f2f2;
  border-radius: 5px;
}
</style>