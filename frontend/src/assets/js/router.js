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
  },

  // MMS 메뉴
  {
    path: '/mms',
    name: 'MMS',
    component: AppMMS,
  },
  {
    path: '/mms/greeting',
    name: 'Greeting',
    component: AppGreeting,
  },
  {
    path: '/mms/history',
    name: 'History',
    component: AppHistory,
  },
  {
    path: '/mms/ci',
    name: 'CI',
    component: AppCI,
  },
  {
    path: '/mms/sponsor',
    name: 'Sponsor',
    component: AppSponsor,
  },
  // TEAM 메뉴
  {
    path: '/team/players',
    name: 'Players',
    component: AppPlayers,
  },
  {
    path: '/team/staff',
    name: 'Staff',
    component: AppStaff,
  },
  {
    path: '/team/stadium',
    name: 'Stadium',
    component: AppStadium,
  },
   // GAME 메뉴
  {
    path: '/game/schedule',
    name: 'Schedule',
    component: AppSchedule,
  },
  {
    path: '/game/results',
    name: 'Results',
    component: AppResults,
  },
  // TICKET 메뉴
  {
    path: '/ticket/info',
    name: 'TicketInfo',
    component: TicketInfo,
  },
  {
    path: '/ticket/purchase',
    name: 'TicketPurchase',
    component: TicketPurchase,
  },
  // MEMBERSHIP 메뉴
  {
    path: '/membership/info',
    name: 'MembershipInfo',
    component: MembershipInfo,
  },
  {
    path: '/membership/purchase',
    name: 'MembershipPurchase',
    component: MembershipPurchase,
  },
  // MY PAGE 메뉴
  {
    path: '/myPage/reservations',
    name: 'ReservationsView',
    component: ReservationsView,
  },
  {
    path: '/myPage/membership',
    name: 'MembershipView',
    component: MembershipView,
  },
  {
    path: '/myPage/edit-profile',
    name: 'EditProfile',
    component: EditProfile,
  },
  {
    path: '/myPage/delete-account',
    name: 'DeleteAccount',
    component: DeleteAccount,
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