<template>
    <div class="logo-header">
        <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
        <div class="menu-logo">
            <h1>{{ logoText }}</h1>
        </div>
    </div>
</template>

<script>
export default {
    name: 'LogoHeader',
    computed: {
        logoText() {
        const path = this.$route.path;
        if (path.includes('/mms')) {
        return 'MMS';
        } else if (path.includes('/team')) {
        return 'TEAM';
        } else if (path.includes('/game')) {
        return 'GAME';
        } else if (path.includes('/ticket')) {
        return 'TICKET';
        } else if (path.includes('/myPage')) {
        return 'MY PAGE';
        } else if (path.includes('/membership')) {
        return 'MEMBERSHIP';
        } else if (path.includes('/login')) {
        return 'LOGIN';
        }  else if (path.includes('/signup')) {
        return 'SIGN UP';
        } 
        return 'DEFAULT'; // 기본 텍스트
        }
    }
}
</script>

<style scoped>

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
</style>