<template>
<div class="sponsor-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <div class="menu-logo">
        <h1>MMS</h1>
    </div>
    
    <!-- 인사말, 연혁, CI, 스폰서 메뉴 -->
    <div class="menu">
    <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/greeting' }" @click="navigateTo('/mms/greeting')">인사말</div>
    <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/history' }" @click="navigateTo('/mms/history')">연혁</div>
    <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/ci' }" @click="navigateTo('/mms/ci')">CI</div>
    <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/sponsor' }" @click="navigateTo('/mms/sponsor')">스폰서</div>
    </div>

    <!-- 스폰서 페이지 내용 -->
    <div class="sponsor-content">
        <div class="sponsor-section">
            <div class="sponsor-description">
                <h2>메인 스폰서</h2>
            </div>
            <div class="sponsor-img-container">
                <img src="@/assets/img/mms/mms-sponsor-gsitm.png" alt="GSITM Logo" class="sponsor-image" />
            </div>
        </div>

        <!-- 공식 후원사 -->
        <div class="sponsor-section">
            <div class="sponsor-description">
                <h2>공식 후원사</h2>
            </div>
            <div class="sponsor-img-container">
                <img src="@/assets/img/mms/mms-sponsor-himedia.png" alt="HiMedia Logo" class="sponsor-image" />
            </div>
        </div>

        <!-- 공식 공급업체 -->
        <div class="sponsor-section">
            <div class="sponsor-description">
                <h2>공식 공급업체</h2>
            </div>
            <div class="sponsor-img-row">
                <div class="sponsor-img-container">
                    <img src="@/assets/img/mms/mms-sponsor-megacoffee.png" alt="Mega Coffee Logo" class="sponsor-image" />
                </div>
                <div class="sponsor-img-container">
                    <img src="@/assets/img/mms/mms-sponsor-redbull.png" alt="Red Bull Logo" class="sponsor-image" />
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
export default {
    name: 'AppSponsor',
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
.sponsor-page {
    padding-top: var(--header-height);
    padding-bottom: var(--footer-height);
    text-align: center;
}

/* 메뉴 로고 */
.menu-logo {
  width: 100%;
  height: 200px;
  background-color: #4f8578;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}

.menu-logo h1 {
  font-size: 48px;
  color: #ffffff;
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

/* 스폰서 */
.sponsor-content {
  max-width: 800px;
  margin: 0 auto;
  padding-top: 35px;
  padding-bottom: 60px;
}

.sponsor-section {
    /* margin-bottom: 70px; */
    margin-bottom: 100px;
}

.sponsor-description {
  text-align: left;
  padding-bottom: 10px;
}

.sponsor-description h2 {
    font-size: 24px;
    color: #333;
    margin-left: 50px;
}

.sponsor-img-container {
  display: flex;
  justify-content: flex-start;
  border-radius: 10px;
  padding: 20px;
  max-width: 300px;
  width: 100%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin: 0;
  margin-left: 30px;
}

.sponsor-img-row {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
  gap: 40px;
}

.sponsor-image {
  width: 100%;
  max-height: 70px;
}
</style>