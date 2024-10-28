<template>
    <div class="delete-account-page">
      <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
      <LogoHeader />
  
      <!-- 예매 내역, 나의 멤버십, 개인정보 수정 -->
      <div class="menu">
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/reservations' }" @click="navigateTo('/mypage/reservations')">예매 내역</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/membership' }" @click="navigateTo('/mypage/membership')">나의 멤버십</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/edit-profile' }" @click="navigateTo('/mypage/edit-profile')">나의 정보 수정</div>
      </div>
    
      <!-- 회원 탈퇴 페이지 내용 -->
      <div class="delete-account-content">
  
  
  
      </div>
    </div>
  </template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
      name: 'DeleteAccount',
      components: {
      LogoHeader
    },    
    data() {
      return {
        activeMenu: this.$route.path // 현재 활성화된 경로
      };
    },
}
</script>

<style>

</style>