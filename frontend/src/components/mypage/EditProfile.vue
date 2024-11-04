<template>
  <div class="edit-profile-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />

    <!-- 예매 내역, 나의 멤버십, 나의 정보 수정 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/myPage/reservations' }" @click="navigateTo('/myPage/reservations')">예매 내역</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/myPage/membership' }" @click="navigateTo('/myPage/membership')">나의 멤버십</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/myPage/edit-profile' }" @click="navigateTo('/myPage/edit-profile')">나의 정보</div>
    </div>

    <!-- 나의 정보 수정 페이지 내용 -->
    <div class="profile-container">
      <div class="profile-header">
        <div class="profile-picture">
          <img :src="membershipImage()" alt="멤버십 등급 이미지" />
        </div>
        <div>
          <span class="header-name"><strong>{{ userName }}</strong> 님</span>
        </div>
        <div class="balance-info">
          <span class="header-balance">잔액 : <strong>{{ new Intl.NumberFormat('ko-KR').format(balance) }}</strong>원</span>
          <button class="recharge-button" @click="openRechargeWindow">충전하기</button>
        </div>
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
            <input :type="passwordVisible ? 'text' : 'password'" id="userPassword" v-model="userPassword"/>
            <font-awesome-icon
              :icon="passwordVisible ? ['fas', 'eye-slash'] : ['fas', 'eye']"
              @click="togglePasswordVisibility"
              class="toggle-icon"/>
          </div>
          <div class="form-group">
            <label for="confirmPassword">비밀번호 확인</label>
            <input type="password" id="confirmPassword" v-model="confirmPassword" />
          </div>
        </div>
        <div class="btn-box">
          <label for="btn-password-change" class="notMatch" v-if="!isPasswordMatched">비밀번호가 일치하지 않습니다.</label>
          <button class="btn-password-change" :class="{ disabled: !userPassword || !isPasswordMatched}"
          :disabled="!isPasswordMatched || !userPassword" @click="updatePassword">비밀번호 변경하기</button>
        </div>
        <hr style="border: 1px solid #60a19175; width: 100%;">
        <div class="form-row" id="userPhone">
          <label for="userPhone">전화번호</label>
          <div class="form-group" id="phone">
            <select id="userPhone1" v-model="userPhonePart1">
              <option value="010">010</option>
              <option value="011">011</option>
              <option value="016">016</option>
              <option value="017">017</option>
            </select>
          </div><strong>-</strong>
          <div class="form-group">
            <input type="tel" id="userPhone2" v-model="userPhonePart2" maxlength="4" pattern="[0-9]{3,4}"/>
          </div><strong>-</strong>
          <div class="form-group">
            <input type="tel" id="userPhone3" v-model="userPhonePart3" maxlength="4" pattern="[0-9]{4}"/>
          </div>
          <button v-if="isPhoneChanged && !isPhoneVerified" class="btn-phone-check" :class="{ disabled: !isValid}"
          :disabled="!isValid" @click="verifyPhone">인증하기</button>
          <span v-if="isPhoneVerified || !isPhoneChanged" class="phoneChecked">인증완료</span>
        </div>
        <div class="form-row">
          <label for="address">주소</label>
          <div class="form-group">
            <input type="text" id="address" v-model="userAddress" @click="findAddress" readonly/>
          </div>
        </div>
        <div class="bottom-button-box">
          <button @click="deleteAcount" class="delete-account-button">회원탈퇴</button>
          <button class="edit-profile-button" :class="{ disabled:isChanged}" :disabled="isChanged"
          @click="updateProfile">정보 수정하기</button>
        </div>
      </div>
    </div>
    <div>
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
    balance: {
      type: Number,
      required: true
    },
    membership: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      activeMenu: this.$route.path, // 현재 활성화된 경로
      userName: '',
      userEmail: '',
      userPassword: '',
      confirmPassword: '',
      userPhonePart1: '010',
      userPhonePart2: '',
      userPhonePart3: '',
      userAddress: '',
      passwordVisible: false,
      confirmPasswordVisible: false,
      isPhoneChanged: false,
      isPhoneVerified: true,
      isAddressChanged: false,
    };
  },
  computed: {
    isPasswordMatched() {
      return this.userPassword === this.confirmPassword;
    },
    changePhone(){
      return `${this.userPhonePart1}-${this.userPhonePart2}-${this.userPhonePart3}`;
    },
    isValid(){
      return this.changePhone.length>11;
    },
    isChanged() {
      return !((this.isAddressChanged || this.isPhoneChanged) && this.isPhoneVerified);
    },
  },
  watch: {
    // 경로가 변경될 때마다 activeMenu를 업데이트
    $route(to) {
      this.activeMenu = to.path;
    },
    changePhone(newPhone) {
      const storedPhone = sessionStorage.getItem('phone');
      if (storedPhone !== newPhone) {
        this.isPhoneChanged = true;
        this.isPhoneVerified = false; // 전화번호가 변경되었으므로 인증을 무효화
      } else {
        this.isPhoneChanged = false;
        this.isPhoneVerified =true;
      }
    },
    userAddress(newAddress) {
    const storedAddress = sessionStorage.getItem('address');
      if (storedAddress !== newAddress) {
        this.isAddressChanged = true;
      } else {
        this.isAddressChanged = false;
      }
    },
  },
  methods: {
    navigateTo(route) {
      this.$router.push(route);
      this.activeMenu = route; // 메뉴를 클릭할 때 활성화된 메뉴 업데이트
      sessionStorage.removeItem('phone');
      sessionStorage.removeItem('address');

    },
    onPhoneChange() {
      this.isPhoneVerified = false;
    },
    verifyPhone(){
      const changePhone = `${this.userPhonePart1}-${this.userPhonePart2}-${this.userPhonePart3}`;
      this.$axios.post(`/myPage/info/phone`, {
            userEmail: sessionStorage.getItem('email'),
            userPhone: changePhone
        }).then((response) => {
          this.isPhoneVerified = response.data;
          if(response.data){
            alert('인증되었습니다.')
          }else{
            alert('이미 존재하는 번호입니다.')
          }
        }).catch((error) => {
          console.error('전화번호 인증 중 오류 발생:', error);
        });
    },
    updatePassword() {
      if(window.confirm("비밀번호를 변경하시겠습니까?")){
        this.$axios.post(`/myPage/info/modify`, {
            userEmail: sessionStorage.getItem('email'),
            userPassword: this.userPassword
        }).then((response) => {
          alert(response.data);
        }).catch((error) => {
          console.error('비밀번호 변경 중 오류 발생:', error);
        });
      }
    },
    updateProfile() {
      if(window.confirm("변경하시겠습니까?")){
        this.$axios.post(`/myPage/info/modify`, {
            userEmail: sessionStorage.getItem('email'),
            userPhone: this.changePhone,
            userAddress: this.userAddress
        }).then((response) => {
          sessionStorage.setItem('address',this.userAddress);
          sessionStorage.setItem('phone',this.changePhone);
          alert(response.data);
        }).catch((error) => {
          console.error('사용자 정보 업데이트 중 오류 발생:', error);
        });
      }
    },
    deleteAcount() {
      if(window.confirm('정말로 회원 탈퇴를 진행하시겠습니까?')){
        this.$axios.post(`/myPage/info/deactivate`, {
            userEmail: sessionStorage.getItem('email'),
        }).then((response) => {
      // 로컬스토리지에 저장된 토큰과 사용자 정보 삭제
          sessionStorage.removeItem('token');
          sessionStorage.removeItem('name');
          sessionStorage.removeItem('email');
          sessionStorage.removeItem('role');
          const token = sessionStorage.getItem('token');
          if (token === null) {
            console.log('토큰이 성공적으로 삭제되었습니다.');
            this.$emit('logoutSuccess');
          } else {
            console.log('Content에서 토큰 삭제에 실패했습니다.', token);
          }
          this.$router.push('/');
          alert(response.data);
        }).catch((error) => {
          console.error('회원탈퇴 중 오류 발생:', error);
        });
      }
    },
    togglePasswordVisibility() {
      this.passwordVisible = !this.passwordVisible;
    },
    findAddress() {
      new window.daum.Postcode({
        oncomplete: (data) => {
          this.userAddress = data.address;
        }
      }).open();
    },
    openRechargeWindow() {
      const width = 570;
      const height = 275;
      const left = (window.screen.width / 2) - (width / 2); // 화면 중앙에 위치
      const top = (window.screen.height / 2) - (height / 2);
      window.open('/myPage/rechargee', '충전하기',
      `width=${width},height=${height},,top=${top},left=${left},
      toolbar=no,menubar=no,scrollbars=no,resizable=no,fullscreen=no`);
    },
    withdrawMembership() {
      // 회원 탈퇴 로직 추가 (예: 확인 창 후 API 호출)
      if (confirm('정말로 회원 탈퇴를 진행하시겠습니까?')) {
        alert('회원 탈퇴가 완료되었습니다.');
      }
    },
    setInfo(response){
      console.log(response);
      this.userAddress = response.user.userAddress;
      sessionStorage.setItem('address', response.user.userAddress);
      this.$emit("getBalance", response.balance);
      this.$emit("getMembership", response.user.userMembershipName.split('-')[1]);
      this.userPhonePart1 = response.user.userPhone.split('-')[0];
      this.userPhonePart2 = response.user.userPhone.split('-')[1];
      this.userPhonePart3 = response.user.userPhone.split('-')[2];
      sessionStorage.setItem('phone', response.user.userPhone);
    },
    getUserInfo(){
      this.$axios.get(`/myPage/info`, {
        params: {
          email: sessionStorage.getItem('email')
        }
      }).then((response) => {
        this.setInfo(response.data);
      }).catch((error) => {
        console.error('사용자 정보를 가져오는 중 오류 발생:', error);
      });
    },
    membershipImage(){
      return  require(`@/assets/img/membershipImg/${this.membership}.png`);
    }
  },
  mounted() {
    this.userEmail = sessionStorage.getItem('email');
    this.userName = sessionStorage.getItem('name');
    // 카카오 주소 API 스크립트 로드
    const script = document.createElement('script');
    script.src = 'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    document.head.appendChild(script);
    this.getUserInfo();
  }
}
</script>

<style scoped>
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
  border: 2px solid#60a191cc;
  border-radius: 10px;
  max-width: 650px;
  margin: 0 auto;
  box-sizing: border-box;
  margin-bottom: 50px;
  background-color: #e6ebe567;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.profile-header {
  background-color: #d2e5d07e;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  font-size: 27px;
  border-bottom: 2px solid #60a191;
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
  background-color: #528b7e46;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 20px;
  padding: 10px 15px;
  color: #355a51;
}
.header-balance {
  font-size: 24px;
  margin-bottom: 10px;
  margin-right: 10px;
}
.recharge-button{
  font-size: 16px;
  border: none;
  border-radius: 15px;
  cursor: pointer;
  padding: 4px 10px;
  margin-bottom: 6px;
  color: white;
  background-color: #a5d04fb0;
}
.recharge-button:hover{
  background-color: #90b643;
}
.btn-box{
  margin-left: auto;
}
.btn-password-change{
  margin-top: 5px;
}
.btn-password-change:disabled,
.edit-profile-button:disabled,
.btn-password-change:disabled:hover,
.edit-profile-button:disabled:hover{
  background-color: #d5d4d4;
}

.btn-phone-check{
  font-size: 16px;
  padding: 10px 20px;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #c46f6f97;
}
.btn-phone-check:hover{
  background-color: #c46f6ff1;
}
.btn-password-change,
.edit-profile-button {
  font-size: 16px;
  padding: 10px 20px;
  color: #fff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color: #60a19197;
}
.delete-account-button{
  font-size: 16px;
  padding-left: 0px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  background-color:rgba(245, 245, 245, 0);
  color: rgb(193, 190, 190);
  width: 80px;
  font-size: smaller;
}
.delete-account-button:hover{
  color: red;
}

.edit-profile-button{
  margin-top: -10px;
  margin-right: 0;
}
.btn-password-change:hover,
.edit-profile-button:hover{
  background-color: #4f8578;
}
.bottom-button-box{
  display: flex;
  justify-content: space-between;
}
.phoneChecked{
  font-size: 16px;
  padding: 10px 20px;
  color: #fff;
  border: none;
  border-radius: 5px;
  background-color: #d5d4d4;
}
.profile-form {
  padding: 0px 20px 20px 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.form-row {
  width: 100%;
  align-items: center;
  display: flex;
  gap: 20px;
  margin-right: auto;
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

label[ for="confirmPassword"]{
  overflow: visible;
  white-space: nowrap;
}
label[ for="userPhone"],
label[ for="address"]{
  width: 70px;
}
.toggle-icon {
  margin-left: auto;
  margin-right: 10px;
  margin-top: -32px;
  cursor: pointer;
  width: 20px;
  height: 20px;
  color: rgba(0, 0, 0, 0.381);
}

.notMatch{
  color: rgb(215, 94, 94);
  font-weight: 100;
  margin-left: auto;
  margin-right: 10px;
}

#confirmPassword, #userPassword{
  width: 100%;
  padding: 10px;
  padding-right: 35px;
  box-sizing: border-box;
  border: 1px solid #4f85799c;
  background-color: #ffffffda;
  border-radius: 5px;
  font-size: 16px;
}
#userName, #userEmail, #address {
  width: 100%;
  padding: 10px;
  border: 1px solid #4f85799c;
  border-radius: 5px;
  font-size: medium;
  background-color: #ffffffda;
}

#userPhone1,#userPhone2,#userPhone3{
  width: 100%;
  padding: 10px;
  border: 1px solid #2e4e477c;
  border-radius: 5px;
  text-align: center;
  font-size: medium;
}
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}
</style>