<template>
    <div class="calendar">
      <div class="header">
        <select v-model="selectedYear">
          <option v-for="year in years" :key="year" :value="year">{{ year }}년</option>
        </select>
        <select v-model="selectedMonth">
          <option v-for="(month, index) in months" :key="index" :value="index + 1">{{ month }}월</option>
        </select>
      </div>
  
      <div class="weekdays">
        <div>일</div>
        <div>월</div>
        <div>화</div>
        <div>수</div>
        <div>목</div>
        <div>금</div>
        <div>토</div>
      </div>
  
      <div class="days">
        <div v-for="(day, index) in calendarDays" :key="index" class="day">
          <!-- 수정 -->
          <div class="day-number">{{ day.date }}</div> 
          <div v-if="day.events.length">
            <div v-for="event in day.events" :key="event.id" class="event">
              <!-- 팀 로고 -->
              <img :src="event.logo" alt="Team logo" class="team-logo">
              <!-- 장소 및 시간 -->
              <div class="event-info">
                {{ event.location }}{{ event.time }}
              </div>
              <!-- 결과 및 점수 (원 안에) -->
              <div class="result-circle">
                {{ event.result }} <span class="score">{{ event.score }}</span>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'AppCalendar',
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
        years: [2023, 2024, 2025],
        months: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12']
      };
    },
    computed: {
      calendarDays() {
        const daysInMonth = new Date(this.selectedYear, this.selectedMonth, 0).getDate();
        const firstDayOfMonth = new Date(this.selectedYear, this.selectedMonth - 1, 1).getDay();
        const days = [];
  
        // 빈 칸 추가 (첫 주의 첫 날짜까지)
        for (let i = 0; i < firstDayOfMonth; i++) {
          days.push({ date: '', events: [] });
        }
  
        // 날짜 추가
        for (let day = 1; day <= daysInMonth; day++) {
          const date = `${this.selectedYear}-${String(this.selectedMonth).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
          const dayEvents = this.events.filter(event => event.date === date);
          days.push({ date: day, events: dayEvents });
        }
  
        return days;
      }
    }
  };
  </script>
  
  <style scoped>
select {
  appearance: none; /* 기본 셀렉트 박스 화살표 제거 */
  background-color: #f8f9fa;
  border: 2px solid #ccc;
  padding: 10px;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  outline: none;
  margin: 0 5px;
  transition: border-color 0.3s ease;
  cursor: pointer;
}

select:focus {
  border-color: #60a191; /* 포커스 시 테두리 색상 */
}

select:hover {
  border-color: #4f8578; /* 마우스 오버 시 테두리 색상 */
}

select:disabled {
  background-color: #e9ecef;
  cursor: not-allowed;
}

.calendar {
    display: flex;
    flex-direction: column;
    max-width: 600px;
    margin: 0 auto;
}
  
.header {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
}
  
.weekdays {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    text-align: center;
    margin-bottom: 10px;
}

.days {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    text-align: center;
}
  
.day {
    padding: 10px;
    border: 1px solid #ddd;
    height: 120px;
    position: relative;
}

.day-number {
  position: absolute;
  top: 5px;
  left: 5px;
  font-size: 12px;
  color: #333;
}
  
.event {
    background-color: #4f8578;;
    color: white;
    border-radius: 5px;
    padding: 5px;
    margin-top: 16px;
}

.team-logo {
  width: 40px;
  height: 40px;
  margin-bottom: 10px;
}

.event-info {
  font-size: 14px;
  margin-bottom: 8px;
}

.result-circle {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #333;
  color: white;
  font-weight: bold;
}

.score {
  margin-left: 5px;
}
  </style>  