<template>
<div class="mms-page">
    <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
    <div class="menu-logo">
        <h1>MMS</h1>
    </div>

    <!-- 2차 메뉴 -->
    <ul class="menu">
        <li><router-link to="/mms/greeting" exact-active-class="active">인사말</router-link></li>
        <li><router-link to="/mms/history" exact-active-class="active">연혁</router-link></li>
        <li><router-link to="/mms/ci" exact-active-class="active">CI</router-link></li>
        <li><router-link to="/mms/sponsor" exact-active-class="active">스폰서</router-link></li>
    </ul>

    <!-- 여기는 콘텐츠 없음 -->
</div>
</template>

<script>
export default {
    name: 'AppMMS',
}
</script>

<style scoped>
.mms-page {
  text-align: center;
}

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
  color: #d6d6d6;
}

.menu {
  list-style: none;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.menu li {
  display: inline-block;
}
</style>