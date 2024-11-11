<template>
    <!-- 모달 창 -->
    <div id="modal" class="modal">
        <!-- 모달 내용 -->
        <div class="first-modal-content">
            <!-- 모달 제목 -->
            <div class="modal-title" v-if="firstPage">
                <p class="match-info-title"><strong>{{ formatMatchInfo( receivedModalData.matchInfo.matchTeam, thisTeam) }}</strong></p>
                <div class="match-info">
                    <p>{{ receivedModalData.matchInfo.matchStadium }}&nbsp;</p>
                    <p> | &nbsp;</p> 
                    <p v-html="formatDate(receivedModalData.matchInfo.matchDate)"></p>
                </div>
                <hr class="divider" />
            </div>
            <!-- 모달 디테일 -->
            <div v-if="firstPage">
                <div class="modal-body">
                    <!-- 모달 이미지 -->
                    <div class="modal-ticket-img" v-if="selectedZoneImage">
                        <img :src="selectedZoneImage" alt="선택한 구역 이미지" class="stadium-image" />
                    </div>
                    <!-- 모달 표 -->
                    <div class="modal-table-container">
                        <table class="modal-table">
                            <thead class="table-theader">
                                <tr>
                                    <th>구역</th>
                                    <th>잔여석</th>
                                </tr>
                            </thead>
                            <tbody v-for="zone in filteredAvailableSeats" :key="zone.zoneName">
                                <tr>
                                    <td>
                                        <button class="select-zone-btn" @click="toggleZoneSelection(zone)"
                                        :style="{ backgroundColor: getButtonColor(zone.zoneName) }">
                                            {{ zone.zoneName }}
                                        </button>
                                    </td>
                                    <td>{{ zone.availableSeatAmount }}석</td>
                                </tr>

                                <!-- 선택한 구역의 섹션 정보 표시 -->
                                <tr v-if="zoneSelection === zone">
                                    <td colspan="2">
                                        <table class="modal-table-hide">
                                            <tbody>
                                                <tr v-for="section in zone.sections" :key="section.seatId">
                                                    <td>{{ section.sectionName }}</td>
                                                    <td>
                                                        <div class="quantity-selector">
                                                            <button @click="decreaseQuantity(section)" 
                                                                    :disabled="section.quantity <= 0">-</button>
                                                            <input type="text" v-model="section.quantity" readonly class="quantity-box" />
                                                            <button @click="increaseQuantity(section)">
                                                                +
                                                            </button>
                                                        </div>
                                                    </td>
                                                    <td>{{ section.availableSeatAmount }}석</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <hr class="divider" />

                <div class="result-container">
                    <div class="result-text">
                        구역을 선택하면 좌석은 자동 배정됩니다.
                        <div>
                            선택한 좌석 : {{ sectionSelection ? sectionSelection.sectionName : ' ' }}구역 {{ countTicket }}매
                        </div>
                        <div class="result-text-all">
                            총 : {{ countTicket }}매
                        </div>
                    </div>
                
                    <!-- 버튼 영역 -->
                    <div class="first-button">
                        <!-- <button class="hiden-button" ref="autoClickButton" @click="handleClick"></button> -->
                        <!-- 결제 버튼 -->
                        <button class="buy-button" @click="openSecondPage">다음</button>

                        <!-- 모달 종료버튼 -->
                        <button class="close-button" @click="closeModal">닫기</button>
                    </div>
                </div>
            </div>
            <!-- ------------------------------------------------------------------ -->

            <!-- 2번째 모달  -->
            <div v-if="secondPage" class="second-modal-content">
                <div class="modal-body second-modal-body">
                    <div class="left-body">
                        <!-- 경기 정보 -->
                        <div class="second-modal-left">
                            <div class="modal-second-title">
                                <p id="second-modal-match-info-title"><strong>{{ formatMatchInfo(receivedModalData.matchInfo.matchTeam, thisTeam) }}</strong></p>
                                <div id="second-modal-match-info">
                                    <p>{{ receivedModalData.matchInfo.matchStadium }}&nbsp;</p>
                                    <p> | &nbsp;</p> 
                                    <p v-html="formatDate(receivedModalData.matchInfo.matchDate)"></p>
                                </div>
                            </div>
                            <!-- 왼쪽 표 -->
                            <div class="left-table">
                                <div class="modal-table-container">
                                    <table class="modal-table second-modal-left-table">
                                        <tbody>
                                            <tr class="table-theader">
                                                <th>구역</th>
                                                <th>매수</th>
                                                <th>금액</th>
                                                <th>멤버십할인</th>
                                            </tr>
                                            <tr>
                                                <td> {{ seatSelection.sectionName }}구역 </td>
                                                <td> {{ seatSelection.quantity }}매</td>
                                                <td> {{ seatSelection.seatPrice }}원</td>
                                                <td> {{ receivedModalData.userMembership.membershipDiscount }} %</td>
                                            </tr>
                                            <tr class="table-theader">
                                                <th colspan="4">총액</th>
                                            </tr>
                                            <tr>
                                                <td colspan="4">{{ formattedTotal }}원</td>
                                            </tr>
                                        </tbody>
                                    </table>

                                    <div class="modal-info-box">
                                        <div class="icon">
                                            <img src="@/assets/img/anyImg/bell-icon.png" alt="alert" />
                                        </div>
                                        <div class="info-content">
                                            <h4>예매 마감안내</h4>
                                            <ul class="notice">
                                                <li> - 당일 경기 시작 전 1시간 까지 가능</li>
                                                <li> - 취소 시간 이후 구매한 티켓은 취소 및 좌석 변경이 되지 않습니다.</li>
                                            </ul>
                                            <h4>예매 취소안내</h4>
                                            <ul class="notice">
                                                <li> - 당일 경기 시작 12시간 전까지 가능</li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                <div class="vertical-divider"></div>
                    
                    <div class="right-table">
                   
                        <div class="ticket-info-content">
                            <p><strong>[예매정보]</strong></p>
                            <p>경기정보: {{ formatMatchInfo(receivedModalData.matchInfo.matchTeam, thisTeam) }}</p>
                            <p>경기장: {{ receivedModalData.matchInfo.matchStadium }}</p>
                            <p v-html="`경기일시: ${formatDate(receivedModalData.matchInfo.matchDate)}`"></p>
                            <p>구역: <strong>{{ seatSelection.sectionName }}</strong>구역 / <strong>{{ seatSelection.quantity }}</strong>매</p>
                        </div>
                        <hr class="divider" />

                        <div>
                            <table class="second-modal-table" id="second-modal-ticke-price">
                                <tbody>
                                    <tr>
                                        <td>티켓 금액</td>
                                        <td>
                                            {{ formattedTotal }}원
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>멤버십 할인</td>
                                        <td>
                                            {{ user.membership }} | {{ receivedModalData.userMembership.membershipDiscount }} %
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>총 결제 금액</td>
                                        <td>
                                            {{ new Intl.NumberFormat('ko-KR').format(payment()) }}원
                                        </td>
                                    </tr>
                                </tbody>
                        </table>
                        <div class="charge">
                            <div class="charge-money">
                                <p><strong>이용가능한 충전금액 : </strong></p>
                                <p>{{ new Intl.NumberFormat('ko-KR').format(user.balance) }} 원</p>
                            </div>
                            <button id="btn-charge" v-if="leftMoney() < 0" @click="openRechargeWindow">충전하기</button>
                            <div class="membership-price">
                                <p>결제금액</p>
                                <p>- {{  new Intl.NumberFormat('ko-KR').format(payment()) }} 원</p>
                            </div>
                            <div class="left-money">
                                <p>잔액</p>
                                <!-- leftMoney 값이 0보다 작으면 'negative' 클래스를 추가 -->
                                <p :class="{ 'negative': leftMoney() < 0 }">{{ new Intl.NumberFormat('ko-KR').format(leftMoney()) }} 원</p>
                            </div>
                        </div>
                    </div>

                    <div>
                        <p class="p">
                            취소기한 : {{ calculateCancellationDeadline(receivedModalData.matchInfo.matchDate) }}<br>
                            <!-- 링크 -->
                            취소수수료 : 
                            <router-link to="/ticket/info">[상세보기]</router-link>
                        </p>
                    </div>

                    <div class="second-button">
                        <!-- 결제 버튼 -->
                        <button v-if="leftMoney() >= 0" class="buy-button" @click="purchaseTicket">예매 하기</button>
                        <!-- 모달 종료버튼 -->
                        <button class="close-button" @click="openFirstPage">취소</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'TicketModal',
    props: {
        receivedModalData: {
            type: Object,
            required: true,
        },
        user: {
            type: Object,
            required: true,
        },
    },
    computed: {
        selectedZones() {
            // 수량이 1 이상인 구역만 필터링
            return this.receivedModalData.availableSeatsList
                .filter(zone => zone.availableSeatAmount > 0 && zone.isChecked)
                .map(zone => ({
                    ...zone,
                    sections: zone.sections || [],
                    quantity: zone.sections.reduce((total, section) => total + (section.quantity || 0), 0), // 섹션의 수량 합계
                    price: zone.price * zone.quantity // 가격 계산
                }));
        },
        filteredAvailableSeats() {
            // 멤버십이 GOLD를 포함하는 경우 GOLD 구역을 포함하고, 그렇지 않으면 GOLD 구역을 제외
            let list = this.receivedModalData.availableSeatsList.filter(zone => 
                this.user.membership === 'gold' ? 
                (zone.availableSeatAmount > 0) : 
                (zone.availableSeatAmount > 0 && zone.zoneName !== 'GOLD')
            );
            return list;
        },
        formattedTotal() {
            // 숫자를 세 자리마다 쉼표가 붙은 문자열로 변환
            return this.total.toLocaleString();
        },
    },
    watch: {
        // 버튼의 수량이 변경되었을 때, 모든 수량이 0이면 activeButtonIndex 초기화
        zones: {
            handler() {
                if (this.zones.every(zone => zone.quantity === 0)) {
                    this.activeButtonIndex = null;
                }
            },
            deep: true
        },
    },
    mounted() {
        this.initializeSectionQuantities();
    },
    data() {
        return {
            firstPage: true,
            secondPage: false,
            // 좌석 선택 관련
            zones: [],  // 초기값 설정
            zoneSelection: null,
            sectionSelection: null,
            selectedZoneImage: require('@/assets/img/stadium/stadium-main.jpg'),
            // 좌석 계산
            countTicket: 0,
            selectedZone: null,
            seatSelection : {
                seatId : null,
                sectionName : '',
                seatPrice: null,
                quantity: 0
            },
            total: 0,
            thisTeam: "GS ITM",
        };
    },
    methods: {
        // 예매하기 버튼 클릭 시 호출되는 메서드
        async purchaseTicket(){
            const requestBody = {
                matchId: this.receivedModalData.matchInfo.matchId,
                ticketDetailAmount: this.seatSelection.quantity,
                ticketDetailSeat: this.seatSelection.seatId,
                ticketPaidPrice: this.payment(),
            };
            // axios 요청
            try {
                const response = await axios.post("/ticket/purchase/completed", requestBody);
                console.log(response.data);
                alert('티켓 구매 성공');
                this.$router.push({ path: '/myPage/reservations' }); // 마이페이지로 리다이렉트
            } catch (error) {
                const errorMessage = error.response ? error.response.data : error.message;
                console.error('Error purchasing ticket:', errorMessage); // 에러 메시지 로깅
                alert('티켓 구매 실패: ' + errorMessage); // 사용자에게 구체적인 에러 메시지 표시
            }
        },
        // 경기 정보 형식 지정 메서드
        formatMatchInfo(matchTeam, thisTeam) {
            return matchTeam === "서울하이체육관" ? `${thisTeam} VS ${matchTeam}` : `${matchTeam} VS ${thisTeam}`;
        },
        // 날짜 포맷팅 메서드
        formatDate(date) {
            const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
            const dateObj = new Date(date);
            // 날짜 포맷
            const formattedDate = dateObj.toLocaleDateString('ko-KR', options);
            // 요일 가져오기
            const days = ['일', '월', '화', '수', '목', '금', '토'];
            const dayName = days[dateObj.getDay()];
            // 시간 포맷
            const formattedTime = dateObj.toLocaleTimeString('ko-KR', {
            hour: '2-digit',
            minute: '2-digit'
            });
            return `${formattedDate} (${dayName}) ${formattedTime}`; // <br> 제거
        },
        // 총합
        calculateTotal() {
            this.total = this.seatSelection.seatPrice * this.seatSelection.quantity;
        },
        //두 번째 모달에 전달할 좌석 정보
        createSeatSelection(){
            const matchingSeat = this.receivedModalData.seatDTOList.find(seat => seat.seatId === this.sectionSelection.seatId);
            const seatPrice = matchingSeat ? matchingSeat.seatPrice : 0; // 매칭된 좌석이 없을 경우 기본값 0
            this.seatSelection = {
                seatId: this.sectionSelection.seatId,
                sectionName: this.sectionSelection.sectionName,
                quantity: this.countTicket,
                seatPrice: seatPrice
            };
        },
        payment(){
            return  this.total * (100 - this.receivedModalData.userMembership.membershipDiscount) * 0.01;
        },
        leftMoney(){
            return this.user.balance-this.payment();
        },
        // 첫 모달창으로 이동
        openFirstPage() {
            this.firstPage = true;
            this.secondPage = false;
            this.total = 0;
        },
        // 두번째 모달창으로 이동
        openSecondPage() {
            // 좌석이 선택되지 않은 경우 경고창 띄우기
            if (this.countTicket === 0) {
                alert("좌석을 선택해주세요.");
                return;
            }
            this.createSeatSelection();
            this.calculateTotal();
            if (this.total > 0) {
                this.firstPage = false;
                this.secondPage = true;
            } else {
                alert("좌석을 선택해주세요.");
            }
        },
        increaseQuantity(section) {
            // 다른 섹션의 수량 초기화
            this.zoneSelection.sections.forEach(sec => {
                if (sec !== section) {
                    sec.quantity = 0; // 선택되지 않은 섹션의 수량 초기화
                    this.countTicket = 0;
                }
            });
            if(section.quantity == 4 && section.availableSeatAmount > section.quantity) {
                this.countTicket = section.quantity;
            }
            if (section.quantity < 4 && section.availableSeatAmount > section.quantity) {
                section.quantity++;
                this.countTicket = section.quantity;
                this.sectionSelection = section; // 선택된 섹션 정보 저장
            }
            // 섹션 이름에서 첫 글자 추출
            const sectionName =section.sectionName;
            const firstChar = sectionName.charAt(0).toUpperCase(); // 첫 글자
            const secondChar = sectionName.split('/')[1]?.charAt(0).toUpperCase(); // '/' 다음 첫 글자
            console.log(`선택된 구역 이름: ${firstChar}${secondChar}`);
            // 파일 경로 생성
            if (secondChar) {
                this.selectedZoneImage = require(`@/assets/img/stadium/stadium-${firstChar}${secondChar}.jpg`); // 조합된 파일명
            } else {
                this.selectedZoneImage = require('@/assets/img/stadium/stadium-main.jpg'); // 기본 이미지
            }
        },
        decreaseQuantity(section) {
            if (section.quantity > 0) {
                section.quantity--;
                this.countTicket--;
            }
            // 섹션 이름에서 첫 글자 추출
            const sectionName =section.sectionName;
            const firstChar = sectionName.charAt(0).toUpperCase(); // 첫 글자
            const secondChar = sectionName.split('/')[1]?.charAt(0).toUpperCase(); // '/' 다음 첫 글자
            console.log(`선택된 구역 이름: ${firstChar}${secondChar}`);
            // 파일 경로 생성
            if (secondChar) {
                this.selectedZoneImage = require(`@/assets/img/stadium/stadium-${firstChar}${secondChar}.jpg`); // 조합된 파일명
            } else {
                this.selectedZoneImage = require('@/assets/img/stadium/stadium-main.jpg'); // 기본 이미지
            }
        },
        toggleZoneSelection(zone) {
            //수량 초기화
            this.initializeSectionQuantities();
            this.zoneSelection = this.zoneSelection === zone ? null : zone;
        },
        toggleSectionQuantity(section) {
            //수량 초기화
            this.initializeSectionQuantities();
            // 다른 섹션의 매수가 변화할 때 기존 선택된 섹션의 수량을 0으로 초기화
            this.zoneSelection.sections.forEach(sec => {
                if (sec !== section) {
                    sec.quantity = 0; // 선택되지 않은 섹션의 수량 초기화
                    this.countTicket = 0;
                }
            });
            this.sectionSelection = section; // 현재 섹션을 선택
        },
        onCheckboxChange(zone) {
            //수량 초기화
            this.initializeSectionQuantities();
            if (this.isChecked) {
                this.zoneSelection = zone;
            } else {
                this.hideDetails();
            }
        },
        hideDetails() {
            this.countTicket = 0; // 숫자 값으로 초기화
            this.zones.forEach(zone => {
                zone.quantity = 0; // 모든 구역의 수량을 0으로 설정
            });
            this.selectedZoneImage = require('@/assets/img/stadium/stadium-main.jpg'); // 이미지도 초기화
        },
        initializeSectionQuantities() {
            //countTicket 초기화
            this.countTicket = 0;
            // 각 섹션의 수량을 초기화
            this.receivedModalData.availableSeatsList.forEach(zone => {
                zone.sections.forEach(section => {
                    section.quantity = 0; // 수량 초기화
                });
            });
        },
        // 모달 열기 닫기
        closeModal() {
            this.hideDetails();
            this.firstPage = true;
            this.secondPage = false;
            this.$emit('closeModal'); // 모달 종료 이벤트 발생
        },

        // 버튼 색깔을 반환하는 메서드
        getButtonColor(zoneName) {
            switch (zoneName) {
                case 'GOLD':
                    return '#FFE400';
                case 'BLUE':
                    return '#3162C7';
                case 'AWAY':
                    return '#8C8C8C';
                case 'EAST':
                    return '#993800';
                default:
                    return '#47C83E'; // 나머지 구역
            }
        },

        //취소기한 자동 계산
        calculateCancellationDeadline(date) {
            const dateObj = new Date(date);
            dateObj.setHours(0, 0, 0, 0); // 당일 오전 12시로 설정
            return dateObj.toLocaleString('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
            });
        },
        openRechargeWindow() {
            const width = 570;
            const height = 275;
            const left = (window.screen.width / 2) - (width / 2); // 화면 중앙에 위치
            const top = (window.screen.height / 2) - (height / 2);
            window.open(`/myPage/rechargee`, '충전하기',
            `width=${width},height=${height},,top=${top},left=${left},
            toolbar=no,menubar=no,scrollbars=no,resizable=no,fullscreen=no`);
        },
    },
}
</script>


<style scoped>
.modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.1);
    /* 모달 뒤 어두운 배경 */
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000
}

.first-modal-content {
    background: white;
    padding: 20px;
    border-radius: 10px;
    max-width: 1000px; /* 모달 창 너비 줄임 */
    max-height: 800px; /* 모달 창 높이 줄임 */
    width: 1000px; /* 모달 창 너비 줄임 */
    height: 800px; /* 모달 창 높이 줄임 */
    position: relative;
    overflow-y: auto; /* 세로 스크롤 추가 */
}

.second-modal-content {
    background: white;
    padding: 20px;
    border-radius: 10px;
    max-width: 1000px;
    max-height: 800px;
}

.second-modal-body {
    display: flex;
    flex-direction: row;
    justify-content: space-between; /* 양쪽 정렬 */
    gap: 10px; /* left-body와 right-table 간격 */
    width: 100%;
}

.left-body{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}
.modal-title {
    text-align: center;
    /* 타이틀을 가운데 정렬 */
    margin-bottom: 5px;
    /* 타이틀 아래 간격 */
    font-size: 23px;
}
.match-info-title {
    margin-bottom: 0px;
    font-size: 26px;
}
.modal-second-title {
    margin-bottom: 0px;
    /* 타이틀 아래 간격 */
}
.modal-second-title{
    margin: 5px;
    font-size: 20px;
}
#second-modal-match-info-title {
    margin: 0px;
    font-size: 20px;
    margin-bottom: -5px;
}
#second-modal-match-info{
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 0px;
}
.modal-body {
    display: flex;
    /* 이미지와 표를 옆으로 나란히 배치 */
    align-items: flex-start;
    /* 이미지와 표의 상단을 맞추기 */
}

.second-modal-left {
    display: flex;
    flex-direction: column;
    margin-bottom: 20px;
    justify-content: center;
    align-items: center;
}

/* 티켓 이미지 */
.modal-ticket-img img {
    max-width: 600px; /* 이미지 너비 줄임 */
    clip-path: inset(0 17% 0 0);
    /* 위쪽, 오른쪽, 아래쪽, 왼쪽 여백 순서 */
}

/* 이미 잘린 이미지 부분 안으로 표가 들어가게 함 */
.modal-table-container {
    margin-left: -10%;
    justify-content: center;
    align-items: center;
}

.modal-table {
    margin-left: 10%;
    border-collapse: collapse;
    width: 400px; /* 테이블 너비 줄임 */
    table-layout: fixed;
    /* 테이블 레이아웃을 고정(fixed)으로 설정 */
}

.vertical-divider {
    position: absolute;
    right: 37%;
    width: 1px;
    height: 730px;
    background-color: black;
    /* 세로 줄 색상 */
    margin: 0 20px;

}

.table-theader {
    background-color: #f0f0f0;
}

.modal-table-hide {
    margin-left: 5%;
    border-collapse: collapse;
    font-size: 16px;
    width: 90%;
    table-layout: fixed;
    /* 테이블 레이아웃을 고정(fixed)으로 설정 */
    background-color: rgb(255, 255, 255);
}

.modal-table-hide td {
    vertical-align: middle; /* 세로 가운데 정렬 */
    text-align: center; /* 가로 가운데 정렬 */
    padding: 8px; /* 셀의 패딩 조정 (필요시) */
}

.select-zone-btn {
    display: inline-block;
    margin: 0px;
    padding: 5px 10px;
    text-align: center;
    width: 100px; /* 버튼 사이즈 통일 */
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
}

.modal-info-box {
    display: flex;
    /* 아이콘과 텍스트를 나란히 배치 */
    align-items: center;
    /* 아이템들을 수직으로 가운데 정렬 */
    background-color: #f7f7f7;
    /* 배경색 설정 */
    border: 1px solid #ccc;
    /* 경계선 설정 */
    border-radius: 10px;
    /* 모서리를 둥글게 설정 */
    padding: 20px;
    /* 내부 여백 */
    margin-top: 40px;
    margin-bottom: 30px;
    margin-left: 56px;
    width: 500px;
}

.icon {
    margin-right: 10px;
    /* 아이콘과 텍스트 사이의 간격 */
}

.icon img {
    /* 아이콘 크기 */
    width: 80px;
    height: 80px;
}

.info-content {
    text-align: left;
    /* 글자 왼쪽 정렬 */
    color: #000000;
    /* 글자 색상 */
    font-size: 16px;
    /* 일반 문단 글자 크기 */
    list-style-type: disc;
    /* 목록 스타일 */
    padding-left: 10px;
    /* 목록 왼쪽 여백 */
}

.p {
    font-size: 13px;
    /* 일반 문단 글자 크기 */
    color: #1a1a1a;
    /* 부드러운 색상 */
    margin-top: 10px;
    /* 위쪽 여백 */
}

.right-table {
    max-width: 300px;
}

.left-table {
    margin: 0 auto;
    margin-top: 20px;
    width: 100%;
    display: flex;
    justify-content: center;
}

.second-modal-left-table {
    width: 500px;
    font-size: 16px;
}

.second-modal-table {
    max-width: 300px;
    width: 300px;
    margin-left: auto;
    margin-right: auto;
    align-content: center;
    background: white;
    border-collapse: collapse;
    table-layout: fixed;
    font-size: 17px;
}

.second-modal-ticket-price {
    font-size: 20px;
}
.ticket-info-content {
    text-align: left;
    line-height: 16px;
    font-size: 17px;
}

/* 모달페이지 안의 버튼 정렬 */

.quantity-selector {
    display: flex;
    justify-content: center;
    align-items: center;
    align-content: center;
    vertical-align: middle;
}

.quantity-selector button {
    display: flex; /* 플렉스 레이아웃 사용 */
    justify-content: center; /* 가로 중앙 정렬 */
    align-items: center; /* 세로 중앙 정렬 */
    margin-top: 0px;
    margin-left: 0px;
    width: 30px;
    height: 30px;
    font-size: 16px;
    text-align: center;
    border: 1px solid #ccc;
    background-color: #f0f0f0;
    cursor: pointer;
    color: black;
}

.quantity-selector button:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.quantity-box {
    max-width: 40px;
    text-align: center;
    border: 1px solid #ddd;
    margin: 0 5px;
    font-size: 16px;
    height: 30px;
}

.result-container {
    display: flex;
    align-items: center;
    gap: 20px; /* 텍스트와 버튼 사이의 간격 */
    margin-top: 40px;
}

.result-text {
    margin-left: 15%;
    width: 500px;
    font-size: 20px;
}

.result-text-all {
    color: red;
    font-size: 25px;
}

.match-info {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 0px;
}

.first-button {
    display: flex;
    gap: 10px; /* 버튼 간격 */
}

/* 다음 버튼 */
.buy-button {
    background-color: red;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    height: 50px;
    width: 120px;
    margin: 10px;
}

/* 닫기 버튼 */
.close-button {
    background-color: black;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    height: 50px;
    width: 120px;
    margin: 10px;
}

.buy-button:hover {
    background-color: #d32f2f;
}

.close-button:hover {
    background-color: #583939;
}

/* 충전 관련 */
.charge {
  border: 1px solid #bebebe;
  margin-top: 10px;
  padding: 20px;
  width: 100%; /* 전체 페이지의 50%로 설정 */
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: Arial, sans-serif;
  border-radius: 15px;
  font-size: 18px;
}

.charge-money {
  display: flex;
  justify-content: space-between;
  width: 100%;
  align-items: center;
  margin-bottom: 25px;
  align-items: flex-start; /* 텍스트와 버튼이 상단에 맞춰지도록 설정 */
}

.charge-money p {
  margin: 0;
  font-weight: bold;
}
.left-money {
  border: 1px solid #BCE067;
  padding-left: 15px;
  padding-right: 15px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin: 5px 0;
  border-radius: 10px;
}

.left-money{
background-color: #E4F7BA;
border: none;
}
.negative {
  color: red; /* 잔액이 0보다 작을 때 빨간색으로 표시 */
  font-weight: 900;
}
#btn-charge {
    border: none;
    columns: white;
    font-weight: bold;
    width: 80px;
    height: 35px;
    font-size: 15px;
    background-color: #FFBB00;
    margin-bottom: 0px;
    padding: 4px 8px;
    margin: 10px;
    margin-top: -15px;
    border-radius: 10px;
    margin-left: auto;
}

.notice{
    padding-left: 20px;
    padding-bottom: 10px;
}
</style>