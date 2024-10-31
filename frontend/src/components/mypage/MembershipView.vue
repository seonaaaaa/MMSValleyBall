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
    <div class="membership-view-content" v-if="membershipDetails">
      <h2>이용 중인 멤버십 결제 내역</h2>
      <div class="membership-info">
        <div class="membership-detail">
            <p>
            <strong>이용 중인 멤버십 : </strong> {{ formattedMembershipName }}
            <span class="membership-image-container">
        <span v-if="grade == 'gold'">
          <img :src="goldImage" alt="골드 등급" class="membershipLevel-image" />
        </span>
        <span v-else-if="grade == 'silver'">
           <img :src="silverImage" alt="실버 등급" class="membershipLevel-image" />
        </span>
        <span v-else>
          <img :src="bronzeImage" alt="브론즈 등급" class="membershipLevel-image" />
        </span>
      </span>
      </p>

        <p><strong>회원 이름 : {{ user.name }}</strong> 님</p>
          <p><strong>결제 금액 : </strong> {{ membershipDetails.membershipPrice }}원</p>
          <p><strong>결제 상태 : </strong>
            <span v-if="membershipDetails.membershipSalesStatus === 'PURCHASE'">결제 완료</span>
            <span v-else-if="membershipDetails.membershipSalesStatus === 'CONFIRMED'">환불 불가</span>
            <span v-else-if="membershipDetails.membershipSalesStatus === 'REFUNDED'">환불 완료</span>
          </p>
          <p><strong>결제 날짜 : </strong> {{ formattedPaymentDate }}</p>
        </div>
        <div class="cancel-button-container">
          <button class="cancel-membership-button">결제 취소</button>
        </div>
      </div>
      <p class="membership-note">
        결제 후 콘텐츠 이용 내역이 없을 경우, 결제일로부터 7일 이내에 직접 결제 취소가 가능합니다.
      </p>
    </div>
  </div>
</template>

<script>
import LogoHeader from '../common/LogoHeader.vue';

export default {
  name: 'MembershipView',
  components: {
    LogoHeader
  },
  props: {
    user: {
      type: Object,
      default: () => ({ name: '', role: 'guest', email: '', isLoggedIn: false })
    }
  },
  data() {
    return {
      activeMenu: this.$route.path, // 현재 활성화된 경로
      membershipDetails: null, // 멤버십 세부 정보
      // 골드 등급 이미지 경로
      goldImage: require('@/assets/img/membershipImg/gold.png'),
      // 실버 등급 이미지 경로
      silverImage: require('@/assets/img/membershipImg/silver.png'),
      // 브론즈 등급 이미지 경로
      bronzeImage: require('@/assets/img/membershipImg/bronze.png'),

    };
  },
  watch: {
    // 경로가 변경될 때마다 activeMenu를 업데이트
    $route(to) {
      this.activeMenu = to.path;
    }
  },
  computed: {
    // 계산된 속성으로 데이터를 가공
    formattedMembershipName() {
      if (!this.membershipDetails || !this.membershipDetails.membershipName) {
        return '';
      }

      const membershipName = this.membershipDetails.membershipName;
      // 예: "24/25-gold"를 숫자 부분과 등급 부분으로 분리
      const [year, grade] = membershipName.split('-');
      
      // 필요에 따라 데이터를 가공하여 원하는 형식으로 반환
      return `${year} 시즌 - ${grade.toUpperCase()} 등급`;
    },

    formattedPaymentDate() {
    if (!this.membershipDetails || !this.membershipDetails.membershipSalesCreateAt) {
      return '';
    }
    
    // '2024-08-10T13:58:00' 형식의 결제 날짜를 '2024-08-10 13:58:00'으로 변경
    return this.membershipDetails.membershipSalesCreateAt.split('T').join(' ');
  }
  },
  mounted() {
    this.fetchEvents(); // 컴포넌트가 로드될 때 실행
  },
  methods: {
    navigateTo(route) {
      this.$router.push(route);
      this.activeMenu = route; // 메뉴를 클릭할 때 활성화된 메뉴 업데이트
    },

    async fetchEvents() {
      console.log('---------------' + this.user.email);

      const params = new URLSearchParams();
      params.append('email', this.user.email);

      try {
        const response = await this.$axios.post("/myPage/membership", null, { params: params });
        console.log(response.data);
        
        // 서버 응답 데이터를 membershipDetails에 저장
        this.membershipDetails = {
          membershipName: response.data.membershipName || '멤버십 이름',
          membershipPrice: response.data.membershipPrice || '멤버십 가격',
          membershipSalesStatus: response.data.membershipSalesStatus || '결제 후 상태',
          membershipSalesCreateAt: response.data.membershipSalesCreateAt || '결제 날짜'
        };
      } catch (error) {
        console.error('멤버십 정보 가져오기 오류:', error);
      }
    }
  }
}
</script>

<style>
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
}

.membership-detail p {
  margin: 10px 0;
  font-size: 16px;
}

.cancel-button-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
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

.membership-detail p {
  margin: 10px 0;
  font-size: 18px; /* 폰트 크기 변경 */
}
</style>
