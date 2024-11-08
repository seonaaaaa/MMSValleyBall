<template>
  <div id="app">
    <AppHeader v-if="showHeaderFooter" @logoutSuccess="handleLogoutSuccess" :isLoggedIn="isLoggedIn"/>
    <router-view :isLoggedIn="isLoggedIn" :balance="Number(balance)" 
    :membership="membership" :toModal="toModal" :modalStatus="modalStatus"
    @loginSuccess="handleLoginSuccess" @logoutSuccess="handleLogoutSuccess" 
    @getBalance="updateBalance" @getMembership="updateMembership" @openMadal="sendingRequestFormModal"
    />
    <AppFooter v-if="showHeaderFooter"/>
  </div>
</template>

<script>
import AppHeader from './components/common/Header.vue'
import AppFooter from './components/common/Footer.vue'
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

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
    let toModal = ref({
      ticketSalesDto: {},
      matchInfo: {},
      availableSeatsList: [],
      userMembership: {},
      seatDTOList: []
    });
    let modalStatus = ref(false);

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

    const sendingRequestFormModal = async (matchId) => {
      sessionStorage.setItem('matchId', matchId);
      try {
        const response = await axios.get('/ticket/purchase/modal', {
            params: {
                matchId: matchId
            }
        });
        toModal.value = response.data;
        console.log(toModal.value);
      } catch (error) {
        console.error("Error fetching data: ", error);
      }
      modalStatus.value = true;
      sessionStorage.setItem('modalStatus', 'true');
    };

    onMounted(() => {
      // 새로고침 시 sessionStorage에 저장된 값을 불러와 설정
      isLoggedIn.value = sessionStorage.getItem('isLoggedIn') === 'true';
      balance.value = Number(sessionStorage.getItem('balance')) || 0;
      membership.value = sessionStorage.getItem('membership') || 'bronze';
      modalStatus.value = sessionStorage.getItem('modalStatus') === 'true';
      // 새로고침 시 모달창이 열려있는지 여부를 판단해서 다시 경기정보 날려주기
      if(modalStatus.value){
        sendingRequestFormModal(sessionStorage.getItem('matchId'));
      }
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
      toModal,
      modalStatus,
      handleLoginSuccess,
      handleLogoutSuccess,
      updateBalance,
      updateMembership,
      sendingRequestFormModal
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
