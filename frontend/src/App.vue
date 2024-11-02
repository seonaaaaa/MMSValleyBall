<template>
  <div id="app">
    <AppHeader  v-if="showHeaderFooter" :isLoggedIn="isLoggedIn" @logoutFromHeader="isLoggedIn=$event"/>
    <router-view :isLoggedIn="isLoggedIn" :balance="balance"
    @logoutFromContent="isLoggedIn=$event" 
    @login="isLoggedIn=$event"
    @userbalance="balance=$event"/>
    <h1>{{ balance }}</h1>
    <AppFooter  v-if="showHeaderFooter"/>
  </div>
</template>

<script>
import AppHeader from './components/common/Header.vue'
import AppFooter from './components/common/Footer.vue'
import { computed } from 'vue';
import { useRoute } from 'vue-router';

export default {
  name: 'App',
  components: {
    AppHeader,
    AppFooter
  },
  data() {
    return {
      isLoggedIn : false,
      balance : 0,
    }
  },
  mounted() {
    
  },
  setup() {
    const route = useRoute();
    const showHeaderFooter = computed(() => route.path !== '/myPage/recharge');
    return {
      showHeaderFooter
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
