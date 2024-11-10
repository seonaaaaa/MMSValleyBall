<template>
  <div class="payment-container">
    <h2 class="payment-title">나의 금액 충전하기</h2>
    <div class="howMany">
      <label for="amountSelect" class="amountSelect">금액 선택:</label>
      <select id="amountSelect" v-model.number="amount">
        <option value="0" disabled>충전하실 금액을 선택해주세요.</option>
        <option value="10000">10,000원</option>
        <option value="20000">20,000원</option>
        <option value="30000">30,000원</option>
        <option value="40000">40,000원</option>
        <option value="50000">50,000원</option>
        <option value="100000">100,000원</option>
        <option value="200000">200,000원</option>
        <option value="300000">300,000원</option>
      </select>
      <h3 class="balance">
        현재 잔액: {{ moneyFormat(balance) }}원 + {{ moneyFormat(amount) }}원 = 충전 후 잔액 {{moneyFormat(balance+amount)}}원
      </h3>
    </div>
    <div class="button-group">
      <button class="pay-button confirm" @click="recharge()">충전하기</button>
      <button class="pay-button cancel" @click="cancel">취소</button>
    </div>
  </div>
</template>

<script>

export default {
  name: 'RechargeView',
  data() {
    return {
      amount: 0,
      balance: 0,
    }
  },
  mounted() {
    this.balance = Number(sessionStorage.getItem('balance') || 0);
  },
  methods: {
    recharge(){
      if(this.amount==0){
        alert('충전하실 금액을 선택해주세요.');
        return;
      }
      console.log("충전 함수 실행")
      this.$axios.post('/myPage/info/recharge', {
        amount: this.amount
      }).then(() => {
        alert(`${this.amount}원 충전되셨습니다.`);
        if (window.opener && !window.opener.closed) {
          window.opener.postMessage({ type: 'updateBalance', balance: this.amount+this.balance }, '*');
        }
        window.close();
      }).catch((error) => {
        console.error('충전 실패:', error);
      });
    },
    cancel() {
      window.close();
      alert('취소하셨습니다.');
    },
    moneyFormat(money){
      return new Intl.NumberFormat('ko-KR').format(money)
    }
  }
};
</script>

<style scoped>
.payment-title{
  margin-top: 0;
  margin-bottom: 10px;
}
.payment-container {
  width: 100%;
  max-width: 600px;
  height: 100%;
  margin: 0;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  overflow: hidden; /* 스크롤 방지 */
}
.balance{
  margin-bottom: 10px;
}

.howMany {
  margin-bottom: 20px;
}
#amountSelect {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.button-group {
  display: flex;
  justify-content: space-between;
}
.pay-button {
  width: 48%;
  padding: 10px;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.pay-button.confirm {
  background-color: #adcf69;
}
.pay-button.confirm:hover {
  background-color: #92c06c;
}
.pay-button.cancel {
  background-color: #d9534f;
}
.pay-button.cancel:hover {
  background-color: #c9302c;
}
</style>
