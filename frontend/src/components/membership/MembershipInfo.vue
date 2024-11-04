<template>
    <div class="membership-info-page">
      <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
      <LogoHeader />
  
      <!-- 멤버십 안내, 멤버십 구매 메뉴 -->
      <div class="menu">
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/membership/info' }" @click="navigateTo('/membership/info')">멤버십 안내</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/membership/purchase' }" @click="navigateTo('/membership/purchase')">멤버십 구매</div>
      </div>
    
      <!-- 멤버십 안내 페이지 내용 -->
      <div class="membership-info-content">
        <div class="membership-info-layout">
          <div class="membership-season-info">
            <h4>2024/25 시즌 멤버십 안내</h4>
              <table class="membership-info-table">
                <thead>
                  <tr>
                    <th>종류</th>
                    <th>혜택</th>
                    <th>멤버십 기간</th>
                    <th>판매 기간</th>
                    <th>판매 가격</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>GOLD</td>
                    <td>
                        <p>골드전용 좌석 구매</p>
                        <p>경기 7일전 선예매</p>
                        <p>팀 유니폼 증정 (랜덤)</p>
                        <p>팀 굿즈(MMS 고무장갑, 슬로건) 증정</p>
                    </td>
                    <td rowspan="2">2024.8.16<br> - 2025.7.31</td>
                    <td rowspan="2">2024.8.1<br> - 2024.8.8</td>
                    <td>300,000원</td>
                  </tr>
                  <tr>
                    <td>SILVER</td>
                    <td>
                        <p>경기 7일전 선예매</p>
                        <p>팀 굿즈(MMS 고무장갑, 슬로건) 증정</p>
                    </td>
                    <!-- <td>2024.8.16-2025.7.31</td>
                    <td>2024.8.1-2024.8.8</td> -->
                    <td>100,000원</td>
                  </tr>
                  <tr>
                    <td>BRONZE</td>
                    <td>-</td>
                    <td>-</td>
                    <td>-</td>
                    <td>-</td>
                  </tr>
                </tbody>
            </table>
          </div>
          <br>
          <div class="membership-season-goods">
              <h4>2024/25 시즌 굿즈 안내</h4>
              <div class="goods">
                <div class="slogan">
                  <img class="sloganImg" src="@/assets/img/mms/slogun.png">
                  <p>슬로건</p>
                </div>
                <div class="goods-box">
                  <div>
                    <img class="img" src="@/assets/img/mms/mms-ci-uniform.png">
                    <p>유니폼</p>
                  </div>
                  <div>
                    <img class="img" src="@/assets/img/mms/mms_gloves.png">
                    <p>고무장갑</p>
                  </div>
                </div>
              </div>
          </div>
          
          <button class="btn" id="btn-purchase" onclick="window.location.href='http://localhost:8080/membership/purchase';">구매하기</button>
          <br>
        </div>
        
      </div>
    </div>
  </template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
      name: 'MembershipInfo',
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
  
  <style scoped>
  .membership-info-page {
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
  
  /* 멤버십 안내 */
  .membership-info-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  max-width: 1400px; /* 최대 너비를 설정 */
  margin: 0 auto;
}

.membership-info-layout {
  width: 100%; /* 전체 페이지의 70%로 설정 */
  margin-bottom: 30px; /* 아래 여백 추가 */
}

.membership-info-table {
  width: 75%;
  text-align: center;
  margin: auto;
  padding: 0;
  border-collapse: collapse; /* 테두리 겹침 방지 */
}

.membership-info-table th,
.membership-info-table td {
  text-align: center; /* 테이블 셀 가운데 정렬 */
  padding: 10px; /* 셀 내부 여백 */
  border: 1px solid #ddd; /* 테두리 추가 */
}

.membership-season-goods {
    text-align: center;
    margin-bottom: 30px;
}

.goods {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 30px;
}

.slogan {
    margin-bottom: 20px; /* 슬로건과 아래 항목 사이의 간격 */
}

.goods-box {
    display: flex;
    gap: 50px;
    justify-content: center;
}

.goods-box div {
    text-align: center;
}

.img {
    width: 320px;
    height: 280px;
    object-fit: cover;
}

.sloganImg{
  width: 800px;
}

.btn {
  border: none;
  /* 테두리 없음 */
  color: white;
  /* 글자 색 (흰색) */
  padding: 8px 16px;
  /* 버튼 내부 여백 (위아래 12px, 좌우 24px) */
  text-align: center;
  /* 글자 가운데 정렬 */
  text-decoration: none;
  /* 밑줄 없음 */
  display: inline-block;
  /* 기본 인라인 블록 설정 */
  font-size: 22px;
  /* 글자 크기 */
  border-radius: 8px;
  /* 모서리를 둥글게 */
  cursor: pointer;
  /* 마우스를 올렸을 때 포인터 모양 */
  transition: background-color 0.3s;
  /* 배경색이 바뀌는 효과 추가 */
  margin-top: 50px;
  margin-left: 15px;
  margin-right: 15px;
  margin-bottom: 50px;

  width: 100px;
  /* 버튼의 너비 */
  height: 50px;
  /* 버튼의 높이 */
}

#btn-purchase {
  width: 140px;
  background-color: #60a191;  /* 버튼 배경색 (녹색) */
}

#btn-purchase:hover {
  background-color: #4d7e74; /* 마우스를 올렸을 때 배경색 변경 */
}
  </style>