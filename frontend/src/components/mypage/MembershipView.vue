<template>
  <div class="membership-view-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />

    <!-- 예매 내역, 나의 멤버십, 개인정보 수정 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/reservations' }" @click="navigateTo('/mypage/reservations')">예매 내역</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/membership' }" @click="navigateTo('/mypage/membership')">나의 멤버십</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mypage/edit-profile' }" @click="navigateTo('/mypage/edit-profile')">나의 정보 수정</div>
    </div>

    <!-- 멤버십 내역 페이지 내용 -->
    <div class="membership-view-content" v-if="grade">
      <h2>이용 중인 멤버십 결제 내역</h2>

      <!-- 골드/실버 등급일 경우 -->
      <div class="membership-info" v-if="grade !== 'bronze'">
        <div class="membership-detail">
          <p>
            <strong>이용 중인 멤버십 : </strong> {{ year }} 시즌 {{ membership.toUpperCase() }}
            <span class="membership-image-container">
              <span v-if="grade === 'gold'">
                <img :src="goldImage" alt="골드 등급" class="membershipLevel-image" />
              </span>
              <span v-else-if="grade === 'silver'">
                <img :src="silverImage" alt="실버 등급" class="membershipLevel-image" />
              </span>
            </span>
          </p>
          <p><strong>회원 이름 : {{ name }}</strong> 님</p>
          <p><strong>결제 금액 : </strong> {{ new Intl.NumberFormat('ko-KR').format(membershipDetails.membershipPrice) }}원</p>
          <p><strong>결제 상태 : </strong>
            <span v-if="membershipDetails.membershipSalesStatus === 'PURCHASE'">결제 완료</span>
            <span v-else-if="membershipDetails.membershipSalesStatus === 'CONFIRMED'">환불 불가</span>
            <span v-else-if="membershipDetails.membershipSalesStatus === 'REFUNDED'">환불 완료</span>
          </p>
          <p><strong>결제 날짜 : </strong> {{ formattedPaymentDate }}</p>
        </div>
        <div class="cancel-button-container">
          <button
            @click="MembershipCancel"
            class="cancel-membership-button"
            :disabled="membershipDetails.membershipSalesStatus === 'CONFIRMED' || membershipDetails.membershipSalesStatus === 'REFUNDED'"
            :class="{ 'disabled-button': membershipDetails.membershipSalesStatus === 'CONFIRMED' || membershipDetails.membershipSalesStatus === 'REFUNDED' }">
            결제 취소
          </button>
        </div>
        <p class="membership-note">
          결제 후 콘텐츠 이용 내역이 없을 경우, 결제일로부터 7일 이내에 직접 결제 취소가 가능합니다.
        </p>
      </div>

      <!-- 브론즈 등급일 경우 -->
      <div class="membership-info-bronze" v-else-if="grade === 'bronze'">
        <p><strong>이용중인 멤버십이 없습니다.</strong></p>
        <p><strong>구매 후 이용해주세요.</strong></p>
        <button @click="goToMembershipInfo" class="btn-goToMembershipInfo">멤버십 안내</button>&nbsp;&nbsp;<button @click="goToMembershipPurchase" class="btn-goToMembershipPurchase">멤버십 구매</button>
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
    membership: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      name: 'MMS',
      activeMenu: this.$route.path,
      membershipDetails: null,
      goldImage: require('@/assets/img/membershipImg/gold.png'),
      silverImage: require('@/assets/img/membershipImg/silver.png'),
      bronzeImage: require('@/assets/img/membershipImg/bronze.png'),
      grade : '',
      year : '',
    };
  },
  watch: {
    $route(to) {
      this.activeMenu = to.path;
    }
  },
  computed: {
    formattedPaymentDate() {
      if (!this.membershipDetails || !this.membershipDetails.membershipSalesCreateAt) {
        return '';
      }
      return this.membershipDetails.membershipSalesCreateAt.split('T').join(' ').split('.')[0];
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
    async fetchEvents() {
      const params = new URLSearchParams();
      params.append('email', sessionStorage.getItem('email'));

      try {
        const response = await this.$axios.post("/myPage/membership", null, {params: params});

        this.membershipDetails = {
          membershipName: response.data.membershipName || '멤버십 이름',
          membershipPrice: response.data.membershipPrice || '멤버십 가격',
          membershipSalesStatus: response.data.membershipSalesStatus || '결제 후 상태',
          membershipSalesCreateAt: response.data.membershipSalesCreateAt || '결제 날짜'
        };

        const [year, grade] = this.membershipDetails.membershipName.split('-');
        this.grade = grade;
        this.year = year;
      } catch (error) {
        console.error('멤버십 정보 가져오기 오류:', error);
      }
    },
    MembershipCancel(){
    const params = new URLSearchParams();
    params.append('userEmail', sessionStorage.getItem('email'));

    this.$axios.post("/membership/cancel", null, { params: params})
    .then(()=>{
      alert('결제가 성공적으로 완료되었습니다.');
      window.location.reload();
    })
  }
}
}
</script>

<style scoped>
.membership-view-page {
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

/* 멤버십 내역 */
.membership-info {
  background-color: #f9f9f9;
  padding: 20px;
  margin: 20px auto;
  width: 80%;
  max-width: 600px;
  border-radius: 10px;
  border: 2px solid #60a191;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  text-align: left;
  margin-bottom: 50px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.membership-info-bronze {
  background-color: #f9f9f9;
  padding: 20px;
  margin: 20px auto;
  width: 80%;
  max-width: 600px;
  border-radius: 10px;
  border: 2px solid #60a191;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  text-align: center;
  margin-bottom: 50px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.membership-detail p {
  margin: 10px 0;
  font-size: 18px;
}

.cancel-button-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 0px;
  margin-bottom: 20px;
}

.cancel-membership-button {
  background-color: #d9534f;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.cancel-membership-button:hover {
  background-color: #c9302c;
}

.membership-note {
  font-size: 14px;
  color: #666;
  margin-top: 10px;
}

.membershipLevel-image{
  width: 40px;
}

button{
  background-color: #4f8578;
  color: white;
  border: none;
  padding: 10px;
  width: 150px;
  height: 53px;
  margin-top: 10px;
  margin-left: 10px;
  border-radius: 5px;
  cursor: pointer;
}

.membership-detail p {
  font-size: 20px;
}

.disabled-button {
  background-color: gray;
  cursor: not-allowed;
}
</style>
