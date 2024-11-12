<template>
  <div class="ci-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <LogoHeader />
  
    <!-- 인사말, 연혁, CI, 스폰서 메뉴 -->
    <div class="menu">
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/greeting' }" @click="navigateTo('/mms/greeting')">인사말</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/history' }" @click="navigateTo('/mms/history')">연혁</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/ci' }" @click="navigateTo('/mms/ci')">CI</div>
      <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/mms/sponsor' }" @click="navigateTo('/mms/sponsor')">스폰서</div>
    </div>
  
    <!-- CI 페이지 내용 -->
    <div class="ci-content">
      <!-- 엠블럼 섹션 -->
      <div class="section emblem">
        <img src="@/assets/img/mms/mms-ci-emblem.png" alt="Emblem" class="ci-image" />
        <div class="ci-description">
          <h2>엠블럼</h2>
          <p>MMS 팀의 엠블럼은 팀의 탄생 배경을 상징적으로 담고 있습니다. 팀명 'MMS'는 ‘마미손’이라는 뜻에서 유래했으며, 핑크색 장갑은 이를 상징적으로 표현한 이미지입니다. 엠블럼에 등장하는 손은 배구 경기를 시작하기 전의 긴장감과 흥미를 표현하고 있으며, 그 손이 공을 든 모습은 배구의 다이내믹한 순간을 상징합니다. 엠블럼에 사용된 다양한 색상은 MMS 배구단의 다채롭고 창의적인 플레이 스타일을 그대로 반영하고 있으며, 팀의 활기와 에너지를 상징합니다. 이 엠블럼은 배구 팬들과 팀을 연결하는 상징으로, MMS 팀의 끊임없는 도전과 열정을 담고 있습니다.</p>
        </div>
      </div>

      <hr class="divider" />

      <!-- 유니폼 섹션 -->
      <div class="section uniform">
        <img src="@/assets/img/mms/mms-ci-uniform.png" alt="Uniform" class="ci-image" />
        <div class="ci-description">
          <h2>유니폼</h2>
          <p>MMS 배구단의 유니폼은 팀의 강렬한 에너지를 시각적으로 담아낸 디자인입니다. 선명한 패턴은 팀의 역동적이고 빠른 플레이 스타일을 상징하며, 중앙의 팀 로고는 자부심과 소속감을 강조합니다. 스폰서 로고와 배구공 이미지가 조화롭게 배치되어, 세련되면서도 팀의 정체성을 잘 표현하고 있습니다.</p>
        </div>
      </div>

      <hr class="divider" />

      <!-- 마스코트 섹션 -->
      <div class="section mascot">
        <img src="@/assets/img/mms/mms-ci-mascot.png" alt="Mascot" class="ci-image" />
        <div class="ci-description">
          <h2>마스코트</h2>
          <p>'미손'은 엄마손의 강력한 스파이크에서 태어난 열정 넘치는 배구 소년입니다. 그 힘찬 스파이크는 누구도 막을 수 없지만, 미손은 공격뿐 아니라 수비에서도 뛰어난 실력을 자랑하는 다재다능한 선수죠. 배구 경기를 좋아하지만, 무엇보다도 팬들과 함께하는 응원을 더 사랑하는 미손! 언제나 미소를 잃지 않고 팬들에게 기운을 북돋아주는 귀여운 마스코트입니다. 미손이와 함께라면 경기는 언제나 즐겁고, 응원은 더욱 뜨겁습니다! 팬 여러분도 미손과 함께 신나는 경기를 즐겨보세요!</p>
        </div>
      </div>

    </div>
  </div>
  </template>

<script>
import LogoHeader from '../common/LogoHeader.vue';

export default {
  name: 'AppCI',
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
.ci-page {
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

/* CI */
.ci-content {
  max-width: 850px;
  margin: 0 auto;
  padding-top: 35px;
  padding-bottom: 60px;
  padding-left: 30px;
  padding-right: 30px;
}

.section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
}

.ci-image {
  width: 200px;
  height: auto;
}

.ci-description {
  flex: 1;
  text-align: left;
  padding-left: 20px;
}

.ci-description h2 {
  color: #333;
}

.ci-description p {
  color: #555;
}

.divider {
  margin: 20px 0;
  border: none;
  height: 1px;
  background-color: #ccc;
}

/* 화면 크기가 768px 이하로 작아질 때 세로로 배치 */
@media (max-width: 768px) {
  .ci-content {
    flex-direction: column;
    text-align: center;
  }

  .section {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }

}
</style>