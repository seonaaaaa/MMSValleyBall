<template>
  <div class="edit-profile-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />

    <!-- 예매 내역, 나의 멤버십, 나의 정보 수정 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/reservations' }" @click="navigateTo('/mypage/reservations')">예매 내역</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/membership' }" @click="navigateTo('/mypage/membership')">나의 멤버십</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/edit-profile' }" @click="navigateTo('/mypage/edit-profile')">나의 정보</div>
    </div>

    <!-- 나의 정보 수정 페이지 내용 -->
    <div class="profile-container">
      <div class="profile-header">
        <div class="profile-picture">
          <img :src="membershipImage(userMembership)" alt="멤버십 등급 이미지" />
        </div>
        <div>
          <span class="header-name"><strong>{{ userName }}</strong> 님</span>
        </div>
        <div class="balance-info">
          <span class="header-balance">잔액 : <strong>{{ balance }}</strong>원</span>
        </div>
        <button class="recharge-button" @click="openRechargeWindow">충전하기</button>
      </div>

      <div class="profile-form">
        <div class="form-row"> 
          <div class="form-group">
            <label for="userName">이름</label>
            <input type="text" id="userName" v-model="userName" />
          </div>
          <div class="form-group">
            <label for="userEmail">이메일</label>
            <input type="text" id="userEmail" v-model="userEmail" />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label for="userPassword">비밀번호</label>
            <input type="text" id="userPassword" v-model="userPassword" />
          </div>
          <div class="form-group">
            <label for="confirmPassword">비밀번호 확인</label>
            <input type="text" id="confirmPassword" v-model="confirmPassword" />
          </div>
        </div>
        <div class="form-row" id="userPhone">
          <label for="userPhone">전화번호</label>
          <div class="form-group">
            <select id="userPhone1" v-model="userPhonePart1">
              <option value="010">010</option>
              <option value="011">011</option>
              <option value="016">016</option>
              <option value="017">017</option>
            </select>
          </div><strong>-</strong>
          <div class="form-group">
            <input type="number" id="userPhone2" v-model="userPhonePart2" />
          </div><strong>-</strong>
          <div class="form-group">
            <input type="number" id="userPhone3" v-model="userPhonePart3" />
          </div>
        </div>
        <div class="form-row" id="address">
          <label for="address">주소</label>
          <div class="form-group">
            <input type="text" id="address" v-model="userAddress" @click="findAddress" readonly/>
          </div>
        </div>
        <button class="edit-profile-button">
          <i class="fas fa-pen"></i> 정보 수정하기
        </button>
      </div>
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
  props:{
    balance : Number,
  },
  data() {
    return {
      activeMenu: this.$route.path, // 현재 활성화된 경로
      userName: '홍길동',
      userEmail: 'hong@example.com',
      userPassword: '',
      confirmPassword: '',
      userPhonePart1: '010',
      userPhonePart2: '',
      userPhonePart3: '',
      userAddress: '',
      userMembership: 'bronze',
      passwordVisible: false,
      userId: 0,
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
      if (this.userPassword !== this.confirmPassword) {
        alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
        return;
      }
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
    },
    openRechargeWindow() {
      window.open(`/myPage/recharge?balance=${this.balance}`, '충전하기', 'width=500,height=250,toolbar=no,menubar=no,scrollbars=no,resizable=no');
    },
    withdrawMembership() {
      // 회원 탈퇴 로직 추가 (예: 확인 창 후 API 호출)
      if (confirm('정말로 회원 탈퇴를 진행하시겠습니까?')) {
        alert('회원 탈퇴가 완료되었습니다.');
      }
    },
    setInfo(response){
      this.userName = response.user.userName;
      this.userEmail = response.user.userEmail;
      this.userAddress = response.user.userAddress;
      this.$emit("userbalance", response.balance);
      this.userPhonePart1 = response.user.userPhone.split('-')[0];
      this.userPhonePart2 = response.user.userPhone.split('-')[1];
      this.userPhonePart3 = response.user.userPhone.split('-')[2];
      this.userMembership = response.user.userMembership.split('-')[1];
      this.userId = response.user.userId;
    },
    getUserInfo(){
      this.$axios.get(`/myPage/info`, {
        params: {
          email: sessionStorage.getItem('email')
        },
        headers: {
          Authorization : sessionStorage.getItem('token')
        }
      }).then((response) => {
        this.setInfo(response.data);
      }).catch((error) => {
        console.error('사용자 정보를 가져오는 중 오류 발생:', error);
      });
    },
    membershipImage(userMembership){
      return  require(`@/assets/img/membershipImg/${userMembership}.png`);
    }
  },
  mounted() {
    // 카카오 주소 API 스크립트 로드
    const script = document.createElement('script');
    script.src = 'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    document.head.appendChild(script);
    this.getUserInfo();
  }
}
</script>

<style>
.edit-profile-page {
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
.profile-container {
  border: 1px solid#565656;
  border-radius: 10px;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
  margin-bottom: 50px;
  background-color: #efeae33e;
}
.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding-bottom: 10px;
  margin-bottom: 20px;
  font-size: 25px;
  border-bottom: 1px solid #8b8686;
}
.profile-picture {
  display: flex;
  align-items: center;
}
.profile-picture img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.balance-info{
  margin-left: auto;
  text-align: center;
  justify-content: center;
}
.header-balance {
  font-size: 20px;
  margin-bottom: 10px; /* 잔액과 버튼 간의 간격 추가 */
}
.recharge-button{
  padding: 5px 18px;
  background-color: #e0a825;

}
.recharge-button,
.edit-profile-button {
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.edit-profile-button{
  margin-left: auto;
  padding: 10px 20px;
  width: 18%;
  background-color: #000;
}
.profile-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.form-row {
  align-items: center;
  display: flex;
  gap: 20px;
}
.form-group {
  flex: 1;
  display: flex;
  flex-direction: column;
}
label {
  text-align: left;
  justify-content: center;
  margin-bottom: 5px;
  font-weight: bold
}

label[for="address"],
label[for="userPhone"]{
  width: 10%;
}

input {
  padding: 10px;
  border: 1px solid #000;
  border-radius: 5px;
}
#userPhone1{
  padding: 10px;
  border: 1px solid #000;
  border-radius: 5px;
}
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}
#address, #userPhone{
  margin-top: 10px;
}
</style>