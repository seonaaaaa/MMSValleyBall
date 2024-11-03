<template>
  <div class="membership-purchase-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />

    <!-- 멤버십 안내, 멤버십 구매 메뉴 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/membership/info' }" @click="navigateTo('/membership/info')">멤버십 안내</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/membership/purchase' }" @click="navigateTo('/membership/purchase')">멤버십 구매</div>
    </div>
  
    <!-- 멤버십 구매 페이지 내용 -->
    <div class="membership-purchase-content">
      <p class="sub-title">2024/25 시즌 멤버십 구매</p>
      <div class="membership-info-layout">
        <table class="membership-info-table">
          <thead>
            <tr>
              <th>종류</th>
              <th>혜택</th>
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
              <td>300,000원</td>
            </tr>
            <tr>
              <td>SILVER</td>
              <td>
                  <p>경기 7일전 선예매</p>
                  <p>팀 굿즈(MMS 고무장갑, 슬로건) 증정</p>
              </td>
              <td>100,000원</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-if="membership=='bronze'" class="membership-purchase-box">
        <!-- 멤버십 선택 -->
        <div class="membership-selection">
          <p class="sub-title">멤버십 선택</p>
          <select class="select-form" v-model="selectedMembership">
            <option value="">선택</option>
            <option value="silver-100000">SILVER</option>
            <option value="gold-300000">GOLD</option>
          </select>
        </div>
        <div class="charge">
            <div class="charge-money">
              <p>충전금액 : </p>
              <p>{{ new Intl.NumberFormat('ko-KR').format(balance) }} 원</p>
            </div>
            <button class="btn" id="btn-charge" v-if="leftMoney < 0" @click="openRechargeWindow">충전하기</button>
            <div class="membership-price">
              <p>결제금액</p>
              <p>- {{ paymenet }} 원</p>
            </div>
            <div class="left-money">
                <p>결제 후 잔액</p>
                <!-- leftMoney 값이 0보다 작으면 'negative' 클래스를 추가 -->
                <p :class="{ 'negative': leftMoney < 0 }">{{ formattedLeftMoney }} 원</p>
            </div>
        </div>
        <div class="buttons">
          <button class="btn" id="btn-purchase" @click="purchaseMembership" v-if="leftMoney >= 0">구매하기</button>
        </div>
      </div>
      <div v-else>
        이미 이용중인 멤버십이 있습니다.
        <button class="myMembership">나의 멤버십</button>
      </div>
    </div>
  </div>
</template>

<script>
import LogoHeader from '../common/LogoHeader.vue';
import axios from 'axios';
export default {
  name: 'MembershipPurchase',
  components: {
    LogoHeader
  },    
  props: {
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
      // 서버에서 데이터 받아오기
      selectedMembership: '',   // 선택된 멤버십 가격
    };
  },
  watch: {
    // 경로가 변경될 때마다 activeMenu를 업데이트
    $route(to) {
      this.activeMenu = to.path;
    }
  },
  computed: {
    paymenet(){
      if(this.selectedMembership!=''){
        return Number(this.selectedMembership.split('-')[1]).toLocaleString('ko-KR');
      }
      return 0;
    },
    leftMoney() {
      return this.balance - Number(this.selectedMembership.split('-')[1]);
    },
    formattedLeftMoney(){
      if(this.selectedMembership!=''){
        return this.leftMoney.toLocaleString('ko-KR');
      }
      return this.balance.toLocaleString('ko-KR');
    }
  },
  methods: {
    navigateTo(route) {
      this.$router.push(route);
      this.activeMenu = route; // 메뉴를 클릭할 때 활성화된 메뉴 업데이트
    },
    // 구매하기 버튼 클릭 시 호출되는 메서드
    async purchaseMembership() {
      let purchaseInfo ={
        email: sessionStorage.getItem('email'),
        membership: this.selectedMembership.split('-')[0]
      }
      try {
          await axios.post("/membership/purchase", purchaseInfo);
          this.$emit('getMembership', this.selectedMembership.split('-')[0]);
          this.$emit('getBalance', this.leftMoney);
          alert('멤버십 구매 성공');
          this.$router.push({ path: '/myPage/membership' }); // 마이페이지로 리다이렉트
      } catch (error) {
          const errorMessage = error.response ? error.response.data : error.message;
          console.error('Error purchasing membership:', errorMessage); // 에러 메시지 로깅
          alert('멤버십 구매 실패: ' + errorMessage); // 사용자에게 구체적인 에러 메시지 표시
      }
    },
    openRechargeWindow() {
      const width = 570;
      const height = 275;
      const left = (window.screen.width / 2) - (width / 2); // 화면 중앙에 위치
      const top = (window.screen.height / 2) - (height / 2);
      window.open(`/myPage/recharge`, '충전하기', 
      `width=${width},height=${height},,top=${top},left=${left},
      toolbar=no,menubar=no,scrollbars=no,resizable=no,fullscreen=no`);
    },
  }
};
</script>

<style>
.membership-purchase-page {
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

/* 멤버십 구매 */
.sub-title {
font-size: 20px;
font-weight: 700;
}

.membership-selection p {
font-size: 18px;
}

.membership-purchase-content {
display: flex;
flex-direction: column;
align-items: center;
justify-content: center;
width: 100%;
margin: 0 auto;
}

.membership-info-layout {
width: 80%; /* 전체 페이지의 50%로 설정 */
margin-bottom: 30px; /* 아래 여백 추가 */
}

.membership-info-table {
font-size: 21;
width: 70%;
text-align: center;
margin: auto;
padding: 0;
border-collapse: collapse; /* 테두리 겹침 방지 */
}

th, td {
text-align: center; /* 테이블 셀 가운데 정렬 */
padding: 10px; /* 셀 내부 여백 */
border: 1px solid #d8d8d8; /* 테두리 추가 */
}

.membership-info-table th {
background-color: #d2e5d0;
color: rgb(7, 7, 7);
}

.membership-info-table td {
background-color: #eef1ee;
}
.membership-purchase-box{
  width: 70%;
}
.membership-selection {
display: flex;
margin-top: 0px;
margin-bottom: 30px;
gap: 30px;
text-align: center;
width: 40%;
margin-left: 0px;
justify-content: center;
align-items: center;
}

.select-form {
width: 20%;
text-align: center;
font-size: 16px;
padding: 10px;
border-radius: 10px;
}

.charge {
  border: 1.5px solid #999999;
  padding: 30px;
  width: 50%; /* 전체 페이지의 50%로 설정 */
  display: flex;
  flex-direction: row;
  align-items: center;
  font-family: Arial, sans-serif;
  border-radius: 15px;
}

.charge-money {
  display: flex;
  justify-content: space-between;
  width: 100%;
  align-items: center;
  margin-bottom: 25px;
  align-items: flex-start; /* 텍스트와 버튼이 상단에 맞춰지도록 설정 */
}

.charge-money p {
  margin: 0;
  font-weight: bold;
}

.membership-price, .left-money {
  border: 1px solid #BCE067;
  padding-left: 15px;
  padding-right: 15px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin: 5px 0;
  border-radius: 10px;
}
.membership-price{
  margin-right: 20px;
}

.left-money{
background-color: #E4F7BA;
border: none;
}
.negative {
  color: red; /* 잔액이 0보다 작을 때 빨간색으로 표시 */
  font-weight: 900;
}

.buttons{
margin-top: 30px;
margin-bottom: 50px;
gap: 20px;
}
/* 버튼 css */
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
background-color: #4CAF50;  /* 버튼 배경색 (녹색) */
}

#btn-purchase:hover {
background-color: #45a049; /* 마우스를 올렸을 때 배경색 변경 */
}

#btn-charge {
width: 80px;
height: 35px;
font-size: 15px;
background-color: #FFBB00;
margin-bottom: 0px;
padding: 4px 8px;
margin: 10px;
margin-top: -15px;
border-radius: 10px;
margin-left: auto;
}

#btn-charge:hover {
background-color: #EDA900; /* 마우스를 올렸을 때 배경색 변경 */
}

#btn-cancel {
background-color: #A6A6A6;  /* 버튼 배경색 (녹색) */
}


</style>