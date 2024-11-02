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
        saveUserInfo(token) {
            let payload = null;
            try {
            const base64Payload = token.split('.')[1]; // 토큰의 두 번째 부분 (Payload)
            const base64 = base64Payload.replace(/-/g, '+').replace(/_/g, '/'); // Base64 형식을 표준으로 맞춤
            const decodedPayload = decodeURIComponent(
                atob(base64)
                .split('')
                .map((c) => `%${('00' + c.charCodeAt(0).toString(16)).slice(-2)}`)
                .join('')
            );
            payload = JSON.parse(decodedPayload); // JSON 파싱하여 객체로 반환
            } catch (error) {
                console.error('토큰 파싱 실패', error);
            }
            // 유저 정보를 로컬스토리지에 저장
            sessionStorage.setItem('email', payload.username);
            sessionStorage.setItem('name', payload.name);
            sessionStorage.setItem('role', payload.role);
        },
        login(){
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
                console.log(response);
                if (response.status === 401) {
                alert("이메일 혹은 패스워드가 잘못 입력되었습니다.");
                } else {
                let accessToken = response.headers.authorization;  // 응답헤더에서 토큰 받기
                sessionStorage.setItem("token", accessToken); // 토큰 sessionStorage에 저장
                this.saveUserInfo(accessToken); 
                this.$emit('login',true);
                this.$axios.defaults.headers.common[
                    "Authorization"
                ] = `Bearer ${accessToken}`;
                alert("로그인이 되었습니다");
                this.$router.replace("/");
                }
            }).catch(() => {
                alert("로그인 실패!!");
            });
        },
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
 