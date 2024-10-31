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
    <h2>전체 경기 결과</h2>

    <!-- 상단 컨트롤 박스 (시즌 선택, 라운드 네비게이션, 전체보기 버튼) -->
    <div class="header-container">
      <div class="select-container">
        <select v-model="selectedSeasonId" @change="fetchMatches(0)">
          <option v-for="season in seasons" :key="season.seasonId" :value="season.seasonId">
            {{ season.seasonName }}
          </option>
      </select>
      </div>

      <!-- 라운드 내브 -->
      <div class="round-navigation">
        <img src="@/assets/img/game/nav-icon-previous.png"
              alt="Previous"
              @click="changeRound(-1)"
              :class="{ disabled: matchRoundId <= 1 }" />

        <!-- 페이지 전체보기 할 때는 '전체' / 라운드 선택하면 '라운드' 출력 -->
        <h2>
          {{ totalPages === 1 && !matchRoundId ? '전체' : (matches.length > 0 ? matches[0].matchRoundId + ' 라운드' : '라운드') }}
        </h2>
        <img src="@/assets/img/game/nav-icon-next.png"
              alt="Next"
              @click="changeRound(1)"
              :class="{ disabled: matchRoundId >= maxRound }" />
      </div>

      <!-- 라운드 셀렉트 박스 -->
      <div class="round-select-box">
        <select v-model="matchRoundId" @change="fetchMatches(0)">
          <option disabled value="">라운드 선택</option>
          <option v-for="round in 6" :key="round" :value="round">
            {{ round }}라운드
          </option>
        </select>
      </div>

      <!-- 전체보기 버튼 -->
      <button class="show-all-btn" @click="showAllMatches">전체보기</button>
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
          <td>{{ match.tableIndex }}</td>
          <td>
            <span class="home-team">MMS GS</span>
            <span class="score">{{ match.matchSetScore }}:{{ match.matchOpponentTeamSetScore }}</span>
            <span class="away-team">{{ match.teamName }}</span>
          </td>
          <td>{{ formatTime(match.matchDate) }}</td>
          <td>{{ match.teamStadium }}</td>
          <td>{{ match.matchRoundId }} 라운드</td>
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
      // 시즌 
      seasons: [
        { seasonId: 1, seasonName: '도드람 2023-2024 V-리그' },
        { seasonId: 2, seasonName: '도드람 2024-2025 V-리그' },
      ],
      selectedSeasonId: 2, // 기본 선택 시즌 2024-2025 V-리그로 설정
      matches: [], // 전체 경기 데이터 저장
      currentPage: 0, // 현재 페이지
      totalPages: 0, // 전체 페이지 수
      matchRoundId: 2, // 시작 라운드
      maxRound: 6, // 총 라운드 수
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

    // 라운드 변경
    changeRound(step) {
      const newRound = this.matchRoundId + step;
      if (newRound >= 1 && newRound <= this.maxRound) {
        this.matchRoundId = newRound;
        this.fetchMatches(); // 변경된 라운드의 데이터 호출
      }
    },

    // 선택된 시즌별 데이터 페이징 처리하여 가져오기
    async fetchMatches(page = 0) {
      try {
        const response = await axios.get(`http://localhost:4000/game/results`, {
          params: {
            seasonId: this.selectedSeasonId,
            page: page,
            size: 10, // 10개씩 페이징 처리
            matchRoundId: this.matchRoundId, // 선택된 라운드
            sort: 'matchDate,asc', // matchDate를 기준으로 오름차순 정렬
          },
        });
        // 테이블 인덱스 (순번) 계산
        // 가져온 데이터의 인덱스를 기준으로 index + 1로 계산해서 순번을 1부터 시작하도록 설정
        this.matches = response.data.content.map((match, index) => ({
          ...match,
          tableIndex: index + 1 + page * 6
        }));
        this.totalPages = response.data.totalPages;
        this.currentPage = page;
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },

    // 전체보기 - 선택된 시즌의 전체 데이터 가져오기
    async showAllMatches() {
      this.matchRoundId = ''; // 라운드 선택 초기화
      try {
        const response = await axios.get(`http://localhost:4000/game/results`, 
        {
          params: {
          seasonId: this.selectedSeasonId,
          page: 0,
          size: 1000, // 한 페이지에 최대한 큰 값을 설정하여 모든 데이터를 가져오기
          sort: 'matchDate,asc', // matchDate를 기준으로 오름차순 정렬
          },
        });
        this.matches = response.data.content.map((match, index) => ({
          ...match,
          tableIndex: index + 1,
        }));
        this.totalPages = 1; // 전체보기 시 페이지 1로 고정
        this.currentPage = 0;
      } catch (error) {
        console.error("Error fetching all match data:", error);
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
      const month = String(d.getMonth() + 1).padStart(2, '0'); // 월을 2자리로 표시
      const day = String(d.getDate()).padStart(2, '0'); // 일을 2자리로 표시
      return `${month}.${day} (${['일', '월', '화', '수', '목', '금', '토'][d.getDay()]})`;
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
    this.fetchMatches();  // 초기 페이지 로딩 시 데이터를 가져오도록 설정
  }
};
</script>

<style scoped>
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
.game-results-content {
  padding-top: 30px;
  padding-bottom: 100px;
}

.header-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  margin-bottom: 8px;
}

.select-container {
  position: absolute;
  left: 0px;
  display: flex;
}

select {
  /* background-color: #f8f9fa; */
  -webkit-appearance: none; /* 기본 화살표 제거 */
  -moz-appearance: none; /* Firefox 기본 화살표 제거 */
  appearance: none; /* 기본 화살표 제거 */
  border: 1px solid #ccc;
  padding: 10px 40px;
  padding-right: 50px;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  cursor: pointer;
  background-image: url('@/assets/img/game/select-icon-down.png'); /* 화살표 아이콘 경로 */
  background-repeat: no-repeat;
  background-position: right 15px center; /* 화살표 위치 */
  background-size: 12px; /* 화살표 크기 */
  margin-top: 0;
}

.round-navigation {
  display: flex;
  justify-content: center;
  align-items: center;
}

.round-navigation img {
  width: 40px;
  height: 40px;
  margin: 0 20px;
  cursor: pointer;
  transition: opacity 0.3s; /* 투명도 변경 애니메이션 추가 */
}

.round-navigation img.disabled {
  cursor: not-allowed;
  pointer-events: none;
  opacity: 0.5;
}

.round-select-box {
  position: absolute;
  right: 150px;
  top: 50%;
  transform: translateY(-50%);
}

.show-all-btn {
  border: 1px solid #ccc;
  padding: 10px 30px;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  cursor: pointer;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

/* 테이블 스타일 */
.match-table {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  border-collapse: collapse;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.match-table th, .match-table td {
  border: 1px solid #ccc;
  text-align: center;
  padding: 15px;
}

.match-table th {
  background-color: #f5f5f5;
  font-weight: bold;
}

.home-team, .away-team {
  margin-right: 10px; /* 팀명 사이의 공백 */
}
.score {
  margin: 0 13px; /* 스코어와 팀명 사이의 여백 */
}
</style>