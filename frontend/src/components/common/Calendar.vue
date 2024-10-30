<template>
  <!-- main & game 페이지에서 Calendar 공통 사용 -->
  <div class="calendar-main">
    <!-- 상단 타이틀 및 공통 라벨 영역 -->
    <div class="header-title">
      <div class="legend">
        <div class="circle home-circle"></div>
        <span class="legend-text">홈 경기</span>
        <div class="circle away-circle"></div>
        <span class="legend-text">원정 경기</span>
      </div>
      
      <!-- Main 모드와 Total 모드의 타이틀 차이점 반영 -->
      <h2 v-if="calendarMode === 'main'" class="current-month">
        ({{ selectedYear }}년 {{ selectedMonth }}월)
      </h2>

      <div v-if="calendarMode === 'total'" class="header-controls">
        <img src="@/assets/img/game/nav-icon-previous.png" alt="이전달" class="nav-button" @click="prevMonth" />
        <select v-model="selectedYear">
          <option v-for="year in years" :key="year" :value="year">{{ year }}년</option>
        </select>
        <select v-model="selectedMonth">
          <option v-for="(month, index) in months" :key="index" :value="index + 1">{{ month }}월</option>
        </select>
        <img src="@/assets/img/game/nav-icon-next.png" alt="다음달" 
            class="nav-button" @click="nextMonth" />
      </div>

      <!-- Main 모드에만 표시되는 전체 일정 보러가기 버튼 -->
      <router-link v-if="calendarMode === 'main'" to="/game/schedule">
        <button class="view-all-btn">전체 일정 보러가기</button>
      </router-link>
      
      <!-- Total 모드에만 표시되는 티켓 예매하기 버튼 -->
      <router-link v-if="calendarMode === 'total'" to="/ticket/purchase">
        <button class="ticket-purchase-btn">티켓 예매하기</button>
      </router-link>
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
          <td v-for="day in week" :key="day.date" :class="{ 'home-game': day.events.length && day.events[0].matchStadium === 'HOME', 'away-game': day.events.length && day.events[0].matchStadium === 'AWAY' }">
            <div class="day-number">{{ day.date }}</div>
            <div v-if="day.events.length">
              <div v-for="event in day.events" :key="event.matchId" class="event">
                <img :src="getTeamLogo(event.teamId)" alt="Team Logo" class="team-logo" />
                <div class="event-info">{{ formatLocationAndTime(event) }}</div>
                <div class="result-container">
                  <div :class="['result-circle', getResultClass(event.matchSetScore, event.matchOpponentTeamSetScore)]">
                    <span class="result">{{ getDisplayResult(event.matchSetScore, event.matchOpponentTeamSetScore) }}</span>
                  </div>
                  <span class="score">{{ event.matchSetScore }}:{{ event.matchOpponentTeamSetScore }}</span>
                </div>
                <router-link v-show="isReservationVisible(event)" to="/ticket/purchase">
                  <button :disabled="!isReservationEnabled(event)" class="reservation-button">예매하기</button>
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
  name: 'CalendarComponent',
  props: {
    events: {
      type: Array,
      default: () => []
    },
    calendarMode: {
      type: String,
      default: 'total'
    }
  },
  data() {
    return {
      // 현재 선택된 연도와 월을 데이터로 저장
      selectedYear: new Date().getFullYear(),
      selectedMonth: new Date().getMonth() + 1,
      years: [2023, 2024, 2025],
      months: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
    };
  },
  methods: {
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
    // 경기 장소와 시간을 포맷팅
    formatLocationAndTime(match) {
      const date = new Date(match.matchDate);
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const time = `${hours}:${minutes}`;
      let location = '';

      // HOME 경기일 경우 장소에 '서울 하이' 출력
      if (match.matchStadium === 'HOME') {
        location = '서울 하이';
      } else {
        // AWAY 경기일 경우 상대 팀 ID에 따라 특정 장소 설정
        if (match.matchOpponentTeamId === 2) {
          location = '수원';
        } else if (match.matchOpponentTeamId === 5) {
          location = '의정부';
        } else {
          // 그 외의 AWAY 경기일 경우 '지역 + 체육관명' ex. '서울 장충'
          location = match.teamStadium.slice(0, 2) + ' ' + match.teamStadium.slice(2, -3);
        }
      }
      return `${location} ${time}`;
    },
    // 경기 결과 및 일정에 따라 CSS 클래스 반환
    getResultClass(matchSetScore, matchOpponentTeamSetScore, matchDate) {
      const currentDate = new Date();
      const eventDate = new Date(matchDate);
      
      if (eventDate > currentDate) return 'upcoming'; // 경기 예정일 경우
      if (matchSetScore > matchOpponentTeamSetScore) return 'win';
      else if (matchSetScore < matchOpponentTeamSetScore) return 'lose';
    },
    // 경기 결과 및 일정에 따른 표시 텍스트 반환
    getDisplayResult(matchSetScore, matchOpponentTeamSetScore, matchDate) {
      const currentDate = new Date();
      const eventDate = new Date(matchDate);

      if (eventDate > currentDate) return '예'; // 경기 예정 표시
      return matchSetScore > matchOpponentTeamSetScore ? '승' : '패';
    },
    // 이전 달 출력
    prevMonth() {
      if (this.selectedMonth === 1) {
        this.selectedMonth = 12;
        this.selectedYear--;
      } else {
        this.selectedMonth--;
      }
    },
    // 다음 달 출력
    nextMonth() {
      if (this.selectedMonth === 12) {
        this.selectedMonth = 1;
        this.selectedYear++;
      } else {
        this.selectedMonth++;
      }
    },
    // 예매하기 버튼 보이기
    isReservationVisible(event) {
      const eventDate = new Date(event.matchDate);
      const eventMonth = eventDate.getMonth() + 1;
      const eventYear = eventDate.getFullYear();

      const currentDate = new Date();
      const currentMonth = currentDate.getMonth() + 1;
      const currentYear = currentDate.getFullYear();

      // 현재 달 확인
      const isInCurrentMonth = eventMonth === currentMonth && eventYear === currentYear;

      // 다음 달 확인 (연말에서 연초로 넘어가는 경우 포함)
      const isInNextMonth = 
        (currentMonth === 12 && eventMonth === 1 && eventYear === currentYear + 1) || 
        (eventMonth === currentMonth + 1 && eventYear === currentYear);
      
      // 현재 달 또는 다음 달일 때만 true 반환
      return isInCurrentMonth || isInNextMonth;
    },
    // 예매하기 버튼 활성화
    isReservationEnabled(event) {
      const currentDate = new Date();
      const eventDate = new Date(event.matchDate);

      // 현재 날짜보다 과거라면 비활성화
      if (currentDate > eventDate) return false;

      // 경기 7일 전 13시 이후부터 활성화
      const sevenDaysBeforeEvent = new Date(eventDate);
      sevenDaysBeforeEvent.setDate(eventDate.getDate() - 7);
      sevenDaysBeforeEvent.setHours(13, 0, 0);

      return currentDate >= sevenDaysBeforeEvent;
    }
  },
  computed: {
    // 달력에 표시할 주간 데이터를 계산
    calendarWeeks() {
      const daysInMonth = new Date(this.selectedYear, this.selectedMonth, 0).getDate();
      const firstDayOfMonth = new Date(this.selectedYear, this.selectedMonth - 1, 1).getDay();
      const weeks = [];
      let days = [];
      
      // 첫 주 빈 칸 채우기
      for (let i = 0; i < firstDayOfMonth; i++) {
        days.push({ date: '', events: [] });
      }

      // 각 날짜에 이벤트 추가
      for (let day = 1; day <= daysInMonth; day++) {
        const date = `${this.selectedYear}-${String(this.selectedMonth).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        const dayEvents = this.events.filter(event => new Date(event.matchDate).toISOString().split('T')[0] === date);
        days.push({ date: day, events: dayEvents });

        if (days.length === 7) {
          weeks.push(days);
          days = [];
        }
      }

      // 마지막 주에 빈 칸 채우기
      if (days.length > 0) {
        while (days.length < 7) days.push({ date: '', events: [] });
        weeks.push(days);
      }
      return weeks;
    }
  }
};
</script>

<style scoped>
/* Calendar 상단 공통 */
.header-title {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 60px;
  max-width: 1200px;
  margin: 0 auto 30px;
  padding-bottom: 10px;
  font-weight: bold;
}

/* 홈, 원정 경기 라벨 스타일 */
.legend {
  position: absolute;
  left: 20px;
  bottom: 13px;
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
  font-size: 16px;
  color: #565656;
  margin-right: 20px;
}

/* Main 모드 스타일 */
.current-month {
  color: gray;
  text-align: center;
  flex: 1;
}

.view-all-btn {
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border: none;
  box-shadow: 0 2px 4px rgba(50, 50, 50, 0.2);
  padding: 10px;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-left: 20px;
  transition: border-color 0.3s ease;
  cursor: pointer;
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

/* Total 모드 스타일 */
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

.header-controls select:first-of-type {
  /* 연도와 월 셀렉트 박스 사이의 여백 */
  margin-right: 10px;
}

select {
  -webkit-appearance: none; /* 기본 화살표 제거 */
  -moz-appearance: none; /* Firefox 기본 화살표 제거 */
  appearance: none; /* 기본 화살표 제거 */

  border: 1px solid #ddd;
  background-color: #f8f9fa;
  border: none;
  box-shadow: 0 2px 4px rgba(50, 50, 50, 0.2);
  /* padding: 10px; */
  padding: 10px 30px;
  padding-right: 40px;

  border-radius: 5px;
  font-size: 18px;
  font-weight: bold;
  color: #333;
  outline: none;
  margin: 0 5px;
  cursor: pointer;

  background-image: url('@/assets/img/game/select-icon-down.png'); /* 화살표 아이콘 경로 */
  background-repeat: no-repeat;
  background-position: right 15px center; /* 화살표 위치 */
  background-size: 12px; /* 화살표 크기 */
}

/* 상단 티켓 예매하기 버튼 */
.ticket-purchase-btn {
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border: none;
  box-shadow: 0 2px 4px rgba(50, 50, 50, 0.2);
  padding: 10px 20px;
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

.ticket-purchase-btn:hover {
  transform: translateY(-50%) scale(1.05);
  color: #222;
}

/* Calendar Table 스타일 공통 */
/* 달력 테이블 */
.calendar-table {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  border-collapse: collapse;
  height: 100%;
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
  width: 170px;
  height: 180px;
  border: 1px solid #ddd;
  text-align: center;
  vertical-align: middle;
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
  justify-content: center;
  align-items: center;  
  text-align: center; 
  width: 100%; 
  height: 100%; 
}

.team-logo {
  width: 100%;
  max-height: 60px;
  width: auto;
  margin-bottom: 5px;
}

.result-container {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-top: 10px;
  margin-bottom: 10px;
}

/* 동그라미 */
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

.win {
  background-color: #52a792;
}

.lose {
  background-color: rgb(67, 67, 67);
}

.upcoming {
background-color: #afafaf;
}

/* 동그라미 안 텍스트 */
.result {
  font-size: 14px;
  text-align: center;
  align-items: center;
}
/* 점수 텍스트 */
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
box-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
font-size: 14px;
font-weight: bold;
color: #333;
cursor: pointer;
}

.reservation-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
  color: #858383;
}

.reservation-button:hover:not(:disabled) {
  transform: scale(1.05);
  color: #222;
}
</style>