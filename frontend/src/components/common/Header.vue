<template>
  <header>
    <!-- 로그인, 회원가입 링크 상단 -->
    <div class="auth-links">
      <div class="hamburger" @click="toggleMenu" v-if="isMobile">
        <span></span>
        <span></span>
        <span></span>
      </div>
      <router-link v-if="!isLoggedIn" to="/login" class="login">LOGIN</router-link>
      <router-link v-if="!isLoggedIn" to="/signup" class="signup">SIGNUP</router-link>
      <!-- <button v-if="role == 'ADMIN' && isLoggedIn" @click="goToAdminPage" class="admin">ADMIN HOME</button> -->
      <button v-if="isLoggedIn" @click="logout" class="logout">LOGOUT</button>
    </div>

    <!-- 모바일 햄버거 메뉴 -->
    <div :class="['mobile-menu', { 'is-open': isMenuOpen }]">
      <button @click="closeMobileMenu" class="close-btn">X</button>
      <ul class="mobile-menu-list">
        <li v-for="(item, index) in unifiedMenu" :key="index">
          <div @click="toggleSubMenu(index)" class="mobile-menu-title">
            {{ item.title }}
          </div>
          <ul :class="['mobile-dropdown', { 'is-open': activeIndex === index }]">
            <li v-for="subItem in item.submenu" :key="subItem.path">
              <router-link :to="subItem.path" @click="closeMobileMenu">{{ subItem.title }}</router-link>
            </li>
          </ul>
        </li>
      </ul>
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
  props: {
    isLoggedIn: {
      type: Boolean,
      required: true
    },
  },
  data() {
    return {
      isMenuOpen: false,
      activeIndex: null,
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
          path: '/myPage/reservations',
          submenu: [
            { title: '예매 내역', path: '/myPage/reservations' }, 
            { title: '나의 멤버십', path: '/myPage/membership' }, 
            { title: '나의 정보 수정', path: '/myPage/edit-profile' }, 
          ] 
        }
      ],
      role: 'guest',
    };
  },
  computed: {
    unifiedMenu() {
      return [...this.leftMenu, ...this.rightMenu];
    }
  },
  mounted(){
    const token = sessionStorage.getItem('token');
    if(token!=null){
      this.role = sessionStorage.getItem('role');
    }
  },
  methods: {
    logout() {
    // 로컬스토리지에 저장된 토큰과 사용자 정보 삭제
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('name');
    sessionStorage.removeItem('email');
    sessionStorage.removeItem('role');
    const token = sessionStorage.getItem('token');
    if (token === null) {
      console.log('토큰이 성공적으로 삭제되었습니다.');
      this.$emit('logoutSuccess');
      alert("로그아웃이 되었습니다.");
    } else {
      console.log('Header에서 토큰 삭제에 실패했습니다.', token);
    }
      this.$router.push('/'); // 메인으로
    },
    goToAdminPage(){
      const targetUrl = `http://localhost:4000/admin/userList?adminName=${encodeURIComponent(this.name)}`;
      window.location.href = targetUrl;
    },
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    },
    toggleSubMenu(index) {
      this.activeIndex = this.activeIndex === index ? null : index;
    },
    closeMobileMenu() {
      this.isMenuOpen = false;
      this.activeIndex = null;
    },
  },
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
}

.auth-links a {
  color: #565656;
  margin: 0 10px;
  font-size: 18px;
}

.auth-links a:hover {
  color: #000000;
}

/* 로그아웃 버튼 */
.logout{
  background-color: #ffffff;
  border: none; /* 테두리 없애기 */
  outline: none; /* 포커스 시 나타나는 외곽선 없애기 (선택 사항) */
  font-size: 18px;
  color: #565656;
}
.admin{
  background-color: #ffffff;
  border: none; /* 테두리 없애기 */
  outline: none; /* 포커스 시 나타나는 외곽선 없애기 (선택 사항) */
  font-size: 18px;
  color: #565656;
}
.admin:hover{
  color: #000000;
}
.logout:hover{
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
}

/* 메뉴 전체를 감싸는 컨테이너 */
.nav-menu {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 1140px;
  margin: 0 auto;
  padding: 0;
  /* margin: 0; */
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
  padding: 0;
}

/* 우측 메뉴 */
.nav-right {
  display: flex;
  justify-content: center; 
  flex-grow: 1;
  padding: 0;
}

/* 1차 메뉴 */
.nav-item {
  position: relative;
  justify-content: center;
  text-align: center;
  font-size: 18px;
  width: 160px;
}

.nav-item a {
  color: #565656;
  font-weight: 600;
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

/* 1024px 이하일 때 스타일 조정 */
@media (max-width: 1024px) {
  /* 네비게이션 바 크기 및 메뉴 폭 조정 */
  .nav-menu {
    max-width: 90%; /* 메뉴 폭 줄이기 */
  }

  /* 로고 이미지 숨기기 */
  .logo img {
    display: none;
  }

  /* 메뉴 글씨 크기 줄이기 */
  .nav-item {
    font-size: 16px;
  }
  
  /* 인증 링크 상단 글씨 크기 줄이기 */
  .auth-links a {
    font-size: 16px;
  }
}

/* 768px 이하일 때 스타일 조정 */
@media (max-width: 768px) {
  /* 네비게이션 바 크기 및 메뉴 폭 조정 */
  .nav-menu {
    max-width: 100%; /* 메뉴 폭 줄이기 */
    padding: 0 10px; /* 좌우 패딩 줄이기 */
  }

  /* 로고 이미지 숨기기 */
  .logo img {
    display: none;
  }

  /* 메뉴 항목의 너비와 간격 줄이기 */
  .nav-item {
    width: 120px; /* 메뉴 항목 너비 줄이기 */
    font-size: 16px; /* 글씨 크기 유지 */
    padding: 0 5px; /* 메뉴 항목 간 좌우 패딩 줄이기 */
  }

  /* 인증 링크 상단 글씨 크기와 간격 조정 */
  .auth-links a {
    font-size: 16px; /* 글씨 크기 유지 */
    margin: 0 5px; /* 링크 간격 줄이기 */
  }
}

/* 햄버거 아이콘 */
.hamburger {
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 25px;
  margin-right: auto;
}

.hamburger span {
  display: block;
  height: 3px;
  background-color: #333;
  transition: all 0.3s ease;
}

/* 모바일 메뉴 */
.mobile-menu {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: white;
  padding: 60px 20px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
  transform: translateX(-100%); /* 화면 왼쪽 바깥에 시작 */
  transition: transform 0.3s ease;
  z-index: 1001;
}

.mobile-menu.is-open {
  display: block;
  transform: translateX(0); /* 화면 내로 슬라이드 */
}

/* 닫기 버튼 */
.close-btn {
  position: absolute;
  top: 10px;
  right: 20px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

/* 모바일 메뉴 항목 */
.mobile-menu-list {
  list-style: none;
  padding: 0;
}

.mobile-menu-title {
  font-size: 24px;
  margin-bottom: 10px;
  cursor: pointer;
}

/* 모바일 드롭다운 메뉴 */
.mobile-dropdown {
  list-style: none;
  padding-left: 20px;
  margin-top: 5px;
  overflow: hidden;
  max-height: 0;
  transition: max-height 0.3s ease;
}

.mobile-dropdown.is-open {
  max-height: 300px;
}

.mobile-dropdown li {
  margin-bottom: 10px;
}

.mobile-dropdown li a {
  color: #565656;
  font-size: 18px;
}

.mobile-dropdown li a:hover {
  color: #000;
}

@media (max-width: 480px) {

  .navbar,
  .nav-menu {
    display: none;
  }

  .hamburger {
    display: flex;
  }

  .auth-links {
    box-shadow: 0px 8px 8px -4px rgba(0, 0, 0, 0.1);
  }
}

@media (min-width: 481px) {
  .hamburger,
  .mobile-menu {
    display: none;
  }
}
</style>