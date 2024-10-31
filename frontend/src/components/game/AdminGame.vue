<template>
  <div class="admin-game-page">

    <!-- 상단 컨트롤 -->
    <div class="filter-controls">
        <span>총 경기 수 : <b>{{ totalMatches }}</b>개</span>
        <button @click="navigateToInsertPage">+ 신규 경기</button>
        <button @click="resetFilters">전체</button>
    </div>

    <!-- 경기 목록 테이블 -->
    <table>
        <thead>
            <tr>
                <th>No</th>
                <th>ID</th>
                <th>
                    <select v-model="selectedSeason" @change="fetchMatches(0)">
                        <option :value="null">시즌</option>
                        <option v-for="season in seasons" :key="season.seasonId" :value="season.seasonId">
                            {{ season.seasonName }}
                        </option>
                    </select>
                </th>
                <th>
                    <select v-model="selectedRound" @change="fetchMatches(0)">
                        <option :value="null">라운드</option>
                        <option v-for="round in rounds" :key="round" :value="round">{{ round }}라운드</option>
                    </select>
                </th>
                <th>
                    <select v-model="selectedSortOrder" @change="fetchMatches(0)">
                        <option :value="{ field: 'matchDate', direction: 'asc' }">경기 날짜 △</option>
                        <option :value="{ field: 'matchDate', direction: 'desc' }">경기 날짜 ▽</option>
                    </select>
                </th>
                <th>
                    <select v-model="selectedStadium" @change="fetchMatches(0)">
                        <option :value="null">경기장</option>
                        <option value="HOME">HOME</option>
                        <option value="AWAY">AWAY</option>
                    </select>
                </th>
                <th>
                    <select v-model="selectedTeam" @change="fetchMatches(0)">
                        <option :value="null">팀 이름</option>
                        <option v-for="team in teams" :key="team.teamId" :value="team.teamId">{{ team.teamName }}</option>
                    </select>                    
                </th>
                <th>우리 팀</th>
                <th>상대 팀</th>
                <th>
                    <select v-model="selectedMailStatus" @change="fetchMatches(0)">
                        <option :value="null">메일 상태</option>
                        <option value="DEFAULT">DEFAULT</option>
                        <option value="REQUIRED">REQUIRED</option>
                    </select>
                </th>
                <th>
                    <select v-model="selectedMatchStatus" @change="fetchMatches(0)">
                        <option :value="null">경기 상태</option>
                        <option value="DEFAULT">DEFAULT</option>
                        <option value="DELETED">DELETED</option>
                    </select>
                </th>
                <th>수정</th>
                <th>삭제</th>
                <th>메일 상태 변경</th>
                <th>메일 발송</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(match, index) in matchList" :key="match.matchId">
                <td>{{ index + 1 + currentPage * pageSize }}</td>
                <td>{{ match.matchId }}</td>
                <td>{{ match.seasonName }}</td>
                <td>{{ match.matchRoundId }}</td>
                <td>{{ match.matchDate }}</td>
                <td>{{ match.matchStadium }}</td>
                <td>{{ match.teamName }}</td>
                <td>{{ match.matchSetScore }}</td>
                <td>{{ match.matchOpponentTeamSetScore }}</td>
                <td>{{ match.matchMailStatus }}</td>
                <td>{{ match.matchStatus }}</td>
                <td>
                    <button @click="navigateToEditPage(match.matchId)">수정</button>
                </td>
                <td>
                    <button v-if="match.matchStatus === 'DEFAULT'" 
                        @click="deactivateMatch(match.matchId)">
                        삭제
                    </button>
                    <button v-else
                    @click="activateMatch(match.matchId)">
                        활성화
                    </button>
                </td>
                <td>
                    <button @click="updateMailStatus(match.matchId)"
                     :disabled="match.matchMailStatus !== 'REQUIRED'">
                        메일 상태 변경
                    </button>
                </td>
                <td>
                    <button @click="sendEmails(match.matchId)"
                    :disabled="match.matchMailStatus !== 'REQUIRED'">
                        메일 발송
                    </button>
                </td>
          </tr>
        </tbody>
    </table>

    <!-- 페이지네이션 컨트롤 -->
    <div class="pagination">
        <div class="pages">
            <button @click="changePage(currentPage - 1)" :disabled="currentPage === 0">&lt;</button>
            <button v-for="page in totalPages" :key="page" @click="changePage(page - 1)" :class="{ active: page === currentPage + 1 }">{{ page }}</button>
            <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages - 1">&gt;</button>
        </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'AdminGame',
    data() {
        return {
            matchList: [],
            totalMatches: 0,
            totalPages: 1,
            currentPage: 0,
            pageSize: 10,

            selectedSeason: 2,
            selectedRound: null,
            selectedStadium: null,
            selectedTeam: null,
            selectedMailStatus: null,
            selectedMatchStatus: null,

            selectedSortOrder: { field: 'matchDate', direction: 'asc' },  // 기본 정렬 순서 설정

            seasons: [{ seasonId: 1, seasonName: '23/24' }, { seasonId: 2, seasonName: '24/25' }],
            rounds: [1, 2, 3, 4, 5, 6],
            teams: [{ teamId: 1, teamName: 'OK저축은행' }, 
                    { teamId: 2, teamName: '한국전력' },
                    { teamId: 3, teamName: '현대캐피탈' },
                    { teamId: 4, teamName: '삼성화재' },
                    { teamId: 5, teamName: 'KB손해보험' },
                    { teamId: 6, teamName: '우리카드' }]
        }
    },
    mounted() {
        this.fetchMatches(this.currentPage);
    },
    methods: {
    fetchMatches(page) {
      axios
        .get(`http://localhost:4000/game/admin`, {
          params: { 
            page: page, 
            size: this.pageSize, 
            seasonId: this.selectedSeason,
            matchRoundId: this.selectedRound,
            matchStadium: this.selectedStadium,
            teamId: this.selectedTeam,
            matchMailStatus: this.selectedMailStatus,
            matchStatus: this.selectedMatchStatus,
            sortField: this.selectedSortOrder.field,  // 정렬 필드
            sortDirection: this.selectedSortOrder.direction // 정렬 방향
            },
        })
        .then((response) => {
          const data = response.data;
          this.matchList = data.content;
          this.totalMatches = data.totalElements;
          this.totalPages = data.totalPages;
          this.currentPage = data.number;
        })
        .catch((error) => {
          console.error('Error fetching match data:', error);
        });
    },
    changePage(page) {
      if (page >= 0 && page < this.totalPages) {
        this.fetchMatches(page);
      }
    },

    // 전체 버튼 클릭 시 필터 초기화하고 첫 페이지 로드
    resetFilters() {
        this.selectedSeason = null;
        this.selectedRound = null;
        this.selectedStadium = null;
        this.selectedTeam = null;
        this.selectedMailStatus = null;
        this.selectedMatchStatus = null;
        this.selectedSortOrder = { field: 'matchDate', direction: 'asc' };  // 기본 정렬로 초기화
        this.fetchMatches(0);
    },

    // 경기 신규 페이지로 이동
    navigateToInsertPage() {
      this.$router.push({ name: 'InsertGame' });
    },

    // 경기 수정 페이지로 이동
    navigateToEditPage(matchId) {
      this.$router.push({ name: 'UpdateGame', params: { matchId } });
    },

    // 경기 삭제(비활성화)
    deactivateMatch(matchId) {
        // 삭제 확인 창 표시
        if (confirm("삭제하시겠습니까?")) {
            axios
                .patch(`http://localhost:4000/game/admin/delete/${matchId}`)
                .then(response => {
                    alert(response.data); // "경기 삭제 성공" 메시지
                    this.fetchMatches(this.currentPage);   // 데이터 목록 갱신
                })
                .catch(error => {
                    console.error("Error deleting match:", error);
                });
        }
    },

    // 경기 활성화
    activateMatch(matchId) {
        this.updateMatchStatus(matchId, 'DEFAULT');
    },

    // 경기 상태 업데이트 메서드 
    updateMatchStatus(matchId, status) {
    axios.patch(`http://localhost:4000/game/admin/update-status/${matchId}`, { status })
        .then(response => {
            alert(response.data); // 상태 업데이트 성공 메시지
            this.fetchMatches(this.currentPage); // 데이터 목록 갱신
        })
        .catch(error => {
            console.error("Error updating match status:", error);
        });
    },

    // 경기 상태 수정
    updateMailStatus(matchId, status = 'DEFAULT') {
        axios
            .patch(`http://localhost:4000/game/admin/update-mail-status/${matchId}`, { mailStatus: status })
            .then(response => {
                alert(response.data); // "메일 상태가 DEFAULT로 변경되었습니다." 메시지
                this.fetchMatches(this.currentPage);
            })
            .catch((error) => {
                console.error("Error updating mail status:", error);
            });
    },

    sendEmails(matchId) {
    axios
        .post(`http://localhost:4000/game/admin/${matchId}/sendEmail`)
        .then(response => {
            alert(response.data); // "메일 발송 완료 및 상태 업데이트 완료" 메시지
            this.fetchMatches(this.currentPage); // 업데이트된 상태 목록 갱신
        })
        .catch((error) => {
            console.error("Error sending email:", error);
        });
    },
  },
};
</script>

<style scoped>
.admin-game-page {
    padding-top: var(--header-height);
    padding-bottom: var(--footer-height);
    margin-top: 40px;
    margin-bottom: 100px;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
}

/* 상단 컨트롤 스타일 */
.filter-controls {
    display: flex;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    max-width: 1400px;
    font-size: 16px;
}

select, button {
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    margin-bottom: 5px;
    font-weight: bold;
    cursor: pointer;
    font-size: 15px;
    text-align: center;
    text-align-last: center;
}

/* 테이블 스타일 */
table {
    width: 100%;
    max-width: 1400px;
    border-collapse: collapse;
    margin-top: 20px;
    font-size: 16px;
}

th, td {
    padding: 12px 8px; /* 셀 간격 조정 */
    border-bottom: 1px solid #ddd;
    text-align: center;
}

thead th {
    background-color: #f8f9fa;
    font-weight: bold;
    color: #333;
}

tbody tr:hover {
    background-color: #f1f1f1; /* 호버 시 색상 */
}

/* 페이지네이션 스타일 */
.pagination {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 20px;
}

.pagination .pages button {
    margin: 0 5px;
    padding: 8px 12px;
    border: none;
    border-radius: 5px;
    background-color: #e9ecef;
    cursor: pointer;
}

.pagination .pages button.active {
    background-color: #007bff;
    color: white;
}

.pagination .pages button:disabled {
    background-color: #ddd;
    cursor: not-allowed;
}
</style>