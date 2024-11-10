import { createApp } from 'vue';
import App from './App.vue';
import router from './assets/js/router';
import axios from 'axios';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

const app = createApp(App);
app.config.globalProperties.$axios = axios;
axios.defaults.baseURL = 'http://localhost:4000';

app.use(router);

// 네비게이션 가드 설정
router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem('token'); // 토큰을 세션 스토리지에서 가져옴

  // 인증이 필요한 페이지에 접근할 때
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (token) {
      next(); // 토큰이 있으면 접근 허용
    } else {
      alert("로그인 후 이용해주세요.\n로그인 페이지로 이동합니다.");
      next({
        path: '/login',
        query: { from: to.fullPath }, // 이전 페이지 경로 저장
      });
    }
  } else {
    next(); // 인증이 필요 없는 페이지는 그냥 이동
  }
});

library.add(faEye, faEyeSlash);
app.component('font-awesome-icon', FontAwesomeIcon);

app.mount('#app');
