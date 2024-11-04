import { createRouter, createWebHistory } from 'vue-router';

import AppContent from '@/components/common/Content.vue';

// MMS 메뉴
import AppMMS from '@/components/mms/MMS.vue';
import AppGreeting from '@/components/mms/Greeting.vue';
import AppHistory from '@/components/mms/History.vue';
import AppCI from '@/components/mms/CI.vue';
import AppSponsor from '@/components/mms/Sponsor.vue';

// TEAM 메뉴
import AppPlayers from '@/components/team/Players.vue';
import AppStaff from '@/components/team/Staff.vue';
import AppStadium from '@/components/team/Stadium.vue';

// GAME 메뉴
import AppSchedule from '@/components/game/Schedule.vue';
import AppResults from '@/components/game/Results.vue';

// TICKET 메뉴
import TicketInfo from '@/components/ticket/TicketInfo.vue';
import TicketPurchase from '@/components/ticket/TicketPurchase.vue';

// MEMBERSHIP 메뉴
import MembershipInfo from '@/components/membership/MembershipInfo.vue';
import MembershipPurchase from '@/components/membership/MembershipPurchase.vue';

// MY PAGE 메뉴
import ReservationsView from '@/components/mypage/ReservationsView.vue';
import MembershipView from '@/components/mypage/MembershipView.vue';
import EditProfile from '@/components/mypage/EditProfile.vue';
import Recharge from '@/components/mypage/Recharge.vue';


// login, joinpage 만들기
import loginPage from '@/components/member/Login.vue';
import SignupPage from '@/components/member/Signup.vue'

const routes = [
  // 기본 홈 화면 경로 추가
  {
    path: '/',
    name: 'Home',
    component: AppContent,
    meta: { requiresAuth: false }, // 인증이 필요 없는 페이지
  },

  // MMS 메뉴
  {
    path: '/mms',
    name: 'MMS',
    component: AppMMS,
    meta: { requiresAuth: false }
  },
  {
    path: '/mms/greeting',
    name: 'Greeting',
    component: AppGreeting,
    meta: { requiresAuth: false }
  },
  {
    path: '/mms/history',
    name: 'History',
    component: AppHistory,
    meta: { requiresAuth: false }
  },
  {
    path: '/mms/ci',
    name: 'CI',
    component: AppCI,
    meta: { requiresAuth: false }
  },
  {
    path: '/mms/sponsor',
    name: 'Sponsor',
    component: AppSponsor,
    meta: { requiresAuth: false }
  },
  // TEAM 메뉴
  {
    path: '/team/players',
    name: 'Players',
    component: AppPlayers,
    meta: { requiresAuth: false }
  },
  {
    path: '/team/staff',
    name: 'Staff',
    component: AppStaff,
    meta: { requiresAuth: false }
  },
  {
    path: '/team/stadium',
    name: 'Stadium',
    component: AppStadium,
    meta: { requiresAuth: false }
  },
   // GAME 메뉴
  {
    path: '/game/schedule',
    name: 'Schedule',
    component: AppSchedule,
    meta: { requiresAuth: false }
  },
  {
    path: '/game/results',
    name: 'Results',
    component: AppResults,
    meta: { requiresAuth: false }
  },
  // TICKET 메뉴
  {
    path: '/ticket/info',
    name: 'TicketInfo',
    component: TicketInfo,
    meta: { requiresAuth: false }
  },
  {
    path: '/ticket/purchase',
    name: 'TicketPurchase',
    component: TicketPurchase,
    meta: { requiresAuth: false }
  },
  // MEMBERSHIP 메뉴
  {
    path: '/membership/info',
    name: 'MembershipInfo',
    component: MembershipInfo,
    meta: { requiresAuth: false }
  },
  {
    path: '/membership/purchase',
    name: 'MembershipPurchase',
    component: MembershipPurchase,
    meta: { requiresAuth: true }, // 인증이 필요한 페이지
  },
  // MY PAGE 메뉴
  {
    path: '/myPage/reservations',
    name: 'ReservationsView',
    component: ReservationsView,
    meta: { requiresAuth: true }
  },
  {
    path: '/myPage/membership',
    name: 'MembershipView',
    component: MembershipView,
    meta: { requiresAuth: true }
  },
  {
    path: '/myPage/edit-profile',
    name: 'EditProfile',
    component: EditProfile,
    meta: { requiresAuth: true }
  },
  {
    path: '/myPage/rechargee',
    name: 'Recharge',
    component: Recharge,
    meta: { requiresAuth: true }
  },
  // 로그인, 회원가입 
  {
    path: '/login',
    name: 'loginPage',
    component: loginPage,
  },
  {
    path: '/signup',
    name: 'SignupPage',
    component: SignupPage,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), // HTML5 히스토리 모드 사용
  routes
});

export default router;