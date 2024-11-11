<template>
  <footer>
    <div class="footer-container">
      <div class="logo">
          <!-- <router-link to="/">
            <img src="@/assets/logo-mms.png" alt="Team Logo">
          </router-link> -->
          <a href="/#main-page"><img src="@/assets/logo-mms.png" alt="Team Logo"></a>
      </div>
      <div class="footer-content">
        <ul class="footer-menu">
          <li><a href="#">개인정보 처리방침</a></li>
          <li><a href="#">서비스 이용약관</a></li>
          <li><a href="#">운영정책</a></li>
          <li><a href="#">이메일 무단수집거부</a></li>
          <li><a href="/team/stadium#directions-map">오시는 길</a></li>
        </ul>
        <ul class="mms-info">
          <li>대표이사 : 김형민 | 사업등록번호 : 220-26-78113 | 주소 : 서울 종로구 인사동길 12 대일빌딩 7층, 15층</li>
          <li>대표전화 : 070-7544-7000 | FAX : 062-451-1642 | 이메일 : mmsclub@mms.co.kr</li>
          <p>© 2024 GS MMS Volleyball Team. All rights reserved.</p>
        </ul>
      </div>
      <div class="sns-area">
          <ul>
            <li class="sns-icon">
              <a href="#" class="link-sns"><img class="sns-icon-img" src="@/assets/img/common/footer-instagram.svg" alt="Sns Logo"></a>
            </li>
            <li class="sns-icon">
              <a href="#" class="link-sns"><img class="sns-icon-img" src="@/assets/img/common/footer-twitter.svg" alt="Sns Logo"></a>
            </li>
            <li class="sns-icon">
              <a href="#" class="link-sns"><img class="sns-icon-img" src="@/assets/img/common/footer-facebook.svg" alt="Sns Logo"></a>
            </li>
          </ul>
        </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'AppFooter',
}
</script>

<style scoped>
footer {
  background-color: #333;
  color: #fff;
  text-align: center;
  padding: 20px;
  margin: 0;
  width: 100%;
  /* overflow: hidden; */
}

.footer-content {
  margin: auto;
  /* padding: 0 20px; */
}

.footer-container {
  /* flex-wrap: wrap; */
  display: flex;
  justify-content: space-around;
  align-items: center;
  /* width: 100%; */
  max-width: 1140px;
  margin: 0 auto;
}

.logo img {
  height: 150px;
  margin: 0 30px;
}

.footer-menu {
  display: flex;
  justify-content: space-between;
  padding: 0;
  margin-left: 20px;
  margin-right: 20px;
}

.footer-menu li a {
  color: #fff;
  font-weight: bold;
}

.footer-menu li a:hover {
  color: #8a8888;
}

.mms-info {
  color: #8a8888;
  text-align: left;
  padding: 0;
  margin-left: 20px;
  margin-right: 20px;
}

.mms-info p {
  margin-top: 10px;
  color:#8a8888
}

.sns-area {
  margin: 0 30px;
}

.sns-area ul {
  display: flex;
  padding: 0;
  margin: 0;
  gap: 30px;
}

.sns-icon{
  width: 40px;
  height: 40px;
  background-color: #565656;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.sns-icon-img {
  width: 65%;
  height: auto;
  padding: 4px 0 0 0;
  /* svg 컬러 변경 */
  /* #8A8888 */
  filter: invert(63%) sepia(1%) saturate(1299%) hue-rotate(12deg) brightness(87%) contrast(90%);
}

@media (max-width:992px){
  .footer-container {
    display: flex;
    justify-content: space-evenly;
    align-items: center;
  }

  .logo {
    text-align: center;
  }

  .footer-menu{
    display: block;
    text-align:center;
  }

  .mms-info{
    display:none;
  }
}

@media (max-width:600px){
  .footer-menu{
    display: none;
  }
}
</style>