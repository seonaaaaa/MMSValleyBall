<template>
  <div class="membership-view-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />

    <!-- 예매 내역, 나의 멤버십, 개인정보 수정 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/myPage/reservations' }" @click="navigateTo('/myPage/reservations')">예매 내역</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/myPage/membership' }" @click="navigateTo('/myPage/membership')">나의 멤버십</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/myPage/edit-profile' }" @click="navigateTo('/myPage/edit-profile')">나의 정보 수정</div>
    </div>

    <!-- 멤버십 내역 페이지 내용 -->
    <div class="membership-view-content" v-if="user">
      <div class="membership-title">
        <h2><strong>{{ user.name }}</strong> 님이 이용 중인 멤버십 내역</h2>
      </div>

      <!-- 골드/실버 등급일 경우 -->
      <div class="membership-detail" v-if="user.membership !== 'bronze'" :id="user.membership">
        <h1 class="membership-grade">
          <img :src="membershipImage()" alt="멤버십 등급 아이콘" class="membershipLevel-image" />
          <span>{{ user.membership.toUpperCase() }}</span> MEMBERSHIP
          <img :src="membershipImage()" alt="멤버십 등급 아이콘" class="membershipLevel-image" />
        </h1>
        <div id="membership-price">
          <span class="th">결제 금액</span><span class="td"> {{ new Intl.NumberFormat('ko-KR').format(membershipPrice) }}원</span>
        </div>
        <div class="membership-status">
          <span class="th">결제 상태</span>
          <span class="td" v-if="membershipSalesStatus === 'PURCHASE'">결제 완료</span>
          <span class="td" v-else-if="membershipSalesStatus === 'CONFIRMED'">환불 불가</span>
          <span class="td" v-else-if="membershipSalesStatus === 'REFUNDED'">환불 완료</span>
        </div>
        <div class="membership-sales-date">
          <span class="th">결제 날짜</span><span class="td">{{ formattedPaymentDate }}</span>
        </div>
        <div class="membership-end-date">
          <span class="th">멤버십 종료 날짜</span><span class="td">~ {{membershipEndDate}}까지</span>
        </div>
        <p class="membership-note">
          결제 후 콘텐츠 이용 내역이 없을 경우, 결제일로부터 7일 이내에 직접 결제 취소가 가능합니다.
        </p>
      </div>
      <div class="cancel-button-box">
        <button @click="MembershipCancel" class="cancel-membership-button" 
          v-if="membershipSalesStatus === 'PURCHASE'">결제 취소</button>
      </div>
    </div>
      <!-- 브론즈 등급일 경우 -->
      <div class="membership-detail" v-if="user.membership === 'bronze'" id="bronze">
        <h1 class="membership-grade">
          <img :src="membershipImage()" alt="멤버십 등급 아이콘" class="membershipLevel-image" />
          <span>{{ user.membership.toUpperCase() }}</span> MEMBERSHIP
          <img :src="membershipImage()" alt="멤버십 등급 아이콘" class="membershipLevel-image" />
        </h1>
        <div class="membership-bronze-content">
          <p>이용중인 멤버십이 없습니다.</p>
        </div>
        <div class="button-container">
    <button @click="goToMembershipInfo" class="btn-goToMembershipInfo">멤버십 안내</button>
    <button @click="goToMembershipPurchase" class="btn-goToMembershipPurchase">멤버십 구매</button>
  </div>
      </div>
  </div>
</template>

<script>
import LogoHeader from '../common/LogoHeader.vue';

export default {
  name: 'MembershipView',
  components: {
    LogoHeader,
  },
  props:{
    user: {
      type: Object,
      required: true
    },
  },
  data() {
    return {
      name: 'MMS',
      activeMenu: this.$route.path,
      membershipPrice: null,
      membershipSalesStatus: null,
      membershipSalesCreateAt: null,
      membershipEndDate: null,
    };
  },
  watch: {
    $route(to) {
      this.activeMenu = to.path;
    }
  },
  computed: {
    formattedPaymentDate() {
      if (!this.membershipSalesCreateAt) {
        return '';
      }
      return this.membershipSalesCreateAt.split('T').join(' ').split('.')[0];
    }
  },
  mounted() {
    this.name = sessionStorage.getItem('name');
    this.fetchEvents();
  },
  methods: {
    navigateTo(route) {
      this.$router.push(route);
      this.activeMenu = route;
    },
    goToMembershipInfo() {
      this.$router.push('/membership/info');
    },
    goToMembershipPurchase() {
      this.$router.push('/membership/purchase');
    },
    membershipImage(){
      return  require(`@/assets/img/membershipImg/${this.user.membership}.png`);
    },
    async fetchEvents() {
      try {
        const response = await this.$axios.get("/myPage/membership");
        this.membershipPrice = response.data.membershipPrice || '멤버십 가격';
        this.membershipSalesStatus = response.data.membershipSalesStatus || '결제 후 상태';
        this.membershipSalesCreateAt = response.data.membershipSalesCreateAt || '결제 날짜';
        this.membershipEndDate = response.data.seasonEndDate || '멤버십 종료 날짜';
        this.$emit("getMembership",response.data.membershipName.split('-')[1]);
        this.season=response.data.membershipName.split('-')[0]
      } catch (error) {
        console.error('멤버십 정보 가져오기 오류:', error);
      }
    },
    MembershipCancel(){
    this.$axios.get("/membership/cancel")
    .then(()=>{
      this.$emit("getMembership",'bronze');
      this.$emit("getBalance", Number(sessionStorage.getItem('balance')) + this.membershipPrice);
      alert(`멤버십 결제가 취소되셨습니다.\n${new Intl.NumberFormat('ko-KR').format(this.membershipPrice)}원 환불 되셨습니다.`);
      this.membershipSalesStatus=null;
    }).catch((error)=>{
      console.log("멤버십 취소 중 오류 발생 : "+ error);
    });
  }
}
}
</script>

<style scoped>
/* General Page Styling */
/* General Page Styling */
.membership-view-page {
  padding-top: var(--header-height);
  padding-bottom: var(--footer-height);
  text-align: center;
}

/* Menu Styling */
.menu {
  display: flex;
  justify-content: center;
  gap: 50px;
  padding-bottom: 20px;
}

.menu-item {
  width: 120px;
  padding: 10px 0;
  cursor: pointer;
  font-weight: bold;
  color: #565656;
  position: relative;
  transition: color 0.3s ease;
}

.menu-item::after {
  content: "";
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 100%;
  height: 3px;
  background-color: black;
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.menu-item:hover {
  color: #333;
}

.menu-item:hover::after,
.active-menu-item::after {
  transform: scaleX(1);
}

.active-menu-item {
  color: black;
}

.membership-detail {
  margin: auto;
  max-width: 600px;
  border-radius: 10px;
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.3);
  text-align: center;
  height: auto;
}
/* Gold Membership Detail */
#gold {
  border: 2px solid #E1B12C;
  background-color: #f3efe6a9;
}

/* Silver Membership Detail */
#silver {
  border: 2px solid #B0BEC5;
  background-color: #f8f8f8;
}

/* Membership Grade */
.membership-grade {
  padding: 10px;
  margin-bottom: 0px;
  margin-top: 0;
  text-align: center;
  justify-content: center;
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}
#gold .membership-grade {
  border-bottom: 2px solid #D4AF37;
  color: #B8860B;
  background-color: #fff5aa;
}

#silver .membership-grade {
  border-bottom: 2px solid #B0BEC5;
  color: #607D8B;
  background-color: #E0E0E0;
}

/* Membership Details: 각 항목 */
.membership-detail div {
  display: flex;
  padding: 10px 0;
  margin: 0px 30px; 
  position: relative;
  align-items: center; /* 세로 정렬을 중앙으로 */
}

#gold div::before {
  background-color: #E1B12C; /* 골드 색상 */
  content: "";
  position: absolute;
  left: 35%;
  top: 0;
  bottom: 0;
  width: 1px;
  transform: translateX(-50%);
}

#silver div::before {
  background-color: #B0BEC5; /* 은색 계열 */
  content: "";
  position: absolute;
  left: 35%;
  top: 0;
  bottom: 0;
  width: 1px;
  transform: translateX(-50%);
}

/* Gold Membership Detail - 하단 테두리 */
#gold #membership-price,
#gold .membership-status,
#gold .membership-sales-date,
#gold .membership-end-date {
  border-bottom: 1px solid #E8C267;
}

/* Silver Membership Detail - 하단 테두리 */
#silver #membership-price,
#silver .membership-status,
#silver .membership-sales-date,
#silver .membership-end-date {
  border-bottom: 1px solid #B0BEC5;
}

.membership-detail .th {
  font-weight: bold;
  width: 35%;
  text-align: right; 
  padding-right: 30px; 
}
#gold .membership-detail .th {
  color: #8B4513; 
}
#silver .membership-detail .th {
  color: #607D8B
}

.membership-detail .td {
  font-size: 20px;
  color: #3B2F2F; 
  width: 65%;
  text-align: center; 
}
#gold .membership-detail .td {
  color: #3B2F2F; 
}
#silver .membership-detail .td {
  color: #607D8B; 
}
/* Membership Note: 안내문 */
.membership-note {
  font-size: 14px;
  margin-top: 10px;
  color: #b9b9b9;
}

/* Bronze Membership Info */
.membership-info-bronze {
  background-color: #E0F7FA;
  margin: auto;
  padding: 20px;
  max-width: 600px;
  border-radius: 10px;
  border: 2px solid #009688;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
  text-align: center;
  height: auto;
}
#bronze {
  border: 2px solid #B87333;
  background-color: #f2e9e0a7;
  margin-bottom: 30px;
}
#bronze .membership-grade {
  border-bottom: 2px solid #CD7F32;
  color: #8C4B1F; 
  background-color: #FAE3D2;
  font-size: 1.8em;
}
.membership-bronze-content{
  justify-content: center;
}
.membership-bronze-content p {
  color: #5A3E2B;
  font-size: 25px;
}
/* Cancel Membership Button */
.cancel-button-box {
  text-align: right;
  max-width: 600px;
  margin: auto;
}
/* 버튼을 포함하는 컨테이너 */
.button-container {
  display: flex;
  justify-content: center;
  gap: 30px; /* 버튼 사이 간격 확대 */
  margin-right: 30px;
}

/* 브론즈 멤버십 버튼 스타일 */
.btn-goToMembershipInfo,
.btn-goToMembershipPurchase {
  background-color: #B87333; /* 브론즈 색상 */
  color: #fff;
  border: 2px solid #CD7F32;
  border-radius: 8px;
  cursor: pointer;
  padding: 15px 30px;
  font-weight: bold;
  font-size: 1.2em; /* 글씨 크기 확대 */
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
}

/* 버튼 호버 시 스타일 */
.btn-goToMembershipInfo:hover,
.btn-goToMembershipPurchase:hover {
  background-color: #CD7F32; /* 조금 더 짙은 브론즈 색상 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); /* 그림자 효과 추가 */
}
.cancel-membership-button {
  background-color: #f3aca9;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 5px;
  font-size: medium;
  width: 100px;
  height: 40px;
  padding: 10px;
  margin-top: 10px;
  margin-bottom: 30px;
}

.cancel-membership-button:hover {
  background-color: #d32f2f; /* 더 짙은 빨간색 */
}

/* Membership Level Image */
.membershipLevel-image {
  width: 40px;
  vertical-align: middle;
  filter: drop-shadow(2px 2px 2px rgba(0, 0, 0, 0.2)); /* 그림자 효과 추가 */
}
</style>
