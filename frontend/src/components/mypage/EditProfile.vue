<template>
  <div class="edit-profile-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />

    <!-- 예매 내역, 나의 멤버십, 나의 정보 수정 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/reservations' }" @click="navigateTo('/mypage/reservations')">예매 내역</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/membership' }" @click="navigateTo('/mypage/membership')">나의 멤버십</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/edit-profile' }" @click="navigateTo('/mypage/edit-profile')">나의 정보 수정</div>
    </div>
  
    <!-- 나의 정보 수정 페이지 내용 -->
    <div class="edit-profile-content">
      <h2 class="edit-profile-content-title">나의 정보 수정</h2>
      <form @submit.prevent="updateProfile">
        <!-- 이메일 -->
        <div class="form-group">
          <label for="userEmail">이메일</label>
          <input type="email" id="userEmail" v-model="userEmail" readonly required/>
        </div>

        <!-- 이름 -->
        <div class="form-group">
          <label for="userName">이름</label>
          <input type="text" id="userName" v-model="userName" readonly required />
        </div>

        <!-- 비밀번호 -->
        <div class="form-group">
          <label for="userPassword">비밀번호</label>
          <input type="password" id="userPassword" v-model="userPassword" required />
        </div>

        <!-- 전화번호 -->
        <div class="form-group">
          <label for="userPhone">전화번호</label>
          <div class="phone-input-group single-line">
            <select v-model="userPhonePart1" required>
              <option value="010">010</option>
              <option value="011">011</option>
              <option value="016">016</option>
              <option value="017">017</option>
            </select> -
            <input type="tel" v-model="userPhonePart2" maxlength="4" required /> -
            <input type="tel" v-model="userPhonePart3" maxlength="4" required />
          </div>
        </div>

        <!-- 주소 -->
        <div class="form-group">
          <label for="userAddress">주소</label>
          <div class="input-with-button">
            <input type="text" id="userAddress" v-model="userAddress" readonly />
            <button type="button" @click="findAddress" class="check-button">주소 찾기</button>
          </div>
        </div>

        <button type="submit" class="alter-button">수정</button>
      </form>
    </div>
  </div>
</template>

<script>
import LogoHeader from '../common/LogoHeader.vue';

export default {
  name: 'EditProfile',
  components: {
    LogoHeader
  },
  data() {
    return {
      activeMenu: this.$route.path, // 현재 활성화된 경로
      userName: '',
      userEmail: '',
      userPassword: '',
      userPhonePart1: '010',
      userPhonePart2: '',
      userPhonePart3: '',
      userAddress: ''
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
    },
    updateProfile() {
      // 개인정보 업데이트 로직 추가 (예: API 호출)
      const userPhone = `${this.userPhonePart1}-${this.userPhonePart2}-${this.userPhonePart3}`;
      console.log('전화번호:', userPhone);
      alert('개인정보가 업데이트되었습니다.');
    },
    findAddress() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          // 팝업에서 검색결과 선택 시 호출되는 콜백 함수
          this.userAddress = data.address;
        }
      }).open();
    }
  },
  mounted() {
    // 카카오 주소 API 스크립트 로드
    const script = document.createElement('script');
    script.src = 'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    document.head.appendChild(script);
  }
}
</script>

<style scoped>
.edit-profile-page {
  padding-top: var(--header-height);
  padding-bottom: var(--footer-height);
  text-align: left;
}

/* 메뉴 */
.menu {
  display: flex;
  justify-content: center;
  gap: 50px;
  padding-bottom: 20px;
}

.menu-item {
  width: 160px;
  padding: 10px 0;
  position: relative;
  cursor: pointer;
  font-weight: bold;
  color: #565656;
  white-space: nowrap;
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

/* 개인정보 수정 */
.edit-profile-content {
  max-width: 500px;
  margin: 50px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-align: left;
}

.form-group {
  margin-bottom: 30px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #555;
}

.input-with-button {
  display: flex;
  justify-content: space-between;
}

.phone-input-group {
  display: flex;
  gap: 5px;
  align-items: center;
}

select,
input[type="tel"],
input[type="email"],
input[type="password"],
input[type="text"] {
  margin-top: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.phone-input-group select {
  width: auto;
}

.phone-input-group input[type="tel"] {
  width: calc(33% - 10px);
}

input[type="email"][readonly],
input[type="text"][readonly]:not(#userAddress) {
  background-color: #f0f0f0;
  color: #888;
}

input[type="email"]:focus,
input[type="password"]:focus,
input[type="tel"]:focus,
input[type="text"]:focus {
  border-color: #4f8578;
  outline: none;
}

.check-button {
  background-color: #4f8578;
  color: white;
  border: none;
  padding: 15px;
  width: 150px;
  margin-left: 10px;
  border-radius: 5px;
  cursor: pointer;
}

.check-button:hover {
  background-color: #3f6f68;
}

.alter-button {
  margin-top: 15px;
  width: 100%;
  padding: 15px;
  background-color: #4f8578;
  color: #ffffff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 30px;
}

.alter-button:hover {
  background-color: #3f6f68;
}

.edit-profile-content-title {
  margin-bottom: 50px;
  text-align: center;
}
</style>
