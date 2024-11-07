<template>
  <div id="app">
    <AppHeader v-if="showHeaderFooter" @logoutSuccess="handleLogoutSuccess" :isLoggedIn="isLoggedIn"/>
    <router-view :isLoggedIn="isLoggedIn" :balance="Number(balance)" 
    :membership="membership" 
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

export default {
  name: 'App',
  components: {
    AppHeader,
    AppFooter
  },
  setup() {
    const route = useRoute();
    const showHeaderFooter = computed(() => route.path !== '/myPage/rechargee');
    let isLoggedIn = ref(false); 
    let balance = ref(0); 
    let membership = ref('bronze');

    // 로그인 성공 처리 함수
    const handleLoginSuccess = () => {
      isLoggedIn.value = true;
      sessionStorage.setItem('isLoggedIn', 'true');
    };

    // 로그아웃 처리 함수
    const handleLogoutSuccess = () => {
      isLoggedIn.value = false;
      balance.value = 0;
      membership.value = 'bronze';
      sessionStorage.removeItem('isLoggedIn');
      sessionStorage.removeItem('balance');
      sessionStorage.removeItem('membership');
    };

    const updateBalance = (getBalance) => {
      balance.value = getBalance;
      sessionStorage.setItem('balance', String(getBalance));
    };

    const updateMembership = (getMembership) => {
      membership.value = getMembership;
      sessionStorage.setItem('membership', getMembership);
    };

    

    onMounted(() => {
      // 새로고침 시 sessionStorage에 저장된 값을 불러와 설정
      isLoggedIn.value = sessionStorage.getItem('isLoggedIn') === 'true';
      balance.value = Number(sessionStorage.getItem('balance')) || 0;
      membership.value = sessionStorage.getItem('membership') || 'bronze';

      window.addEventListener('message', (event) => {
        if (event.data.type === 'updateBalance') {
          balance.value = event.data.balance;
          sessionStorage.setItem('balance', event.data.balance);
        }
      });
    });
    
    return {
      showHeaderFooter,
      isLoggedIn,
      balance,
      membership,
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

/* 1024px 이하일 때 root 변수 업데이트 */
@media (max-width: 1024px) {
  :root {
    --header-height: 115px;
    --footer-height: 0px;
  }
}

@media (max-width: 768px) {
  :root {
    --header-height: 56px;
    --footer-height: 0px;
  }
}
</style>
