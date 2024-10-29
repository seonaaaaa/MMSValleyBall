<template>
  <div>
    <table>
        <thead>
            <tr>
                <th>경기 ID</th>
                <th>경기 날짜</th>
                <th>경기장</th>
                <th>우리 팀 점수</th>
                <th>상대 팀 점수</th>
                <th>시즌</th>
                <th>라운드</th>
                <th>팀 이름</th>
                <th>메일 상태</th>
                <th>경기 상태</th>
                <th>수정</th>
                <th>삭제</th>
                <th>메일 상태 변경</th>
                <th>메일 발송</th>
                <!-- <th>Match ID</th>
                <th>Match Date</th>
                <th>Stadium</th>
                <th>Set Score</th>
                <th>Opponent Team Set Score</th>
                <th>Season</th>
                <th>Round</th>
                <th>Team Name</th>
                <th>Mail Status</th>
                <th>Match Status</th> -->
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
                <td><a src="#"><button>수정</button></a></td>
                <td><a src="#"><button>삭제</button></a></td>
                <td><a src="#"><button>메일 상태 변경</button></a></td>
                <td><a src="#"><button>메일발송</button></a></td>
          </tr>
        </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'GameAdmin',
    data() {
        return {
            matchList: [],
        }
    },
    mounted() {
        axios.get('http://localhost:4000/game/admin')
        .then(Response => {
            this.matchList = Response.data;
        })
        .catch(error => {
            console.error('Error fetching match data:', error);
        });
    },
    methods: {

    },
};
</script>

<style>

</style>