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
                    <div class="modal-ticket-img">
                        <img src="@/assets/img/stadium/stadium-main.jpg" alt="stadium-img" class="stadium-image" />
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
                                        <input type="button" class="select-zone-btn" @click="selectZone">
                                        골드 [GOLD ZONE]
                                    </td>
                                    <td>
                                        <div class="quantity-selector">
                                            <button @click="decreaseQuantity('zone')"
                                                :disabled="quantities.zone <= 1">-</button>
                                            <input type="text" v-model="quantities.zone" readonly />
                                            <button @click="increaseQuantity('zone')"
                                                :disabled="quantities.zone >= maxSeats.zone">+</button>
                                        </div>
                                    </td>
                                    <td>90석</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

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

    components: {
    },

    data() {
        return {
            quantities: {
                zone: 0, // 구역의 초기 매수 선택
            },
            maxSeats: {
                zone: 4, // 구역의 최대 구매 가능 잔여석 수
            },
        };
    },

    methods: {
        closeModal() {
            this.$emit('close');
        },
        openModal() {
            this.isModalOpen = true; // 모달 열기
        },
        // 숫자 버튼
        increaseQuantity(zone) {
            if (this.quantities[zone] < this.maxSeats[zone]) {
                this.quantities[zone]++;
            }
        },
        decreaseQuantity(zone) {
            if (this.quantities[zone] > 1) {
                this.quantities[zone]--;
            }
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
    width: 30px;
    height: 30px;
    font-size: 16px;
    text-align: center;
    border: 1px solid #ccc;
    background-color: yellow;
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

/* 닫기 버튼 */
.close-button {
    background-color: #f44336;
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