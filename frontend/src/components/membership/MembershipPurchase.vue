<template>
    <div class="membership-purchase-page">
      <!-- 상단 1차 메뉴 텍스트가 들어간 배경 박스 -->
      <LogoHeader />
  
      <!-- 멤버십 안내, 멤버십 구매 메뉴 -->
      <div class="menu">
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/membership/info' }" @click="navigateTo('/membership/info')">멤버십 안내</div>
        <div class="menu-item" :class="{ 'active-menu-item': activeMenu === '/membership/purchase' }" @click="navigateTo('/membership/purchase')">멤버십 구매</div>
      </div>
    
      <!-- 멤버십 구매 페이지 내용 -->
      <div class="membership-purchase-content">
        <h4>2024/25시즌 멤버십 구매</h4>
        <div class="membership-info-layout">
          <table class="membership-info-table">
            <thead>
              <tr>
                <th>종류</th>
                <th>혜택</th>
                <th>판매 가격</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>GOLD</td>
                <td>
                    <p>골드전용 좌석 구매</p>
                    <p>경기 7일전 선예매</p>
                    <p>팀 유니폼 증정 (랜덤)</p>
                    <p>팀 굿즈(MMS 고무장갑, 슬로건) 증정</p>
                </td>
                <td>300,000원</td>
              </tr>
              <tr>
                <td>SILVER</td>
                <td>
                    <p>경기 7일전 선예매</p>
                    <p>팀 굿즈(MMS 고무장갑, 슬로건) 증정</p>
                </td>
                <td>100,000원</td>
              </tr>
            </tbody>
          </table>
        </div>
        
          <!-- 멤버십 선택 -->
          <div class="membership-selection">
            <p><strong>멤버십 선택</strong></p>
            <select class="select-form" v-model="selectedMembershipPrice" @change="updatePrices">
              <option value="">선택</option>
              <option value="100000">SILVER</option>
              <option value="300000">GOLD</option>
            </select>
          </div>

        <div class="charge">
          <table class="charge-table">
            <tr>
              <th>충전 금액</th>
              <th>멤버십 금액</th>
              <th>잔액</th>
            </tr>
            <tr>
              <td>{{ formattedPayment }} 원</td>
              <td>- {{ formattedMembershipPrice }} 원</td>
              <td>{{ formattedLeftMoney }} 원</td>
            </tr>
          </table>
          <button class="btn" id="btn-charge" v-if="leftMoney < 0" onclick="window.location.href='http://localhost:8080//myPage/info/recharge';">충전하기</button>
        </div>

        <div class="buttons">
          <button class="btn" id="btn-purchase" @click="purchaseMembership">구매하기</button>
          <button class="btn" id="btn-cancel" onclick="window.location.href='http://localhost:8080/membership/info';">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import LogoHeader from '../common/LogoHeader.vue';
  
  export default {
      name: 'MembershipPurchase',
      components: {
        LogoHeader
    },    
    data() {
      return {
        activeMenu: this.$route.path, // 현재 활성화된 경로
        selectedMembershipPrice: 0,   // 선택된 멤버십 가격
        payment: 0,                   // 현재 충전 금액
        membershipData: {},           // API에서 가져온 멤버십 데이터
      };
    },
    watch: {
      // 경로가 변경될 때마다 activeMenu를 업데이트
      $route(to) {
        this.activeMenu = to.path;
      }
    },
    computed: {
      formattedPayment() {
        return this.payment.toLocaleString();
      },
      formattedMembershipPrice() {
        return this.selectedMembershipPrice.toLocaleString();
      },
      leftMoney() {
        return this.payment - this.selectedMembershipPrice;
      },
      formattedLeftMoney() {
        return this.leftMoney.toLocaleString();
      },
    },
    methods: {
      navigateTo(route) {
        this.$router.push(route);
        this.activeMenu = route; // 메뉴를 클릭할 때 활성화된 메뉴 업데이트
      },
      // 초기 API 호출로 충전 금액 가져오기
      fetchUserMoney() {
        fetch("/membership/purchase")
          .then(response => response.json())
          .then(data => {
            this.payment = data.userMoney;
            this.membershipData = data.newMembership; // API에서 받은 newMembership 데이터 설정
            this.updatePrices();
          })
          .catch(error => console.error('Error fetching user money:', error));
      },
      // 멤버십 금액 및 남은 금액 업데이트
      updatePrices() {
        // leftMoney가 변하면, computed에서 자동으로 갱신됨
      },
      // 구매하기 버튼 클릭 시 호출되는 메서드
      purchaseMembership() {
        // 선택한 멤버십 아이디 설정
        let membershipId = 4;
            if (this.selectedMembershipPrice === 100000) {
              membershipId = 5;
            } else if (this.selectedMembershipPrice === 300000) {
              membershipId = 6;
            }
            // newMembership의 membershipSalesMembershipId에 선택한 membershipId 설정
            this.membershipData.membershipSalesMembershipId = membershipId;

            fetch("/membership/purchase/completed", {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify(this.membershipData),
            })
              .then(response => response.json())
              .then(data => console.log(data))
              .catch(error => console.error('Error purchasing membership:', error));
          },
        },
        mounted() {
          this.fetchUserMoney(); // 컴포넌트가 마운트될 때 충전 금액 가져오기
        },
  };
  </script>
  
  <style>
  .membership-purchase-page {
      padding-top: var(--header-height);
      padding-bottom: var(--footer-height);
      text-align: center;
  }
  
  /* 메뉴 */
  .menu {
    display: flex;
    justify-content: center;
    gap: 50px;
    padding-bottom: 20px;
  }
  
  .menu-item {
    width: 120px;
    padding: 10px 0;
    position: relative;
    cursor: pointer;
    font-weight: bold;
    color: #565656;
  }
  
  .menu-item::after {
    content: "";
    position: absolute;
    bottom: -5px; /* 밑줄이 텍스트 아래에 표시되도록 간격을 조정 */
    left: 0;
    width: 100%;  /* 밑줄을 전체 div 너비에 맞춤 */
    height: 3px;
    background-color: black;
    transform: scaleX(0); /* 기본적으로 밑줄을 숨김 */
    transition: transform 0.3s ease;
  }
  
  .active-menu-item {
    color: black;  /* 선택된 메뉴의 텍스트 색상을 black으로 설정 */
  }
  
  .active-menu-item::after {
    transform: scaleX(1);  /* 활성화된 메뉴에만 밑줄 표시 */
  }
  
  .menu-item:hover::after {
    transform: scaleX(1);  /* 마우스를 올리면 밑줄 표시 */
  }
  
  /* 멤버십 구매 */

.membership-purchase-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  max-width: 90%; /* 최대 너비를 설정 */
  margin: 0 auto;
}

.membership-info-layout {
  width: 80%; /* 전체 페이지의 50%로 설정 */
  margin-bottom: 30px; /* 아래 여백 추가 */
}

.membership-info-table {
  width: 100%; /* 테이블을 가로 전체에 맞추기 */
  margin: 0 auto; /* 가운데 정렬 */
  padding: auto;
  font-size: 21;
}

.membership-selection {
  display: flex;
  margin-top: 0px;
  margin-bottom: 30px;
  gap: 30px;
  text-align: center;
  width: 40%;
  margin-left: 20px;
}

.select-form {
  width: 60%;
  text-align: center
}

.charge {
  text-align: center;
  width: 60%; /* 전체 페이지의 50%로 설정 */
}

.charge-table {
  width: 80%; /* 테이블을 가로 전체에 맞추기 */
  margin: 0 auto; /* 가운데 정렬 */
  /* margin-bottom: 10px; */
}

.buttons{
  margin-top: 30px;
  margin-bottom: 50px;
  gap: 20px;
}
/* 버튼 css */
.btn {
  border: none;
  /* 테두리 없음 */
  color: white;
  /* 글자 색 (흰색) */
  padding: 8px 16px;
  /* 버튼 내부 여백 (위아래 12px, 좌우 24px) */
  text-align: center;
  /* 글자 가운데 정렬 */
  text-decoration: none;
  /* 밑줄 없음 */
  display: inline-block;
  /* 기본 인라인 블록 설정 */
  font-size: 22px;
  /* 글자 크기 */
  border-radius: 8px;
  /* 모서리를 둥글게 */
  cursor: pointer;
  /* 마우스를 올렸을 때 포인터 모양 */
  transition: background-color 0.3s;
  /* 배경색이 바뀌는 효과 추가 */
  margin-top: 50px;
  margin-left: 15px;
  margin-right: 15px;
  margin-bottom: 50px;

  width: 100px;
  /* 버튼의 너비 */
  height: 50px;
  /* 버튼의 높이 */
}

#btn-purchase {
  width: 140px;
  background-color: #4CAF50;  /* 버튼 배경색 (녹색) */
}

#btn-purchase:hover {
  background-color: #45a049; /* 마우스를 올렸을 때 배경색 변경 */
}
#btn-charge {
  width: 140px;
  background-color: #FFBB00;
  margin-bottom: 0px;
}

#btn-charge:hover {
  background-color: #EDA900; /* 마우스를 올렸을 때 배경색 변경 */
}

#btn-cancel {
  background-color: #A6A6A6;  /* 버튼 배경색 (녹색) */
}


  </style>