<template>
  <div class="calendar-main">
      <!-- 부모 컨테이너 -->
      <div class="header-container">
          <!-- 홈경기/원정경기 정보 -->
          <div class="legend">    
              <div class="circle home-circle"></div>
              <span class="legend-text">홈 경기</span>
              <div class="circle away-circle"></div>
              <span class="legend-text">원정 경기</span>
          </div>
          <!-- 셀렉트 박스 및 이전/다음 달 버튼 -->
          <div class="header-controls">
              <img src="@/assets/img/game/calendar-icon-previou.png" alt="이전달" 
              class="nav-button" @click="prevMonth" />
              <select v-model="selectedYear">
                  <option v-for="year in years" :key="year" :value="year">{{ year }}년</option>
              </select>
              <select v-model="selectedMonth">
                  <option v-for="(month, index) in months" :key="index" :value="index + 1">{{ month }}월</option>
              </select>
          <img src="@/assets/img/game/calendar-icon-next.png" alt="다음달" 
              class="nav-button" @click="nextMonth" />
          </div>
      </div>

  <!-- 달력 테이블 -->
  <table class="calendar-table">
      <thead>
          <tr>
          <th>일</th>
          <th>월</th>
          <th>화</th>
          <th>수</th>
          <th>목</th>
          <th>금</th>
          <th>토</th>
          </tr>
      </thead>
      <tbody>
          <tr v-for="week in calendarWeeks" :key="week[0].date">
          <td v-for="day in week" :key="day.date" :class="{ 'home-game': day.events.length && day.events[0].isHomeGame, 'away-game': day.events.length && !day.events[0].isHomeGame }">
              <div class="day-number">{{ day.date }}</div>
              <div v-if="day.events.length">
                  <div v-for="event in day.events" :key="event.id" class="event">
                      <!-- 팀 이름에 따른 로고 이미지 출력 -->
                      <img :src="getTeamLogo(event.team)" alt="Team Logo" class="team-logo" />
                      <!-- 장소 및 시간 -->
                      <div class="event-info">
                          {{ event.location }} {{ event.time }}
                      </div>
                      <!-- 결과 및 점수 (원 안에 승/패/예 출력, 경기 예정인 경우 '예') -->
                      <div class="result-container">
                          <div :class="['result-circle', getResultClass(event.result, event.date)]">
                              <span class="result">{{ getDisplayResult(event.result, event.date) }}</span> <!-- 승/패/예 -->
                          </div>
                          <span class="score">{{ event.score }}</span> <!-- 점수 -->
                      </div>
                      <router-link to="/ticket/purchase">
                          <button :disabled="!isReservationAvailable(event)" class="reservation-button">
                              예매하기
                          </button>
                      </router-link>
                  </div>
              </div>
          </td>
          </tr>
      </tbody>
  </table>
</div>
</template>

<script>
export default {
  name: 'CalendarTotal',
  props: {
    events: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      selectedYear: new Date().getFullYear(),
      selectedMonth: new Date().getMonth() + 1,
      years: [2023, 2024],
      months: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
    };
  },
  methods: {
    // 팀 로고 표시
    getTeamLogo(teamId) {
      switch (teamId) {
        case 1:
          return require('@/assets/img/game/team_id_1_ok.png');
        case 2:
          return require('@/assets/img/game/team_id_2_vixtorm.png');
        case 3:
          return require('@/assets/img/game/team_id_3_walkers.png');
        case 4:
          return require('@/assets/img/game/team_id_4_blueFangs.png');
        case 5:
          return require('@/assets/img/game/team_id_5_stars.png');
        case 6:
          return require('@/assets/img/game/team_id_6_won.png');
        default:
          return ''; // 기본값 (팀 이름이 없으면 빈 이미지)
      }
    },
    getResultClass(result, eventDate) {
      const currentDate = new Date(); // 현재 날짜
      const eventDateObject = new Date(eventDate); // 경기 날짜

      if (!result && eventDateObject > currentDate) {
          return 'upcoming'; // 경기 예정인 경우 'upcoming' 클래스 반환
      }

      if (result === '승') {
          return 'win'; // 승일 경우 'win' 클래스 반환
      } else if (result === '패') {
          return 'lose'; // 패일 경우 'lose' 클래스 반환
      }
          return ''; // 그 외의 경우 기본값 반환
      },
      getDisplayResult(result, eventDate) {
      const currentDate = new Date();
      const eventDateObject = new Date(eventDate);
      if (!result && eventDateObject > currentDate) {
          return '예'; // 경기 예정
      }
      return result || ''; // 경기 결과가 있으면 그 결과 표시
      },
    // 이전 달 출력 함수
    prevMonth() {
    if (this.selectedMonth === 1) {
      this.selectedMonth = 12;
      this.selectedYear--;
      } else {
          this.selectedMonth--;
      }
    },
    // 다음 달 출력 함수
    nextMonth() {
      if (this.selectedMonth === 12) {
          this.selectedMonth = 1;
          this.selectedYear++;
      } else {
      this.selectedMonth++;
      }
    },
      // 현재 시간과 비교하여 예매 가능 여부를 확인하는 함수
      isReservationAvailable(event) {
          const currentDate = new Date();
          const eventDate = new Date(`${event.date} ${event.time}`);
          return eventDate > currentDate; // 현재 시간보다 이후인 경우에만 true
      }
    },
  computed: {
    calendarWeeks() {
      const daysInMonth = new Date(this.selectedYear, this.selectedMonth, 0).getDate();
      const firstDayOfMonth = new Date(this.selectedYear, this.selectedMonth - 1, 1).getDay();
      const weeks = [];
      let days = [];
      
      // 첫 주 빈 칸 채우기
      for (let i = 0; i < firstDayOfMonth; i++) {
        days.push({ date: '', events: [] });
      }

      // 날짜와 이벤트 추가
      for (let day = 1; day <= daysInMonth; day++) {
        const date = `${this.selectedYear}-${String(this.selectedMonth).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        const dayEvents = this.events.filter(event => event.date === date);
        days.push({ date: day, events: dayEvents });
        
        // 주별로 분리
        if (days.length === 7) {
          weeks.push(days);
          days = [];
        }
      }

      // 마지막 주에 빈 칸 채우기
      if (days.length > 0) {
        while (days.length < 7) {
          days.push({ date: '', events: [] });
        }
        weeks.push(days);
      }
      return weeks;
    }
  }
};
</script>

<style scoped>
/* 부모 컨테이너 */
.header-container {
position: relative;
display: flex;
justify-content: center;
align-items: center;
width: 100%;
max-width: 1200px;
margin: 0 auto 20px;
padding-bottom: 10px;
}

/* 홈, 원정 경기 라벨 스타일 */
.legend {
position: absolute;
left: 20px;
bottom: 10px;
display: flex;
align-items: center;
}

.circle {
width: 15px;
height: 15px;
border-radius: 50%;
margin-right: 10px;
box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
}

.home-circle {
background-color: #acc8c1;
}

.away-circle {
background-color: #d9d9d9;
}

.legend-text {
margin-right: 20px;
font-size: 16px;
color: #565656;
}

/* 셀렉트 박스 및 이전, 다음 달 네비게이션 버튼 */
.header-controls {
display: flex;
justify-content: center;
align-items: center;
}

.nav-button {
width: 40px;
height: 40px;
margin: 0 20px;
cursor: pointer;
}

select {
  background-color: #f8f9fa;
  border: none;
  box-shadow: 0 2px 8px rgba(50, 50, 50, 0.2);
  padding: 10px;
  border-radius: 5px;
  font-size: 18px;
  font-weight: bold;
  color: #333;
  outline: none;
  margin: 0 5px;
  transition: border-color 0.3s ease;
  cursor: pointer;
}

/* 달력 테이블 */
.calendar-table {
  width: 100%;
  max-width: 1200px;
  height: auto;
  margin: 0 auto;
  border-collapse: collapse;
}

.calendar-table thead {
  height: 50px; /* 요일 부분의 높이 */
  border-top: 2px solid #4f8578;
  border-bottom: 1px solid #ddd;
}

.calendar-table th {
  height: 50px; /* 요일 부분 셀의 높이 */
  text-align: center;
  vertical-align: middle;
}

.calendar-table tbody {
  height: 1029px; /* 날짜 부분의 높이 */
}

.calendar-table td {
  position: relative;
  width: 170.760px;
  height: 180px;
  border: 1px solid #ddd;
  text-align: center;
  vertical-align: top;
}

.day-number {
  position: absolute;
  top: 10px;
  left: 17px;
  font-size: 14px;
  color: #333;
}

/* 홈 경기 배경색 */
.calendar-table td.home-game {
  background-color: #acc8c1;
}

/* 원정 경기 배경색 */
.calendar-table td.away-game {
  background-color: #d9d9d9;
}

.event {
  display: flex;
  flex-direction: column; 
  justify-content: flex-end;
  align-items: center;  
  text-align: center; 
  width: 100%; 
  height: 100%; 
  margin-top: 13px;
}

.team-logo {
  width: 100%;
  margin-top: 10px;
  max-height: 60px;
  width: auto;
  margin-bottom: 10px;
}

.result-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
  margin-bottom: 10px;
}

/* 승,패 동그라미 */
.result-circle {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #afafaf; /* 기본 색상 */
  color: white;
  font-weight: bold;
  margin-left: auto;
}

/* 승일 경우 동그라미 컬러 */
.win {
  background-color: #52a792;
}

/* 패일 경우 동그라미 컬러 */
.lose {
  background-color: rgb(67, 67, 67);
}

/* 경기 예정 동그라미 컬러 */
.upcoming {
background-color: #afafaf;
/* color: #333; */
}

/* 승,패 텍스트 */
.result {
  font-size: 14px;
  text-align: center;
  align-items: center;
}

.score {
  font-size: 16px;
  font-weight: bold;
  margin-left: 10px;
}

/* 예매하기 버튼 */
.reservation-button {
padding: 10px 20px;
background-color: #fff;
border: none;
border-radius: 8px;
box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
font-size: 14px;
font-weight: bold;
color: #333;
cursor: pointer;
transition: background-color 0.3s, box-shadow 0.3s, font-weight 0.3s;
}

.reservation-button:disabled:hover {
background-color: #cccccc;
box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
cursor: not-allowed;
}

.reservation-button:hover {
box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.reservation-button:disabled {
background-color: #cccccc;
cursor: not-allowed;
color: #858383;
}
</style>