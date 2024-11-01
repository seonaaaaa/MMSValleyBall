<template>
    <!-- 모달 창 -->
    <div id="modal" class="modal" v-if="visible">

        <!-- 모달 내용 -->
        <div class="first-modal-content">

            <!-- 모달 제목 -->
            <!-- 선택한 데이터 가져와서 일정 넣어야함 -->
            <div class="modal-title" v-if="firstPage">
                <p class="match-info-title"><strong>{{ formatMatchInfo(matchInfo.matchTeam, thisTeam) }}</strong></p>
                <div class="match-info">
                    <p>{{ match.matchStadium }}&nbsp;</p>
                    <p> | &nbsp;</p> 
                    <p v-html="formatDate(match.matchDate)"></p>
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
                            <!-- <th></th> -->
                            <th>잔여석</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="zone in filteredAvailableSeats" :key="zone.zoneName">
                            <td>
                                <button class="select-zone-btn" @click="toggleZoneSelection(zone)">
                                    {{ zone.zoneName }}
                                </button>
                            </td>
                            <!-- <td></td> -->
                            <td>{{ zone.availableSeatAmount }}석</td>
                        </tr>

                        <!-- 선택한 구역의 섹션 정보 표시 -->
                        <tr v-if="zoneSelection != null">
                            <td colspan="2">
                                <table class="modal-table-hide">
                                    <tbody>
                                        <tr v-for="section in zoneSelection.sections" :key="section.seatId">
                                            <td>{{ section.sectionName }}</td>
                                            <td>
                                                <div class="quantity-selector">
                                                    <button @click="decreaseQuantity(section)" 
                                                            :disabled="section.quantity <= 0">-</button>
                                                    <input type="text" v-model="section.quantity" readonly class="quantity-box" />
                                                    <button @click="increaseQuantity(section, section.availableSeatAmount)">
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

        <div class="result-text">
            구역을 선택하면 좌석은 자동 배정됩니다.
            <div>
                선택한 좌석 : {{ sectionSelection ? sectionSelection.sectionName : ' ' }}구역 {{ countTicket }}매
            </div>
            <div class="result-text-all">
                총 : {{ countTicket }}매
            </div>
        </div>
        <div class="first-button">
            <!-- 결제 버튼 -->
            <button class="buy-button" @click="openSecondPage">다음</button>

            <!-- 모달 종료버튼 -->
            <button class="close-button" @click="closeModal">닫기</button>
        </div>
    </div>
            <!-- ------------------------------------------------------------------ -->

            <!-- 2번째 모달  -->
            <div v-if="secondPage" class="second-modal-content">
                <div class="modal-body">
                    <div class="left-body">
                            <!-- 경기 정보 -->
                    <div class="second-modal-left">
                     <div class="modal-second-title">
                        <p id="second-modal-match-info-title"><strong>{{ formatMatchInfo(matchInfo.matchTeam, thisTeam) }}</strong></p>
                        <div id="second-modal-match-info">
                            <p>{{ match.matchStadium }}&nbsp;</p>
                            <p> | &nbsp;</p> 
                            <p v-html="formatDate(match.matchDate)"></p>
                        </div>
                    </div>
                    <!-- 왼쪽 표 -->
                    <div class="left-table">
                        <div class="modal-table-container">
                            <table class="modal-table">
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
                                        <td> {{ userMembership.membershipDiscount }} %</td>
                                    </tr>
                                    <tr class="table-theader">
                                        <th colspan="4">총액</th>
                                    </tr>
                                    <tr>
                                        <td colspan="4">{{ formattedTotal }}원</td>
                                    </tr>
                            </table>

                            <div class="modal-info-box">
                                <div class="icon">
                                    <img src="@/assets/img/anyImg/bell-icon.png" alt="alert" />
                                </div>
                                <div class="info-content">
                                    <h4>예매 마감안내</h4>
                                    <ul>
                                        <li> - 당일 경기 시작 전 1시간 까지 가능</li>
                                        <li> - 취소 시간 이후 구매한 티켓은 취소 및 좌석 변경이 되지 않습니다.</li>
                                    </ul>
                                    <h4>예매 취소안내</h4>
                                    <p class="p">당일 경기 시작 12시간 전까지 가능
                                    </p>
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
                        <p>경기정보: {{ formatMatchInfo(matchInfo.matchTeam, thisTeam) }}</p>
                        <p>경기장: {{ match.matchStadium }}</p>
                        <p v-html="`경기일시: ${formatDate(match.matchDate)}`"></p>
                        <p>구역: <strong>{{ seatSelection.sectionName }}</strong>구역 / <strong>{{ seatSelection.quantity }}</strong>매</p>
                    </div>
                    <hr class="divider" />

                    <div>
                        <table class="second-modal-table" id="second-modal-ticke-price">
                            <tr>
                                <td>티켓 금액</td>
                                <td>
                                    {{ formattedTotal }}원
                                </td>
                            </tr>
                            <tr>
                                <td>멤버십 할인</td>
                                <td>
                                    {{ formattedMembershipType }} | {{ userMembership.membershipDiscount }} %
                                </td>
                            </tr>
                            <tr>
                                <td>총 결제 금액</td>
                                <td>
                                    {{ formattedPayment.payment }}원
                                </td>
                            </tr>
                        </table>
                        <br>
                        <div class="charge">
                            <div class="charge-money">
                                    <p><strong>이용가능한 충전금액 : </strong></p>
                                    <p>{{ userBalance }} 원</p>
                            </div>
                            <button id="btn-charge" v-if="payment.leftMoney < 0" onclick="window.location.href='http://localhost:8080//myPage/info/recharge';">충전하기</button>
                            <div class="membership-price">
                                <p>결제금액</p>
                                <p>- {{ formattedPayment.payment }} 원</p>
                            </div>
                            <div class="left-money">
                                <p>잔액</p>
                                <!-- leftMoney 값이 0보다 작으면 'negative' 클래스를 추가 -->
                                <p :class="{ 'negative': payment.leftMoney < 0 }">{{ formattedPayment.leftMoney }} 원</p>
                            </div>
                        </div>

                    </div>
                    <div>
                        <p class="p">
                            취소기한 : 자동계산 [12시간 전]<br>
                            <!-- 링크 -->
                            취소수수료 : [상세보기]
                        </p>
                    </div>
                    <div class="second-button">
                        <!-- 결제 버튼 -->
                        <button v-if="payment.leftMoney >= 0" class="buy-button" @click="purchaseTicket">예매 하기</button>
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

    components: {

    },

    props: {
        visible: {
            type: Boolean,
            default: false,
        },
        user: {
            type: Object,
            default: () => ({ name: '', role: 'guest', email: '', isLoggedIn: false })
        },
        match: {
            type: Object,
            required: true,
        },
        
    },
    async mounted() {
        console.log("Mounted with user:", this.user); // 유저 정보 출력
        await this.fetchEvents(); // 데이터 fetch
    },

    computed: {
        selectedZones() {
            // 수량이 1 이상인 구역만 필터링
            return this.availableSeatsList
                .filter(zone => zone.availableSeatAmount > 0 && zone.isChecked)
                .map(zone => ({
                    ...zone,
                    sections: zone.sections || [],
                    quantity: zone.sections.reduce((total, section) => total + (section.quantity || 0), 0), // 섹션의 수량 합계
                    price: zone.price * zone.quantity // 가격 계산
                }));
        },
        filteredAvailableSeats() {
            return this.availableSeatsList.filter(zone => zone.availableSeatAmount > 0);
        },

        formattedTotal() {
            // 숫자를 세 자리마다 쉼표가 붙은 문자열로 변환
            return this.total.toLocaleString();
        },

        formattedPayment() {
        // 변환된 결제 금액과 잔액을 반환
            return {
                payment: this.payment.payment.toLocaleString(),
                leftMoney: this.payment.leftMoney.toLocaleString()
            };
        },
        formattedMembershipType() {
            const membershipType = this.userMembership.membershipType;
            // '/' 이후 세 글자와 그 이후 문자열을 추출
            const match = membershipType.match(/\/.{3}(.+)/);
            return match ? match[1].trim() : '';
        }

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
        }
    },

    data() {
        return {
            // 두번째 모달창으로 이동
            firstPage: true,
            secondPage: false,

            // 서버에서 받는 데이터
            ticketSalesDto: {},
            userBalance: 0,
            matchInfo: {},
            availableSeatsList: [],
            userMembership: {},
            seatDTOList: [],

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
            payment: {},
            thisTeam: "GS ITM",
        };
    },

    methods: {
        // API 호출
        async fetchEvents() {
            // let data = {};
            try {
                console.log("Sending request with:", {
                    email: this.user.email,
                    matchId: this.match.matchId
                });
                const response = await axios.get('/ticket/purchase/modal', {
                    params: {
                        email: this.user.email,
                        matchId: this.match.matchId
                    }
                });
                this.ticketSalesDto = response.data.ticketSalesDto;
                this.userBalance = response.data.userBalance;
                this.matchInfo = response.data.matchInfo;
                this.availableSeatsList = response.data.availableSeatsList;
                this.userMembership = response.data.userMembership;
                this.seatDTOList = response.data.seatDTOList;
                this.initializeSectionQuantities(); // 섹션 수량 초기화
                console.log(response.data);
                console.log("response:", response);
                // response.data = response.data;
            } catch (error) {
                console.error("Error fetching data: ", error);
            }
        },
        // 예매하기 버튼 클릭 시 호출되는 메서드
        async purchaseTicket(){
            this.ticketSalesDto = {
                userEmail: this.user.email,
                matchId: this.matchInfo.matchId,
                ticketDetailAmount: this.seatSelection.quantity,
                ticketDetailSeat: this.seatSelection.seatId,
                ticketPaidPrice: this.payment.payment,
                tickeId: null,
                ticketNumber: null,
                ticketCreateAt: new Date().toISOString(), // 현재 시간으로 설정
            };
            // axios 요청
            try {
                const response = await axios.post("/ticket/purchase/completed", this.ticketSalesDto);
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
        // 결제 금액
        calculatePayment() {
            this.payment.payment = this.total * (100 - this.userMembership.membershipDiscount) * 0.01;
            this.payment.leftMoney = this.userBalance - this.payment.payment;
        },

        //두 번째 모달에 전달할 좌석 정보
        createSeatSelection(){
            const matchingSeat = this.seatDTOList.find(seat => seat.seatId === this.sectionSelection.seatId);
            const seatPrice = matchingSeat ? matchingSeat.seatPrice : 0; // 매칭된 좌석이 없을 경우 기본값 0

            this.seatSelection = {
                seatId: this.sectionSelection.seatId,
                sectionName: this.sectionSelection.sectionName,
                quantity: this.countTicket,
                seatPrice: seatPrice
            };
        },

        // 첫 모달창으로 이동
        openFirstPage() {
            this.firstPage = true;
            this.secondPage = false;
            this.total = 0;
        },

        // 두번째 모달창으로 이동
        openSecondPage() {
            this.createSeatSelection();
            this.calculateTotal();
            this.calculatePayment();
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
            
            if (section.quantity < 4 && section.availableSeatAmount > section.quantity) {
                section.quantity++;
                this.countTicket++;
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
            if (this.zoneSelection && this.zoneSelection.zoneName === zone.zoneName) {
                // 선택 해제
                this.zoneSelection = null;
                this.sectionSelection = null;
                this.countTicket = 0;
            } else {
                // 새로운 구역 선택
                this.zoneSelection = zone;
            }
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
                this.showDetails(zone);
            } else {
                this.hideDetails();
            }
        },
        showDetails(zone) {
            this.zoneSelection = zone;
            
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
            this.availableSeatsList.forEach(zone => {
                zone.sections.forEach(section => {
                    section.quantity = 0; // 수량 초기화
                });
            });
        },

        // 모달 열기 닫기
        closeModal() {
            this.hideDetails();
            this.isChecked = false;
            this.isModalOpen = false;
            this.firstPage = true;
            this.secondPage = false;
            this.$emit('close'); // 모달 종료 이벤트 발생
        },

        openModal() {
            this.isModalOpen = true;
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
    background: rgba(0, 0, 0, 0.5);
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
    max-width: 2000px;
    max-height: 1500px;
    position: relative;
}

.second-modal-content {
    background: white;
    padding: 20px;
    border-radius: 10px;
    width: 1300px;
    max-height: 1000px;
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
    max-width: 800px;
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
    width: 700px;
    table-layout: fixed;
    /* 테이블 레이아웃을 고정(fixed)으로 설정 */
}

.vertical-divider {
    position: absolute;
    right: 38%;
    width: 1px;
    height: 800px;
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
    font-size: 18px;
    width: 90%;
    table-layout: fixed;
    /* 테이블 레이아웃을 고정(fixed)으로 설정 */
    background-color: rgb(255, 255, 255);
}

.select-zone-btn {
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
    margin-top: 30px;
    margin-bottom: 30px;
    margin-left: 80px;
    width: 83%;
}

.icon {
    margin-right: 20px;
    /* 아이콘과 텍스트 사이의 간격 */
}

.icon img {
    width: 120px;
    /* 아이콘 크기 */
    height: 120px;
    /* 아이콘 크기 */
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
    padding-left: 20px;
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
    margin-left: 5%;
    width: 400px;
}

.left-table {
    margin-top: 20px;
}

.second-modal-table {
    width: 400px;
    margin-left: auto;
    margin-right: auto;
    align-content: center;
    background: white;
    border-collapse: collapse;
    table-layout: fixed;
}

.second-modal-ticket-price {
    font-size: 20px;
}
.ticket-info-content {
    text-align: left;
    line-height: 16px;
}

.quantity-selector button {
    width: 30px;
    height: 30px;
    font-size: 16px;
    text-align: center;
    border: 1px solid #ccc;
    background-color: #f0f0f0;
    cursor: pointer;
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

.result-text {
    margin-left: 30%;
    width: 500px;
    font-size: 20px;
}

.result-text-all {
    color: red;
    width: 500px;
    font-size: 30px;
}

.match-info {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 0px;
}

.first-button {
    position: absolute;
    right: 1%;
    bottom: 1%;
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
  padding: 20px;
  width: 100%; /* 전체 페이지의 50%로 설정 */
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: Arial, sans-serif;
  border-radius: 15px;
  font-size: 20px;
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
</style>