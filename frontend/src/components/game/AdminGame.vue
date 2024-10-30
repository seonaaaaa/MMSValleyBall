<template>
  <div class="admin-game-page">

    <!-- 신규 경기 버튼 -->
    <button @click="navigateToInsertPage">+ 신규 경기</button>

    <!-- 경기 목록 테이블 -->
    <table>
        <thead>
            <tr>
                <th>경기 ID</th>
                <th>경기 날짜</th>
                <th>경기장</th>
                <th>우리 팀 세트</th>
                <th>상대 팀 세트</th>
                <th>시즌</th>
                <th>라운드</th>
                <th>팀 이름</th>
                <th>메일 상태</th>
                <th>경기 상태</th>
                <th>수정</th>
                <th>삭제</th>
                <th>메일 상태 변경</th>
                <th>메일 발송</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="match in matchList" :key="match.matchId">
                <td>{{ match.matchId }}</td>
                <td>{{ match.matchDate }}</td>
                <td>{{ match.matchStadium }}</td>
                <td>{{ match.matchSetScore }}</td>
                <td>{{ match.matchOpponentTeamSetScore }}</td>
                <td>{{ match.seasonName }}</td>
                <td>{{ match.matchRoundId }}</td>
                <td>{{ match.teamName }}</td>
                <td>{{ match.matchMailStatus }}</td>
                <td>{{ match.matchStatus }}</td>
                <td>
                    <button @click="navigateToEditPage(match.matchId)">경기 수정</button>
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
        <span>총 경기 수: {{ totalMatches }}개</span>
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
        }
    },
    mounted() {
        this.fetchMatches(this.currentPage);
    },
    methods: {
    fetchMatches(page) {
      axios
        .get(`http://localhost:4000/game/admin`, {
          params: { page: page, size: this.pageSize }
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
        // 삭제 확인 창을 표시
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
    margin-top: 100px;
    margin-bottom: 100px;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
}

</style>