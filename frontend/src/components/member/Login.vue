<template>
    <div class="login-page">
      <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
      <LogoHeader />
      
      <!-- 로그인 폼 -->
      <div class="login-form">
             <h2>로그인</h2>
             <form @submit.prevent="login">
                 <div class="form-group">
                     <label for="userEmail">사용자 이메일</label>
                     <input type="email" id="userEmail" v-model="userEmail" required placeholder="ex) abc@aaa.com"/>
                 </div>
                 <div class="form-group">
                     <label for="userPassword">비밀번호</label>
                     <input type="Password" id="userPassword" v-model="userPassword" required placeholder="비밀번호를 입력해주세요" />
                 </div>
                 <button type="submit" class="submit-button">로그인</button>
             </form>
             <div class="links">
                 <router-link to="/signup">회원가입</router-link>
             </div>
         </div>
       </div>

 </template>
 
 <script>
 import LogoHeader from '../common/LogoHeader.vue';
 
 export default {
     name: 'LoginPage',
     components: {
         LogoHeader
     },
     data() {
         return {
            userEmail: '', // 이메일을 위한 데이터
            userPassword: '', // 비밀번호를 위한 데이터
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
        },
        login(){
            // 회원 status 조회
            const params = new URLSearchParams();
            params.append('userEmail', this.userEmail);
            this.$axios.post("/login/check-status", null, { params: params })
            .then((response) => {
                if(response.data === 'INACTIVE'){
                    alert("탈퇴한 회원입니다.");
                }else{
                const user = {
                    email: this.userEmail,
                    password: this.userPassword,
                };
            this.$axios({
                method: "post",
                url: "/login",
                data: JSON.stringify(user),
                headers: {
                "Content-Type": "application/json",
                },
            }).then((response) => {
                if (response.status === 401) {
                    alert("이메일 혹은 패스워드가 잘못 입력되었습니다.");
                } else {
                let accessToken = response.headers.authorization;  // 응답헤더에서 토큰 받기
                sessionStorage.setItem("token", accessToken); // 토큰 sessionStorage에 저장
                this.$emit('loginSuccess',accessToken);
                alert("로그인이 되었습니다");
                const redirectTo = this.$route.query.from || '/';
                this.$router.push(redirectTo); // 원래 페이지로 리다이렉트
                }
            }).catch(() => {
                alert("로그인 실패!!");
            });
            }
        })
        .catch((error) => {
          alert(error);
        });
    }
    }
 }
 </script>
 
 <style scoped>
 .login-page {
     padding-top: var(--header-height);
     padding-bottom: var(--footer-height);
     text-align: center;
 }
 
 .login-form {
     max-width: 400px; /* 폼 최대 너비 */
     margin: 50px auto; 
     padding: 20px; 
     background-color: #f9f9f9; 
     border-radius: 10px;
     border: 2px solid #60a191;
     box-shadow: 0 3px 10px rgba(0, 0, 0, 0.1); 
 }
 
 .login-form h2 {
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
 
 input[type="email"],
 input[type="password"] {
     width: 100%; 
     padding: 10px; 
     border: 1px solid #ccc; 
     border-radius: 5px; 
 }
 
 input[type="email"]:focus,
 input[type="password"]:focus {
     border-color: #4f8578; 
     outline: none; 
 }
 
 .submit-button {
    margin-top: 15px;
     width: 100%;
     padding: 10px; 
     background-color: #4f8578; 
     color: #ffffff; 
     border: none; 
     border-radius: 5px; 
     cursor: pointer; 
     font-size: 25px; 
 }
 
 .submit-button:hover {
     background-color: #3f6f68; 
 }
 
 .links {
     margin-top: 30px; 
 }
 
 .links a {
     color: #4f8578; 
     text-decoration: none; 
 }
 
 .links a:hover {
     text-decoration: underline; 
 }
 </style>