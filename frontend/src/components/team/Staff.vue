<template>
  <div class="staff-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />
  
    <!-- 선수, 스태프, 구장 메뉴 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/team/players' }" @click="navigateTo('/team/players')">선수</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/team/staff' }" @click="navigateTo('/team/staff')">스태프</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/team/stadium' }" @click="navigateTo('/team/stadium')">구장</div>
    </div>
  
    <!-- 스태프 페이지 내용 -->
    <div class="staff-content">
      <div class="staff-section">
      <!-- 코칭 스태프 제목 -->
      <h2>코칭 스태프</h2>
      <div class="staff-container">
        <!-- 코칭 스태프 리스트 -->
        <div class="staff-card" v-for="(staff, index) in coachingStaff" :key="index">
          <img :src="staff.image" alt="코칭 스태프 이미지" class="staff-image" />
          <p class="staff-name">{{ staff.role }}</p>
          <p class="staff-role">{{ staff.name }}</p>
        </div>
      </div>
  
      <!-- 지원 스태프 제목 -->
      <h2>지원 스태프</h2>
      <div class="staff-container">
        <!-- 지원 스태프 리스트 -->
        <div class="staff-card" v-for="(staff, index) in supportingStaff" :key="index">
          <img :src="staff.image" alt="지원 스태프 이미지" class="staff-image" />
          <p class="staff-name">{{ staff.role }}</p>
          <p class="staff-role">{{ staff.name }}</p>
        </div>
      </div>
    </div>
    </div>
  </div>
</template>
  
<script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
    name: 'AppStaff',
    components: {
      LogoHeader
  },
    data() {
      return {
        activeMenu: this.$route.path, // 현재 활성화된 경로
        // 코칭 스태프 데이터
        coachingStaff: [
          { name: '김철수', role: '감독', image: require('@/assets/img/team/team-staff-coach.png') },
          { name: '최현문', role: '수석코치', image: require('@/assets/img/team/team-staff-staff-001.png') },
          { name: '강만준', role: '코치', image: require('@/assets/img/team/team-staff-staff-005.png') }
        ],
        // 지원 스태프 데이터
        supportingStaff: [
          { name: '김민현', role: '트레이너', image: require('@/assets/img/team/team-staff-staff-006.png') },
          { name: '박희영', role: '통역', image: require('@/assets/img/team/team-staff-staff-002.png') },
          { name: '서준희', role: '매니저', image: require('@/assets/img/team/team-staff-staff-003.png') },
          { name: '정재이', role: '전력분석관', image: require('@/assets/img/team/team-staff-staff-004.png') }
        ]
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
  .staff-page {
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
  
  /* 스태프 */
  .staff-content {
    padding-top: 30px;
    padding-bottom: 80px;
  }
  
  .staff-section {
    text-align: center;
    margin: 0 auto;
    max-width: 1200px;
  }
  
  h2 {
    font-size: 24px;
    margin: 30px 0;
    color: #333;
  }
  
  .staff-container {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    gap: 20px;
  }
  
  .staff-card {
    width: 250px;
    text-align: center;
    padding: 10px;
  }
  
  .staff-image {
    width: 100%;
    border-radius: 8px;
    margin-bottom: 10px;
  }
  
  .staff-name {
    font-weight: bold;
    font-size: 18px;
  }
  
  .staff-role {
    font-size: 16px;
    color: gray;
  }
</style>