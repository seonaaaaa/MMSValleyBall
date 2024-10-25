<template>
    <!-- 모달 창 -->
    <div id="modal" class="modal" v-if="visible">
        <div>

            <!-- 모달 내용 -->
            <div class="modal-content">

                <!-- 모달 제목 -->
                <!-- 선택한 데이터 가져와서 일정 넣어야함 -->
                <div class="modal-title">
                    <h2>GS MMS VS 한국전력</h2>
                    <div>
                        서울 하이체육관 | 2024.10.24(목) 19:00
                    </div>
                </div>

                <!-- 모달 디테일 -->
                <div class="modal-body">


                    <!-- 모달 이미지 -->
                    <div class="modal-ticket-img" v-if="selectedZoneImage">
                        <img :src="selectedZoneImage" alt="선택한 구역 이미지" class="stadium-image" />
                    </div>

                    <!-- 모달 표 -->
                    <div class="modal-table-container">
                        <table class="modal-table">
                            <thead>
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

                                <!-- 숨겨진 테이블 -->
                                <table v-if="isChecked" class="hideTable">
                                    <tr v-for="(zone, index) in zones" :key="zone.name">
                                        <td>{{ zone.name }}</td>
                                        <td>
                                            <div class="quantity-selector">
                                                <button @click="decreaseQuantity(index)"
                                                    :disabled="zone.quantity <= 0">-</button>
                                                <input type="text" v-model="zone.quantity" readonly />
                                                <button @click="increaseQuantity(index)"
                                                    :disabled="zone.quantity >= zone.maxSeats">+</button>
                                            </div>
                                        </td>
                                        <td>{{ zone.fullSeats }}</td>
                                    </tr>
                                </table>
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
                <!-- 결제 버튼 -->
                <button class="buy-button" @click="buy">결제</button>
                <!-- 모달 종료버튼 -->
                <button class="close-button" @click="closeModal">닫기</button>
            </div>
        </div>
    </div>

</template>

<script>

export default {
    name: 'TicketModal',

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
        }
    },

    data() {
        return {
            // 구역 정보
            zones: [
                { name: 'GA', quantity: 0, maxSeats: 4, fullSeats: '30석', imageUrl: 'stadium-GA.jpg' },
                { name: 'GB', quantity: 0, maxSeats: 4, fullSeats: '30석', imageUrl: 'stadium-GB.jpg' },
                { name: 'GC', quantity: 0, maxSeats: 4, fullSeats: '30석', imageUrl: 'stadium-GC.jpg' }
            ],

            isChecked: false, // 체크박스의 초기 상태

            // 선택좌석 출력
            countTicket: {
                count: 0,
            },

            // 선택된 구역의 이미지 URL
            selectedZoneImage:
                require('@/assets/img/stadium/stadium-main.jpg'),
        };
    },

    methods: {
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
            this.$emit('close');
        },
        openModal() {
            this.isModalOpen = true;
        },
        // 숫자 버튼
        increaseQuantity(index) {
            if (this.zones[index].quantity < this.zones[index].maxSeats) {
                this.zones[index].quantity++;
                this.countTicket.count++;
                this.selectedZoneImage = require(`@/assets/img/stadium/${this.zones[index].imageUrl}`);
            }
        },
        decreaseQuantity(index) {
            if (this.zones[index].quantity > 0) {
                this.zones[index].quantity--;
                this.countTicket.count--;
                this.selectedZoneImage = require(`@/assets/img/stadium/${this.zones[index].imageUrl}`);
            }
        },

        selectZone(index) {
            // 선택한 구역의 이미지 URL을 설정
            this.selectedZoneImage = this.zones[index].imageUrl;
        }
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

.modal-content {
    background: white;
    padding: 20px;
    border-radius: 10px;
    max-width: 2000px;
    max-height: 1000px;
    position: relative;
}

.modal-title {
    text-align: center;
    /* 타이틀을 가운데 정렬 */
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

.modal-table-container {
    margin-left: -10%;
    /* 이미 잘린 이미지 부분 안으로 표가 들어가게 함 */
}

.modal-table {
    border-collapse: collapse;
    width: 100%;
}

.select-zone-btn {
    width: 15px;
    height: 15px;
    cursor: pointer;
}

.modal-table th,
.modal-table td {
    border: 1px solid #ddd;
    padding: 8px;
}

.quantity-selector {
    display: flex;
    align-items: center;
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

.quantity-selector input {
    width: 40px;
    text-align: center;
    border: 1px solid #ddd;
    margin: 0 5px;
    font-size: 16px;
}

.hideTable {
    border: 1px solid black;
    background-color: lightgray;
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

.buy-button {
    background-color: red;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    height: 50px;
    width: 100px;
    position: absolute;
    top: 90%;
    right: 15%;

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
    width: 100px;
    position: absolute;
    top: 90%;
    right: 5%;
}

.close-button:hover {
    background-color: #d32f2f;
}
</style>