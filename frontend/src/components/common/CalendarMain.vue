<template>
  <div class="calendar-main">
      <!-- 상단 타이틀 영역 -->
      <h2 class="header-title">
          <!-- 경기 일정  -->
          <span class="current-month">({{ selectedYear }}년 {{ selectedMonth }}월)</span>
          <router-link to="/game/schedule">
            <button class="view-all-btn">전체 일정 보러가기</button>
          </router-link>
      </h2>

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
                <!-- 결과 및 점수 (원 안에) -->
                <div class="result-container">
                  <div :class="['result-circle', getResultClass(event.result)]">
                    <span class="result">{{ event.result }}</span> <!-- 승/패 -->
                  </div>
                  <span class="score">{{ event.score }}</span> <!-- 점수 -->
                </div>
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
  name: 'CalendarMain',
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
    };
  },
  methods: {
    getTeamLogo(team) {
      switch (team) {
        case 'blueFangs':
          return require('@/assets/img/game/blueFangs.png');
        case 'jumbos': 
          return require('@/assets/img/game/jumbos.png');
        case 'ok': 
          return require('@/assets/img/game/ok.png');
        case 'vixtorm': 
          return require('@/assets/img/game/vixtorm.png');
        case 'walkers': 
          return require('@/assets/img/game/walkers.png');
        case 'won': 
          return require('@/assets/img/game/won.png');
        default:
          return ''; // 팀 이름이 없으면 기본값 빈 이미지
      }
    },
    getResultClass(result) {
    if (result === '승') {
      return 'win';
    } else if (result === '패') {
      return 'lose';
    } 
    return '';
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
.header-title {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  max-width: 1200px;
  height: 50px;
  margin: 0 auto 20px auto;
  font-weight: bold;
  position: relative;
}

.current-month {
  color: gray;
  text-align: center;
  flex: 1;
}

.view-all-btn {
  background-color: #f8f9fa;
  border: none;
  box-shadow: 0 2px 8px rgba(50, 50, 50, 0.2);
  padding: 10px;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  outline: none;
  margin-left: 20px;
  transition: border-color 0.3s ease;
  cursor: pointer;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

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

.calendar-table td.home-game {
  /* 홈 경기 배경색 */
  background-color: #acc8c1;
}

.calendar-table td.away-game {
  /* 원정 경기 배경색 */
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
  margin-top: 20px;
}

/* 승,패 동그라미 */
.result-circle {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #333; /* 기본 색상 */
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
</style>