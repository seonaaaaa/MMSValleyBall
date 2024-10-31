<template>
  <div id="app">
    <AppHeader :user="user"/>
    <router-view :user="user"/>
    <AppFooter />
  </div>
</template>

<script>
import AppHeader from './components/common/Header.vue'
import AppFooter from './components/common/Footer.vue'

export default {
  name: 'App',
  components: {
    AppHeader,
    AppFooter
  },
  data() {
    return {
      user: { name: '', role: 'guest', email: '', isLoggedIn: false},
    };
  },
  mounted() {
    this.$router.beforeEach((to, from, next) => {
       if(this.existsToken()){
         next();
       }
    });
  },
  methods: {
    async existsToken() {
      const token = localStorage.getItem('accessToken');
      if (token!=null) {
        let payload = null;
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
        } catch (error) {
          console.error('토큰 파싱 실패', error);
          return null;
        }
        console.log(payload);
        this.user.role =payload.role;
        this.user.name = payload.name;
        this.user.email = payload.username;
        this.user.isLoggedIn= true;
        return true;
      } else {
        // 토큰이 없으면 로그아웃 상태
        this.user = { name: '', role: 'guest', email: '', isLoggedIn: false};
        return true;
      }
    },
  },
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
