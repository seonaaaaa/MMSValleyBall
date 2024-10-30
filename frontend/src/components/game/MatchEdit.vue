<template>
  <div class="game-edit-page">
    <h2>경기 수정</h2>

    <form @submit.prevent="updateMatch">
      <!-- 경기 ID (수정 불가) -->
      <div>
        <label>경기 ID</label>
        <input type="text" :value="match.matchId" disabled />
      </div>
      
      <!-- 경기 날짜 및 시간 -->
      <div>
        <label>경기 날짜</label>
        <input type="date" v-model="matchDate" />
        <input type="time" v-model="matchTime" />
      </div>

      <!-- 경기장 선택 -->
      <div>
        <label>경기장</label>
        <select v-model="match.matchStadium">
          <option v-for="stadium in stadiumOptions" :key="stadium" :value="stadium">
            {{ stadium }}
          </option>
        </select>
      </div>

      <!-- 점수 입력 -->
      <div>
        <label>우리 팀 점수</label>
        <input type="number" v-model="match.matchSetScore" />
      </div>
      <div>
        <label>상대 팀 점수</label>
        <input type="number" v-model="match.matchOpponentTeamSetScore" />
      </div>

      <!-- 시즌 선택 -->
      <div>
        <label>시즌</label>
        <select v-model="match.seasonId">
          <option v-for="season in seasonOptions" :key="season.seasonId" :value="season.seasonId">
            {{ season.seasonName }}
          </option>
        </select>
      </div>

      <!-- 라운드 선택 -->
      <div>
        <label>라운드</label>
        <select v-model="match.matchRoundId">
          <option v-for="round in rounds" :key="round" :value="round">
            {{ round }}
          </option>
        </select>
      </div>

      <!-- 팀 이름 선택 -->
      <div>
        <label>팀 이름</label>
        <select v-model="match.teamId">
          <option v-for="team in teamOptions" :key="team.teamId" :value="team.teamId">
            {{ team.teamName }}
          </option>
        </select>
      </div>

      <button type="submit">저장</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MatchEdit',
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
    this.fetchMatchDetails();
    this.fetchSeasons();  // 시즌 정보 가져오기
    this.fetchTeams();    // 팀 정보 가져오기
  },
  methods: {
    // matchId로 경기 정보 조회
    fetchMatchDetails() {
      const matchId = this.$route.params.matchId;
      axios.get(`http://localhost:4000/game/admin/${matchId}`)
        .then(response => {
          this.match = response.data;
          const [date, time] = this.match.matchDate.split('T'); // matchDate에서 날짜와 시간 분리
          this.matchDate = date;
          this.matchTime = time;
        })
        .catch(error => {
          console.error('Error fetching match details:', error);
          alert('경기 정보를 불러오는데 오류가 발생했습니다.');
        });
    },
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
    // 경기 정보 업데이트
    updateMatch() {
      // matchDate를 날짜와 시간으로 조합
      this.match.matchDate = `${this.matchDate}T${this.matchTime}`;

      // 경기 업데이트 요청
      axios.put(`http://localhost:4000/game/admin/update/${this.match.matchId}`, this.match)
      .then(() => {
        alert('경기 정보가 업데이트되었습니다.');
      
        // 업데이트 성공 시 mail status를 REQUIRED로 설정하는 요청
        return axios.patch(`http://localhost:4000/game/admin/update-mail-status/${this.match.matchId}`, {
          mailStatus: 'REQUIRED'
        });
      })
      .then(() => {
        alert('메일 상태가 REQUIRED로 설정되었습니다.');
        // 수정 후 /game/admin 페이지로 이동
        this.$router.push('/game/admin');
      })
      .catch(error => {
        console.error('Error updating match:', error);
        alert('경기 정보 또는 메일 상태 업데이트 중 오류가 발생했습니다.');
      });
    }
  }
};
</script>

<style scoped>
.game-edit-page {
    padding-top: var(--header-height);
    padding-bottom: var(--footer-height);
    margin-top: 100px;
    margin-bottom: 100px;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
}

</style>