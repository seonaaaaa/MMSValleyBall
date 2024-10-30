<template>
  <div id="app">
    <AppHeader />
    <!-- 메타 데이터에서 hideContent가 true인 경우 AppContent를 숨김 -->
    <!-- 경로 없을 때 기본적으로 AppContent 표시 -->
    <AppContent v-if="!$route.meta.hideContent || !$route.name" />
    <router-view />
    <AppFooter />
  </div>
</template>

<script>
import AppHeader from './components/common/Header.vue'
import AppContent from './components/common/Content.vue'
import AppFooter from './components/common/Footer.vue'

export default {
  name: 'App',
  components: {
    AppHeader,
    AppContent,
    AppFooter
  }
}
</script>

<style>
#app {
  min-height: 100vh;  /* 전체 화면을 차지하도록 설정 */
  display: flex;
  flex-direction: column;  /* 상하 배치 */
}

html {
  box-sizing: border-box;
  scroll-behavior: smooth;
  margin: 0;
  padding: 0;
}

*, *::before, *::after {
  box-sizing: border-box;
  font-family: 'Noto Sans KR', sans-serif;
}

body {
  width: 100%;
  margin: 0;
  padding: 0;
}

body, ul, li {
  list-style: none;  /* ul 태그의 ● 제거 */
}

a {
  text-decoration: none; /* a 태그의 밑줄 제거 */
}

:root {
  --header-height: 168px; /* Header 높이를 변수로 정의 */
  --footer-height: 0px; /* Footer 높이를 변수로 정의 */
}
</style>
