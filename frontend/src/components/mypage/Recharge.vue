<template>
  <div class="payment-container">
    <h2 class="payment-title">나의 금액 충전하기</h2>
    <div class="howMany">
      <label for="amountSelect" class="amountSelect">금액 선택:</label>
      <select id="amountSelect" v-model.number="amount">
        <option value="0" disabled>충전하실 금액을 선택해주세요.</option>
        <option value="10000">10000원</option>
        <option value="20000">20000원</option>
        <option value="30000">30000원</option>
        <option value="40000">40000원</option>
        <option value="50000">50000원</option>
        <option value="100000">100000원</option>
        <option value="200000">200000원</option>
        <option value="300000">300000원</option>
      </select>
      <h3 class="balance">
        현재 잔액: {{ balance }}원 + {{ amount }}원 = 충전 후 {{balance+amount}}원
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
    };
  },
  mounted() {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);

    // 쿼리 파라미터로부터 데이터 추출
    this.balance = Number(urlParams.get('balance'));
  },
  methods: {
    recharge(){
      this.$emit('userbalance', this.balance+this.amount);
      if(this.amount==0){
        alert('충전하실 금액을 선택해주세요.');
        return;
      }
      console.log("충전 함수 실행")
      this.$axios.post('/myPage/info/recharge',{
          email: sessionStorage.getItem('email'),
          amount: this.amount
        },{
        headers: {
          Authorization : sessionStorage.getItem('token')
        }
      }).then((response) => {
        console.log(response.data);
        alert(`${this.amount}원 충전되셨습니다.`);
        window.close();
      }).catch((error) => {
        console.error('충전 실패:', error);
      });
    },
    membershipImage(userMembership){
      return  require(`@/assets/img/membershipImg/${userMembership}.png`);
    },
    cancel() {
      window.close();
      alert('취소하셨습니다.');
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
  height: 100vh;
  margin: 0;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
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
  background-color: #28a745;
}
.pay-button.confirm:hover {
  background-color: #218838;
}
.pay-button.cancel {
  background-color: #dc3545;
}
.pay-button.cancel:hover {
  background-color: #c82333;
}
</style>
