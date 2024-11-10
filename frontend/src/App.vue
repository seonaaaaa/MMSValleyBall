<template>
  <div id="app">
    <AppHeader v-if="showHeaderFooter" @logoutSuccess="handleLogoutSuccess" :user="user"/>
    <router-view :user="user"
    @loginSuccess="handleLoginSuccess" @logoutSuccess="handleLogoutSuccess" 
    @getBalance="updateBalance" @getMembership="updateMembership"
    />
    <AppFooter v-if="showHeaderFooter"/>
  </div>
</template>

<script>
import AppHeader from './components/common/Header.vue'
import AppFooter from './components/common/Footer.vue'
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

export default {
  name: 'App',
  components: {
    AppHeader,
    AppFooter
  },
  setup() {
    const route = useRoute();
    const showHeaderFooter = computed(() => route.path !== '/myPage/rechargee');
    let user = ref({
      name : '',
      role : 'guest',
      isLoggedIn : false,
      balance : 0,
      membership : 'bronze'
    });

    // 로그인 성공 처리 함수
    const handleLoginSuccess = (token) => {
      user.value.isLoggedIn = true;
      sessionStorage.setItem('isLoggedIn', 'true');
      axios.defaults.headers.common["Authorization"] = sessionStorage.getItem('token');
      let payload = null;
      console
      try {
        const base64Payload = token.split('.')[1]; // 토큰의 두 번째 부분 (Payload)
        const base64 = base64Payload.replace(/-/g, '+').replace(/_/g, '/'); // Base64 형식을 표준으로 맞춤
        const decodedPayload = decodeURIComponent(
            atob(base64)
            .split('')
            .map((c) => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
            .join('')
        );
        payload = JSON.parse(decodedPayload); // JSON 파싱하여 객체로 반환
         // 유저 정보를 로컬스토리지에 저장
        user.value.name = payload.name;
        user.value.role = payload.role;
        sessionStorage.setItem('name', payload.name);
        sessionStorage.setItem('role', payload.role);
      } catch (error) {
          console.error('토큰 파싱 실패', error);
      }
    };
    // 로그아웃 처리 함수
    const handleLogoutSuccess = () => {
      user.value = {
        name : '',
        role : 'guest',
        isLoggedIn : false,
        balance : 0,
        membership : 'bronze'
      }
      sessionStorage.clear();
    };

    const updateBalance = (getBalance) => {
      user.value.balance = Number(getBalance);
      sessionStorage.setItem('balance', String(getBalance));
    };

    const updateMembership = (getMembership) => {
      user.value.membership = getMembership;
      sessionStorage.setItem('membership', getMembership);
    };
    
    onMounted(() => {
      // 새로고침 시 sessionStorage에 저장된 값을 불러와 설정
      user.value.isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';
      user.value.balance = Number(sessionStorage.getItem('balance')) || 0;
      user.value.membership = sessionStorage.getItem('membership') || 'bronze';
      user.value.name = sessionStorage.getItem('name') || '';
      user.value.role = sessionStorage.getItem('role') || 'guest';
      const token = sessionStorage.getItem("token");
      if (token) {
        axios.defaults.headers.common["Authorization"] = token;
      }

      window.addEventListener('message', (event) => {
        if (event.data.type === 'updateBalance') {
          user.value.balance = event.data.balance;
          sessionStorage.setItem('balance', event.data.balance);
        }
      });
    });
    return {
      showHeaderFooter,
      user,
      handleLoginSuccess,
      handleLogoutSuccess,
      updateBalance,
      updateMembership,
    };
  } 
}
</script>

<style>
#app {
  min-height: 100vh;  /* 전체 화면을 차지하도록 설정 */
  display: flex;
  flex-direction: column;  /* 상하 배치 */
}

html {
  box-sizing: border-box;
  scroll-behavior: smooth;
  margin: 0;
  padding: 0;
}

*, *::before, *::after {
  box-sizing: border-box;
  font-family: 'Noto Sans KR', sans-serif;
}

body {
  width: 100%;
  margin: 0;
  padding: 0;
}

body, ul, li {
  list-style: none;  /* ul 태그의 ● 제거 */
}

a {
  text-decoration: none; /* a 태그의 밑줄 제거 */
}

:root {
  --header-height: 168px; /* Header 높이를 변수로 정의 */
  --footer-height: 0px; /* Footer 높이를 변수로 정의 */
}
</style>
