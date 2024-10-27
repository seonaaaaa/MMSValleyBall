<template>
<div class="game-results-page">
  <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
  <LogoHeader />

  <!-- 경기 일정, 경기 결과 메뉴 -->
  <div class="menu">
    <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/game/schedule' }" @click="navigateTo('/game/schedule')">경기 일정</div>
    <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/game/results' }" @click="navigateTo('/game/results')">경기 결과</div>
  </div>

  <!-- 경기 결과 페이지 내용 -->
  <div class="game-results-content">

    <div class="filter-container">
      <select v-model="selectedSeasonId" @change="fetchMatches(0)">
        <option v-for="season in seasons" :key="season.seasonId" :value="season.seasonId">
          {{ season.seasonName }}
        </option>
      </select>
      <button @click="showAllMatches">전체보기</button>
      <div class="round-navigation">
        <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 0">&lt;</button>
        <span>{{ currentPage + 1 }} 라운드</span>
        <button @click="goToPage(currentPage + 1)" :disabled="currentPage + 1 === totalPages">&gt;</button>
      </div>
    </div>

    <table class="match-table">
      <thead>
        <tr>
          <th>날짜</th>
          <th>순번</th>
          <th>경기</th>
          <th>시간</th>
          <th>구장</th>
          <th>라운드</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="match in matches" :key="match.matchId">
          <td>{{ formatDate(match.matchDate) }}</td>
          <td>{{ match.matchId }}</td>
          <td>
            <span class="home-team">MMS GS</span>
            <span class="score">{{ match.matchSetScore }}:{{ match.matchOpponentTeamSetScore }}</span>
            <span class="away-team">{{ match.teamName }}</span>
          </td>
          <td>{{ formatTime(match.matchDate) }}</td>
          <td>{{ match.teamStadium }}</td>
          <td>{{ (currentPage + 1) }} 라운드</td>
        </tr>
      </tbody>
    </table>

  </div>
</div>
</template>

<script>
import axios from 'axios';
import LogoHeader from '../common/LogoHeader.vue';

export default {
  name: 'AppResults',
  components: {
    LogoHeader
  },    
  data() {
    return {
      activeMenu: this.$route.path, // 현재 활성화된 경로
      // 시즌 데이터
      seasons: [
        { seasonId: 1, seasonName: '도드람 2023-2024 V-리그' },
        { seasonId: 2, seasonName: '도드람 2024-2025 V-리그' },
      ],
      selectedSeasonId: 1, // 기본 선택 시즌 ID
      matches: [], // 전체 경기 데이터 저장
      currentPage: 0, // 현재 페이지
      totalPages: 0, // 전체 페이지 수
    };
  },
  watch: {
    // 경로가 변경될 때마다 activeMenu를 업데이트
    $route(to) {
      this.activeMenu = to.path;
    },
  },
  methods: {
    // 선택한 메뉴로 이동하는 함수
    navigateTo(route) {
      this.$router.push(route);
      // 메뉴를 클릭할 때 활성화된 메뉴 업데이트
      this.activeMenu = route; 
    },

    // 전체보기 - 선택된 시즌의 전체 데이터 가져오기
    async showAllMatches() {
      try {
        const response = await axios.get(`http://localhost:4000/game/schedule/total`, {
          params: { seasonId: this.selectedSeasonId }
        });
        this.matches = response.data.content;
        this.totalPages = 1; // 전체보기 시 페이지 1로 고정
        this.currentPage = 0;
      } catch (error) {
        console.error("Error fetching all match data:", error);
      }
    },

    // 선택된 시즌별 데이터 페이징 처리하여 가져오기
    async fetchMatches(page = 0) {
      try {
        const response = await axios.get(`http://localhost:4000/game/results`, {
          params: {
            seasonId: this.selectedSeasonId,
            page: page,
            size: 6,
          },
        });
        this.matches = response.data.content;
        this.totalPages = response.data.totalPages;
        this.currentPage = page;
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },
    
    // 페이지 이동
    goToPage(page) {
      if (page >= 0 && page < this.totalPages) {
        this.fetchMatches(page);
      }
    },

    // 날짜 포맷팅
    formatDate(date) {
      const d = new Date(date);
      return `${d.getMonth() + 1}.${d.getDate()} (${['일', '월', '화', '수', '목', '금', '토'][d.getDay()]})`;
    },
    // 시간 포맷팅
    formatTime(date) {
      const d = new Date(date);
      return `${d.getHours()}:${d.getMinutes().toString().padStart(2, '0')}`;
    },
    // 경기 결과 포맷팅
    formatMatchResult(match) {
      return `MMS GS   ${match.matchSetScore}:${match.matchOpponentTeamSetScore}   ${match.teamName}`;
    },
  },
  mounted() {
    this.fetchMatches();
  }
};
</script>

<style>
.game-results-page {
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

/* 경기 결과 */
.match-results {
  max-width: 1200px;
  margin: 0 auto;
}

.controls {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.round-navigation {
  display: flex;
  align-items: center;
}

/* 테이블 스타일 */
.match-table {
  width: 100%;
  border-collapse: collapse;
}

.match-table th, .match-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: center;
}

.home-team, .away-team {
  margin-right: 10px; /* 팀명 사이의 공백 */
}
.score {
  margin: 0 13px; /* 스코어와 팀명 사이의 여백 */
}
</style>