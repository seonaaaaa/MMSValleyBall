import { createApp } from 'vue';
import App from './App.vue';
import router from './assets/js/router';
import axios from 'axios';

const app = createApp(App);
app.config.globalProperties.$axios = axios;
app.use(router);
app.mount('#app');