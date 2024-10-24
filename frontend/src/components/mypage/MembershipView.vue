<template>
    <div class="membership-view-page">
      <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
      <LogoHeader />
  
      <!-- 예매 내역, 멤버십 내역, 개인정보 수정, 회원 탈퇴 메뉴 -->
      <div class="menu">
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/reservations' }" @click="navigateTo('/mypage/reservations')">예매 내역</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/membership' }" @click="navigateTo('/mypage/membership')">멤버십 내역</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/edit-profile' }" @click="navigateTo('/mypage/edit-profile')">개인정보 수정</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/delete-account' }" @click="navigateTo('/mypage/delete-account')">회원 탈퇴</div>
      </div>
    
      <!-- 멤버십 내역 페이지 내용 -->
      <div class="membership-view-content">
        <h2>이용 중인 멤버십 결제 내역</h2>
        <div class="membership-info">
          <div class="membership-detail">
            <p><strong>이용 중인 멤버십:</strong> 연간 멤버십</p>
            <p><strong>결제 금액:</strong> 129,000원</p>
            <p><strong>결제 수단:</strong> [**** **** **** 1234] / 일시불</p>
            <p><strong>결제 날짜:</strong> 2022년 11월 7일</p>
          </div>
          <div class="cancel-button-container">
            <button class="cancel-membership-button">결제 취소</button>
          </div>
        </div>
        <p class="membership-note">
          결제 후 콘텐츠 이용 내역이 없을 경우, 결제일로부터 7일 이내에 직접 결제 취소가 가능합니다.
        </p>
      </div>
    </div>
  </template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
      name: 'MembershipView',
      components: {
      LogoHeader
    },    
    data() {
      return {
        activeMenu: this.$route.path // 현재 활성화된 경로
      };
    },
    watch: {
      // 경로가 변경될 때마다 activeMenu를 업데이트
      $route(to) {
        this.activeMenu = to.path;
      }
    },
    methods: {
      navigateTo(route) {
        this.$router.push(route);
        this.activeMenu = route; // 메뉴를 클릭할 때 활성화된 메뉴 업데이트
      }
    }
  }
  </script>
  
  <style>
  .membership-view-page {
      padding-top: var(--header-height);
      padding-bottom: var(--footer-height);
      text-align: center;
  }
  
  /* 메뉴 */
  .menu {
    display: flex;
    justify-content: center;
    gap: 50px;
    padding-bottom: 20px;
  }
  
  .menu-item {
    width: 120px;
    padding: 10px 0;
    position: relative;
    cursor: pointer;
    font-weight: bold;
    color: #565656;
  }
  
  .menu-item::after {
    content: "";
    position: absolute;
    bottom: -5px; /* 밑줄이 텍스트 아래에 표시되도록 간격을 조정 */
    left: 0;
    width: 100%;  /* 밑줄을 전체 div 너비에 맞춤 */
    height: 3px;
    background-color: black;
    transform: scaleX(0); /* 기본적으로 밑줄을 숨김 */
    transition: transform 0.3s ease;
  }
  
  .active-menu-item {
    color: black;  /* 선택된 메뉴의 텍스트 색상을 black으로 설정 */
  }
  
  .active-menu-item::after {
    transform: scaleX(1);  /* 활성화된 메뉴에만 밑줄 표시 */
  }
  
  .menu-item:hover::after {
    transform: scaleX(1);  /* 마우스를 올리면 밑줄 표시 */
  }
  
  /* 멤버십 내역 */
  .membership-info {
    background-color: #f9f9f9;
    padding: 20px;
    margin: 20px auto;
    width: 80%;
    max-width: 600px;
    border-radius: 10px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    text-align: left;
  }

  .membership-detail p {
    margin: 10px 0;
    font-size: 16px;
  }

  .cancel-membership-button {
    background-color: #d9534f;
    color: white;
    border: none;
    padding: 10px 20px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .cancel-button-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }

  .cancel-membership-button {
    background-color: #d9534f;
    color: white;
    border: none;
    padding: 10px 20px;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .cancel-membership-button:hover {
    background-color: #c9302c;
  }

  .membership-note {
    font-size: 14px;
    color: #666;
    margin-top: 10px;
  }
  </style>