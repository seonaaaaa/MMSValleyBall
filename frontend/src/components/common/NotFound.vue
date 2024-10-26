<template>
    <div class="not-found">
      <h1>404</h1>
      <p>죄송합니다. 요청하신 페이지를 찾을 수 없습니다.</p>
      <router-link to="/">홈으로 돌아가기</router-link>
    </div>
  </template>
  
  <script>
  export default {
    name: 'NotFound',
  };
  </script>
  
  <style scoped>
  .not-found {
    text-align: center;
    margin-top: 100px;
  }
  .not-found h1 {
    font-size: 6rem;
    margin-bottom: 20px;
  }
  .not-found p {
    font-size: 1.5rem;
    margin-bottom: 20px;
  }
  </style>