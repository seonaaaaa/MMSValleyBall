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
import DeleteAccount from '@/components/mypage/DeleteAccount.vue';

// login, joinpage 만들기
import loginPage from '@/components/member/Login.vue';
import SignupPage from '@/components/member/Signup.vue'

const routes = [
  // 기본 홈 화면 경로 추가
  {
    path: '/',
    name: 'Home',
    component: AppContent,
    meta: { hideContent: true },
  },

  // MMS 메뉴
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
  },
  // TEAM 메뉴
  {
    path: '/team/players',
    name: 'Players',
    component: AppPlayers,
    meta: { hideContent: true },
  },
  {
    path: '/team/staff',
    name: 'Staff',
    component: AppStaff,
    meta: { hideContent: true },
  },
  {
    path: '/team/stadium',
    name: 'Stadium',
    component: AppStadium,
    meta: { hideContent: true },
  },
   // GAME 메뉴
  {
    path: '/game/schedule',
    name: 'Schedule',
    component: AppSchedule,
    meta: { hideContent: true },
  },
  {
    path: '/game/results',
    name: 'Results',
    component: AppResults,
    meta: { hideContent: true },
  },
  // TICKET 메뉴
  {
    path: '/ticket/info',
    name: 'TicketInfo',
    component: TicketInfo,
    meta: { hideContent: true },
  },
  {
    path: '/ticket/purchase',
    name: 'TicketPurchase',
    component: TicketPurchase,
    meta: { hideContent: true },
  },
  // MEMBERSHIP 메뉴
  {
    path: '/membership/info',
    name: 'MembershipInfo',
    component: MembershipInfo,
    meta: { hideContent: true },
  },
  {
    path: '/membership/purchase',
    name: 'MembershipPurchase',
    component: MembershipPurchase,
    meta: { hideContent: true },
  },
  // MY PAGE 메뉴
  {
    path: '/mypage/reservations',
    name: 'ReservationsView',
    component: ReservationsView,
    meta: { hideContent: true },
  },
  {
    path: '/mypage/membership',
    name: 'MembershipView',
    component: MembershipView,
    meta: { hideContent: true },
  },
  {
    path: '/mypage/edit-profile',
    name: 'EditProfile',
    component: EditProfile,
    meta: { hideContent: true },
  },
  {
    path: '/mypage/delete-account',
    name: 'DeleteAccount',
    component: DeleteAccount,
    meta: { hideContent: true },
  },
  // 로그인, 회원가입 
  {
    path: '/login',
    name: 'loginPage',
    component: loginPage,
    meta: { hideContent: true },
  },
  {
    path: '/signup',
    name: 'SignupPage',
    component: SignupPage,
    meta: { hideContent: true },
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL), // HTML5 히스토리 모드 사용
  routes
});

export default router;