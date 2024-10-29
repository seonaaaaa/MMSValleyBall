<template>
  <div class="signup-page">
    <!-- 상단 로고 헤더 -->
    <LogoHeader />

    <!-- 회원가입 폼 -->
    <div class="signup-form">
      <h2>회원가입</h2>
      <form @submit.prevent="handleSignup">
        <div class="form-group">
          <label class="notice">** 필수 입력 항목입니다.</label>
          <!-- 이메일 -->
          <label for="userEmail">**이메일</label>
          <div class="input-with-button">
            <input type="email" id="userEmail" v-model="userEmail" placeholder="이메일을 입력하세요" required />
            <button type="button" @click="checkEmail" class="check-button">중복 확인</button>
          </div>
        </div>

        <!-- 비밀번호 -->
        <div class="form-group">
          <label for="userPassword">**비밀번호</label>
          <input type="password" id="userPassword" v-model="userPassword" required />
        </div>

        <div class="form-group">
          <label for="userConfirmPassword">**비밀번호 확인</label>
          <input type="password" id="userConfirmPassword" v-model="userConfirmPassword" required />
        </div>

        <!-- 이름 -->
        <div class="form-group">
          <label for="userName">**이름</label>
          <input type="text" id="userName" v-model="userName" required />
        </div>

        <!-- 핸드폰 번호 -->
        <div class="form-group">
          <label for="userPhone">전화번호</label>
          <div class="phone-input-group single-line">
            <select v-model="userPhonePart1">
              <option value="010">010</option>
              <option value="011">011</option>
              <option value="016">016</option>
              <option value="017">017</option>
            </select> -
            <input type="tel" v-model="userPhonePart2" maxlength="4" /> -
            <input type="tel" v-model="userPhonePart3" maxlength="4" />
          </div>
        </div>

        <!-- 주소 -->
        <div class="form-group">
          <label for="userAddress">주소</label>
          <div class="input-with-button">
            <input type="text" id="userAddress" v-model="userAddress" placeholder="주소를 입력하세요" readonly required />
            <button type="button" @click="findAddress" class="check-button">주소 찾기</button>
          </div>
        </div>
        <button type="submit" class="submit-button" :class="{ disabled: isSignupDisabled }" :disabled="isSignupDisabled"> 회원가입 </button>
      </form>
    </div>
  </div>
</template>

<script>
import LogoHeader from '../common/LogoHeader.vue';

  export default {
  name: 'SignupPage',
  components: {
    LogoHeader,
  },
  data() {
    return {
      userEmail: '',
      userPassword: '',
      userConfirmPassword: '',
      userName: '',
      userPhonePart1: '010', // 기본값 설정
      userPhonePart2: '',
      userPhonePart3: '',
      userAddress: '',
      isEmailChecked: false,
    };
  },

  watch: {
    userEmail(newVal, oldVal) {
      // 이메일이 변경되면 중복 확인 상태를 초기화 (false)
      if (newVal !== oldVal) {
        this.isEmailChecked = false;
      }
    }
  },

  computed: {
    isSignupDisabled() {
      // 모든 필수 항목이 입력되었는지 검사
      return !(
        this.userEmail &&
        this.userPassword &&
        this.userConfirmPassword &&
        this.userName &&
        this.isEmailChecked
      );
    }
  },

  methods: {
    // 이메일 중복 확인
    checkEmail() {
      const params = new URLSearchParams();
      params.append('userEmail', this.userEmail);

      this.$axios.post("/signup/check/email", null, { params: params })
        .then((response) => {
          console.log(response);
          if (response.data === "True") {
            alert("사용가능한 이메일입니다.");
            this.isEmailChecked = true;
          } else if (response.data === "False") {
            alert("이미 사용 중인 이메일입니다.");
            this.isEmailChecked = false;
          } else {
            alert("이메일을 입력해주세요.");
            this.isEmailChecked = false;
          }
        })
        .catch((error) => {
          alert(error);
        });
    },

    async handleSignup() {
      // 비밀번호 확인
      if (this.userPassword !== this.userConfirmPassword) {
        alert('비밀번호가 일치하지 않습니다. 다시 확인해주세요.');
        return;
      }

      // 전화번호 합쳐서 보내기
      const users = {
        userEmail: this.userEmail,
        userPassword: this.userPassword,
        userName: this.userName,
        userPhone:  `${this.userPhonePart1}-${this.userPhonePart2}-${this.userPhonePart3}`,
        userAddress: this.userAddress,
      };

      console.log(users);

      // 회원가입 요청 보내기
      this.$axios({
        method: "post",
        url: "/signup",
        data: users,
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((response) => {
        console.log(response);
        alert("회원가입 성공");
        this.$router.replace("/");
      })
      .catch((error) => {
        console.error('회원가입 중 오류 발생:', error);
        alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
      });
    },

    findAddress() {
      // 카카오 주소 API 팝업 열기
      if (!window.daum || !window.daum.Postcode) {
        console.error('카카오 주소 API가 로드되지 않았습니다.');
        alert('주소 찾기 기능을 사용할 수 없습니다. 페이지를 새로고침하거나 나중에 다시 시도해주세요.');
        return;
      }

      new window.daum.Postcode({
        oncomplete: (data) => {
          // 팝업에서 검색결과 선택 시 호출되는 콜백 함수
          this.userAddress = data.address; // 사용자가 선택한 주소를 userAddress에 저장
        },
      }).open();
    },
  },

  mounted() {
    // 카카오 주소 API 스크립트 로드
    const script = document.createElement('script');
    script.src = 'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    script.onload = () => {
      console.log('카카오 주소 API 스크립트가 성공적으로 로드되었습니다.');
    };
    script.onerror = () => {
      console.error('카카오 주소 API 스크립트 로드 중 오류 발생');
    };
    document.head.appendChild(script);
  },
}


</script>

<style scoped>
.signup-page {
  padding-top: var(--header-height);
  padding-bottom: var(--footer-height);
  text-align: left;
}

.signup-form {
  max-width: 500px;
  margin: 50px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.signup-form h2 {
  margin-bottom: 40px;
  color: #333;
  text-align: center;
  padding-bottom: 30px;
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

input[type="email"],
input[type="password"],
input[type="tel"],
input[type="text"] {
  margin-top: 10px;
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
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

.submit-button {
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

.submit-button:hover {
  background-color: #3f6f68;
}

.submit-button.disabled {
  background-color: #ccc; /* 연한 그레이 색상 */
  color: #999; /* 글자 색상도 연한 그레이 */
  cursor: not-allowed; /* 비활성화 상태임을 나타내는 커서 */
}


.notice {
  text-align: right;
  margin-bottom: 40px;
  font-size: 20px;
}
</style>
