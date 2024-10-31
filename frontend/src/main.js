import { createApp } from 'vue';
import App from './App.vue';
import router from './assets/js/router';
import axios from 'axios';


const app = createApp(App);
app.config.globalProperties.$axios = axios;
axios.defaults.baseURL = 'http://localhost:4000';

app.use(router);
// 네비게이션 가드 설정
router.beforeEach((to, from, next) => {
  // 인증이 필요한 페이지에 접근할 때
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const token = localStorage.getItem('accessToken'); // 토큰을 로컬스토리지에서 가져옴
    if (token!=null) {
      next(); // 토큰이 있다면 페이지로 이동
    } else {
      next('/login'); // 토큰이 없다면 로그인 페이지로 리디렉션
    }
  } else {
    next(); // 인증이 필요 없는 페이지는 그냥 이동
  }
});
app.mount('#app');