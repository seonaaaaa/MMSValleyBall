import { createRouter, createWebHistory } from 'vue-router';
import AppMMS from '@/components/mms/MMS.vue'; // 상단 이미지와 메뉴만 있는 MMS 페이지
import AppGreeting from '@/components/mms/Greeting.vue'; // 인사말 컴포넌트
import AppHistory from '@/components/mms/History.vue';   // 연혁 컴포넌트
import AppCI from '@/components/mms/CI.vue';             // CI 컴포넌트
import AppSponsor from '@/components/mms/Sponsor.vue';   // 스폰서 컴포넌트

const routes = [
  {
    path: '/mms',
    name: 'MMS',
    component: AppMMS,
    meta: { hideContent: true },  // AppContent 숨김 설정
  },
  {
    path: '/mms/greeting',
    name: 'Greeting',
    component: AppGreeting,
    meta: { hideContent: true },

  },
  {
    path: '/mms/history',
    name: 'History',
    component: AppHistory,
    meta: { hideContent: true },
  },
  {
    path: '/mms/ci',
    name: 'CI',
    component: AppCI,
    meta: { hideContent: true },
  },
  {
    path: '/mms/sponsor',
    name: 'Sponsor',
    component: AppSponsor,
    meta: { hideContent: true },
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), // HTML5 히스토리 모드 사용
  routes
});

export default router;