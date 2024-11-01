<template>
  <div class="game-insert-page">
    <h2>신규 경기 입력</h2>

    <form @submit.prevent="insertMatch" class="form-table">
      
      <!-- 경기 날짜 및 시간 -->
      <div class="form-row">
        <label>경기 날짜</label>
        <input type="date" v-model="matchDate" />
        <input type="time" v-model="matchTime" />
      </div>

      <!-- 경기장 선택 -->
      <div class="form-row">
        <label>경기장</label>
        <select v-model="match.matchStadium">
          <option v-for="stadium in stadiumOptions" :key="stadium" :value="stadium">
            {{ stadium }}
          </option>
        </select>
      </div>

      <!-- 점수 입력 -->
      <div class="form-row">
        <label>우리 팀 세트 수</label>
        <input type="number" v-model="match.matchSetScore" required min="0" max="5" />
      </div>
      <div class="form-row">
        <label>상대 팀 세트 수</label>
        <input type="number" v-model="match.matchOpponentTeamSetScore" required min="0" max="5" />
      </div>

      <!-- 시즌 선택 -->
      <div class="form-row">
        <label>시즌</label>
        <select v-model="match.seasonId">
          <option v-for="season in seasonOptions" :key="season.seasonId" :value="season.seasonId">
            {{ season.seasonName }}
          </option>
        </select>
      </div>

      <!-- 라운드 선택 -->
      <div class="form-row">
        <label>라운드</label>
        <select v-model="match.matchRoundId">
          <option v-for="round in rounds" :key="round" :value="round">
            {{ round }}
          </option>
        </select>
      </div>

      <!-- 팀 이름 선택 -->
      <div class="form-row">
        <label>팀 이름</label>
        <select v-model="match.teamId">
          <option v-for="team in teamOptions" :key="team.teamId" :value="team.teamId">
            {{ team.teamName }}
          </option>
        </select>
      </div>

      <!-- 버튼 -->
      <div class="button-group">
        <button type="submit">저장</button>
        <button type="button" @click="navigateBack">돌아가기</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'InsertGame',
  data() {
    return {
      match: {},  // 경기 정보 객체
      matchDate: '', // 분리된 날짜
      matchTime: '', // 분리된 시간
      stadiumOptions: ['HOME', 'AWAY'],
      seasonOptions: [], // 서버에서 가져온 시즌 정보
      rounds: [1, 2, 3, 4, 5, 6],
      teamOptions: [] // 서버에서 가져온 팀 정보
    };
  },
  mounted() {
    this.fetchSeasons();  // 시즌 정보 가져오기
    this.fetchTeams();    // 팀 정보 가져오기
  },
  methods: {
    // 시즌 정보 조회
    fetchSeasons() {
      axios.get('http://localhost:4000/game/admin/seasons')
        .then(response => { this.seasonOptions = response.data; })
        .catch(error => {
          console.error('Error fetching seasons:', error);
          alert('시즌 정보를 불러오는데 오류가 발생했습니다.');
        });
    },
    // 팀 정보 조회
    fetchTeams() {
      axios.get('http://localhost:4000/game/admin/teams')
        .then(response => { this.teamOptions = response.data; })
        .catch(error => {
          console.error('Error fetching teams:', error);
          alert('팀 정보를 불러오는데 오류가 발생했습니다.');
        });
    },
    // 새로운 경기 정보 저장
    insertMatch() {
      // matchDate를 날짜와 시간으로 조합
      this.match.matchDate = `${this.matchDate}T${this.matchTime}`;

      // 새로운 경기 생성 요청
      axios.post(`http://localhost:4000/game/admin/insert`, this.match)
        .then(() => {
          alert('새로운 경기가 등록되었습니다.');
          this.$router.push('/game/admin'); // 저장 후 /game/admin 페이지로 이동
        })
        .catch(error => {
          console.error('Error inserting match:', error);
          alert('경기 등록 중 오류가 발생했습니다.');
        });
    },
    navigateBack() {
      this.$router.push('/game/admin');
    },
  }
};
</script>

<style scoped>
.game-insert-page {
    padding-top: var(--header-height);
    padding-bottom: var(--footer-height);
    margin-bottom: 70px;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.form-table {
    width: 100%;
    max-width: 500px;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
}

.form-row {
    display: flex;
    align-items: center;
    width: 100%;
    margin-bottom: 10px;
}

label {
    width: 120px;
    text-align: left;
    margin-bottom: 10px;
    margin-bottom: 10px;
    margin-right: 50px;
}

input, select, button {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
  font-size: 16px;
  cursor: pointer;
}

input:disabled {
  pointer-events: none; /* 마우스 반응을 비활성화 */
}

.button-group {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  width: 100%;
  margin-top: 20px;
}
</style>