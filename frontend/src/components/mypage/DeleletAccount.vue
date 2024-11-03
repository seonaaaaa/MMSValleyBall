<template>
  <!-- 회원 탈퇴 페이지 내용 -->
  <div class="delete-account-content">
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <p>정말로 탈퇴하시겠습니까?</p>
        <div class="modal-actions">
          <button @click="cancelDelete" class="cancel-button">취소</button>
          <button @click="confirmDelete" class="confirm-button">탈퇴하기</button>
        </div>
      </div>
    </div>
  </div>
</template>
  
  <script>
  
  export default {
    name: 'DeleteAccount',
      components: {
    },    
    data() {
      return {
      };
    },
    watch: {
      
    },
    methods: {
     
    }
  }
  </script>
  
  <style>
  /* 회원 탈퇴 */
  .delete-account-content {
    text-align: center;
    margin-top: 20px;
  }

  .delete-button {
    background-color: #e74c3c;
    color: white;
    border: none;
    padding: 10px 20px;
    cursor: pointer;
  }

  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 300px;
    text-align: center;
  }

  .modal-actions {
    margin-top: 20px;
  }

  .cancel-button,
  .confirm-button {
    padding: 10px;
    margin: 0 10px;
    cursor: pointer;
  }

  .cancel-button {
    background-color: #ccc;
  }

  .confirm-button {
    background-color: #e74c3c;
    color: white;
  }
</style>