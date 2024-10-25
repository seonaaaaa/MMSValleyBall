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
            <label for="email">**이메일</label>
            <div class="input-with-button">
              <input type="email" id="email" v-model="email" placeholder="이메일을 입력하세요" required />
              <button type="button" @click="checkEmail" class="check-button">중복 확인</button>
            </div>
          </div>
  
          <div class="form-group">
            <label for="password">**비밀번호</label>
            <input type="password" id="password" v-model="password" required />
          </div>
  
          <div class="form-group">
            <label for="confirmPassword">**비밀번호 확인</label>
            <input type="password" id="confirmPassword" v-model="confirmPassword" required />
          </div>
  
          <div class="form-group">
            <label for="name">**이름</label>
            <input type="text" id="name" v-model="name" required />
          </div>
  
          <!-- 핸드폰 번호 -->
          <div class="form-group">
            <label for="phoneNumber">핸드폰 번호</label>
            <input type="tel" id="phoneNumber" v-model="phoneNumber" placeholder="010-1234-5678" required />
          </div>
  
          <!-- 주소 -->
          <div class="form-group">
            <label for="address">주소</label>
            <div class="input-with-button">
              <input type="text" id="address" v-model="address" placeholder="주소를 입력하세요" readonly required />
              <button type="button" class="check-button">주소 찾기</button>
            </div>
          </div>
          <button type="submit" class="submit-button">회원가입</button>
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
        email: '',
        password: '',
        confirmPassword: '',
        name: '',
        phoneNumber: '',
        address: '',
      };
    },
    methods: {
      handleSignup() {
        console.log('회원가입 시도:', this.email, this.password, this.confirmPassword, this.name, this.phoneNumber, this.address);
      },
      checkEmail() {
        console.log('이메일 중복 확인:', this.email);
      }
    },
  };
  </script>
  
  <style>
  .signup-page {
    padding-top: var(--header-height);
    padding-bottom: var(--footer-height);
    text-align: left;
  }

  .signup-form h2 {
  margin-bottom: 40px;
  color: #333;
  text-align: center;
  padding-bottom: 30px;
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
    margin-bottom: 30px;
    color: #333;
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

  .notice{
    text-align: right;
    margin-bottom: 40px;
    font-size: 20px;
  }
  </style>
  