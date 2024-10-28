<template>
    <!-- 모달 창 -->
    <div id="modal" class="modal" v-if="visible">

        <!-- 모달 내용 -->
        <div class="first-modal-content">

            <!-- 모달 제목 -->
            <!-- 선택한 데이터 가져와서 일정 넣어야함 -->
            <div class="modal-title" v-if="firstPage">
                <h2>GS MMS VS 한국전력</h2>
                <div>
                    서울 하이체육관 | 2024.10.24(목) 19:00
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
                                    <th>매수선택</th>
                                    <th>잔여석</th>
                                </tr>
                            </thead>

                            <!-- DB 조회하여 데이터 넣는 구간 -->
                            <tbody>
                                <tr>
                                    <td>
                                        <input type="checkbox" class="select-zone-btn" v-model="isChecked"
                                            @change="onCheckboxChange">
                                        골드 [GOLD ZONE]
                                    </td>
                                    <td>
                                    </td>
                                    <td>90석</td>
                                </tr>
                            </tbody>
                        </table>

                        <!-- 숨겨진 테이블 -->
                        <table v-if="isChecked" class="modal-table-hide">
                            <tbody>

                                <tr v-for="(zone, index) in zones" :key="zone.name">
                                    <td>{{ zone.name }}</td>
                                    <td>
                                        <div class="quantity-selector">
                                            <button @click="decreaseQuantity(index)"
                                                :disabled="zone.quantity <= 0 || (activeButtonIndex !== null && activeButtonIndex !== index)">
                                                -
                                            </button>
                                            <input type="text" v-model="zone.quantity" readonly class="quantity-box" />
                                            <button @click="increaseQuantity(index)"
                                                :disabled="zone.quantity >= zone.maxSeats || (activeButtonIndex !== null && activeButtonIndex !== index)">
                                                +
                                            </button>
                                        </div>
                                    </td>
                                    <td>{{ zone.fullSeats }}</td>
                                </tr>

                                <!-- <tr v-for="(zone, index) in zones" :key="zone.name">
                                    <td>{{ zone.name }}</td>
                                    <td>
                                        <div class="quantity-selector">
                                            <button @click="decreaseQuantity(index)"
                                                :disabled="zone.quantity <= 0">-</button>
                                            <input type="text" v-model="zone.quantity" readonly class="quantity-box" />
                                            <button @click="increaseQuantity(index)"
                                                :disabled="zone.quantity >= zone.maxSeats">+</button>
                                        </div>
                                    </td>
                                    <td>{{ zone.fullSeats }}</td>
                                </tr> -->
                            </tbody>
                        </table>
                    </div>
                </div>

                <hr class="divider" />

                <div class="result-text">
                    구역을 선택하면 좌석은 자동 배정됩니다.
                    <div v-for="zone in selectedZones" :key="zone.name">
                        선택한 좌석 : {{ zone.name }}구역 {{ zone.quantity }}매
                    </div>
                    <div class="result-text-all">
                        총 : {{ countTicket.count }}매
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

                    <!-- 왼쪽 표 -->
                    <div class="left-table">
                        <div class="modal-table-container">
                            <table class="modal-table">
                                <thead class="table-theader">
                                    <tr>
                                        <th>구역</th>
                                        <th>매수</th>
                                        <th>금액</th>
                                        <th>멤버쉽할인</th>
                                    </tr>
                                </thead>

                                <!-- DB 조회하여 데이터 넣는 구간 -->
                                <tbody>
                                    <tr v-for="zone in selectedZones" :key="zone.name">
                                        <td> {{ zone.name }}구역 </td>
                                        <td> {{ zone.quantity }}매</td>
                                        <td> {{ zone.price }}원</td>
                                        <td>골드</td>
                                    </tr>
                                </tbody>
                            </table>

                            <table class="modal-table">
                                <thead class="table-theader">
                                    <tr>
                                        <td colspan="4">총액</td>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td colspan="4">{{ formattedTotal }}원</td>
                                    </tr>
                                </tbody>
                            </table>

                            <div class="info-box">
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

                    <div class="vertical-divider"></div>

                    <!-- 오른쪽 표 -->
                    <div class="right-table">

                        <!-- 모달 제목 -->
                        <!-- 선택한 데이터 가져와서 일정 넣어야함 -->
                        <div class="modal-second-title">
                            <h2>GS MMS VS 한국전력</h2>
                            <div>
                                서울 하이체육관 | 2024.10.24(목) 19:00
                            </div>
                        </div>
                        <div>
                            예매정보
                        </div>
                        <div v-for="zone in selectedZones" :key="zone.name">
                            {{ zone.name }}구역 / {{ zone.quantity }}매
                        </div>
                        <hr class="divider" />

                        <div>
                            <table class="second-modal-table">
                                <tr>
                                    <td>티켓 금액</td>
                                    <td>
                                        {{ formattedTotal }}원
                                    </td>
                                </tr>
                                <tr>
                                    <td>멤버십 할인</td>
                                    <td>
                                        골드
                                    </td>
                                </tr>
                                <tr>
                                    <td>총 결제 금액</td>
                                    <td>
                                        {{ formattedTotal }}원
                                    </td>
                                </tr>
                            </table>
                            <br>
                            <table class="second-modal-table">
                                <tr>
                                    <td>충전 금액</td>
                                    <td>충전 금액</td>
                                </tr>
                                <tr>
                                    <td> - 총 결제 금액</td>
                                    <td>총 결제 금액</td>
                                </tr>
                                <tr>
                                    <td>충전 잔액</td>
                                    <td>충전 잔액</td>
                                </tr>
                            </table>

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
                            <button class="buy-button" @click="openThirdPage">예매 하기</button>
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

export default {
    name: 'TicketModal',

    components: {

    },

    props: {
        visible: {
            type: Boolean,
            default: false,
        },
    },

    computed: {
        selectedZones() {
            // 수량이 1 이상인 구역만 필터링
            return this.zones.filter(zone => zone.quantity > 0);
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
        }
    },

    data() {
        return {

            // 구역 정보
            zones: [
                { name: 'GA', quantity: 0, maxSeats: 4, fullSeats: '30석', imageUrl: 'stadium-GA.jpg', price: 30000 },
                { name: 'GB', quantity: 0, maxSeats: 4, fullSeats: '30석', imageUrl: 'stadium-GB.jpg', price: 30000 },
                { name: 'GC', quantity: 0, maxSeats: 4, fullSeats: '30석', imageUrl: 'stadium-GC.jpg', price: 30000 }
            ],

            activeButtonIndex: null, // 현재 활성화된 버튼의 인덱스를 저장

            isChecked: false, // 체크박스의 초기 상태

            // 선택좌석 출력
            countTicket: {
                count: 0,
            },

            // 선택된 구역의 이미지 URL
            selectedZoneImage:
                require('@/assets/img/stadium/stadium-main.jpg'),

            // 두번째 모달창으로 이동
            firstPage: true,
            secondPage: false,

            total: 0,

        };
    },

    methods: {
        // 총합
        calculateTotal() {
            for (let i = 0; i < this.selectedZones.length; i++) {
                this.total = this.selectedZones[i].price * this.selectedZones[i].quantity + this.total;
            }
        },

        // 첫 모달창으로 이동
        openFirstPage() {
            this.firstPage = true;
            this.secondPage = false;
            this.total = 0;
        },

        // 두번째 모달창으로 이동
        openSecondPage() {
            this.calculateTotal();
            if (this.total > 0) {
                this.firstPage = false;
                this.secondPage = true;
            } else {
                alert("좌석을 선택해주세요.");
            }
        },


        onCheckboxChange() {
            if (this.isChecked) {
                this.showDetails();
            } else {
                this.hideDetails();
            }
        },
        showDetails() {
        },
        hideDetails() {
            this.countTicket.count = 0;
            this.zones.forEach(zone => {
                zone.quantity = 0; // 모든 구역의 수량을 0으로 설정                
            });
            this.selectedZoneImage =
                require('@/assets/img/stadium/stadium-main.jpg'); // 이미지도 초기화
        },

        // 모달 열기 닫기
        closeModal() {
            this.hideDetails();
            this.isChecked = false;
            this.$emit('close');
        },
        openModal() {
            this.isModalOpen = true;
        },
        // 숫자 버튼
        increaseQuantity(index) {
            if (this.zones[index].quantity < this.zones[index].maxSeats) {
                this.zones[index].quantity++;
                this.activeButtonIndex = index; // 현재 클릭된 버튼 인덱스 저장
                this.countTicket.count++;
                this.selectedZoneImage = require(`@/assets/img/stadium/${this.zones[index].imageUrl}`);

            }
        },
        decreaseQuantity(index) {
            if (this.zones[index].quantity > 0) {
                this.zones[index].quantity--;
                this.activeButtonIndex = index; // 현재 클릭된 버튼 인덱스 저장
                this.countTicket.count--;
                this.selectedZoneImage = require(`@/assets/img/stadium/${this.zones[index].imageUrl}`);

            }
        },

        selectZone(index) {
            // 선택한 구역의 이미지 URL을 설정
            this.selectedZoneImage = this.zones[index].imageUrl;
        },

    }
}
</script>


<style>
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
    max-height: 1000px;
    position: relative;
}

.second-modal-content {
    background: white;
    padding: 20px;
    border-radius: 10px;
    max-width: 1300px;
    max-height: 1000px;
    position: relative;

}

.modal-title {
    text-align: center;
    /* 타이틀을 가운데 정렬 */
    margin-bottom: 20px;
    /* 타이틀 아래 간격 */
}

.modal-second-title {
    margin-bottom: 20px;
    /* 타이틀 아래 간격 */
}

.modal-body {
    display: flex;
    /* 이미지와 표를 옆으로 나란히 배치 */
    align-items: flex-start;
    /* 이미지와 표의 상단을 맞추기 */
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
    height: 750px;
    background-color: black;
    /* 세로 줄 색상 */
    margin: 0 20px;

}

.table-theader {
    background-color: #f0f0f0;
}

.modal-table-hide {
    margin-left: 10%;
    border-collapse: collapse;
    font-size: 18px;
    width: 700px;
    table-layout: fixed;
    /* 테이블 레이아웃을 고정(fixed)으로 설정 */
    background-color: rgb(255, 255, 255);
}

.select-zone-btn {
    cursor: pointer;
}

.info-box {
    position: relative;
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
    width: 45%;
    left: -16%;
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
    margin-left: -45%;
    width: 400px;
}

.left-table {
    margin-top: 100px;
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
</style>