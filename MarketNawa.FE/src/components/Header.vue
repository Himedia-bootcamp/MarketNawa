<!-- src/components/Header.vue -->
<template>
  <header class="header">
    <div class="logo" @click="goToHome">MARKETNAWA</div>
    <div class="icons">
      <i class="fas fa-shopping-cart" @click="showAlert"></i>
      <i class="fas fa-user" @click="showAlert"></i>
    </div>
  </header>
</template>

<script>
export default {
  methods: {
    goToHome() {
      window.location.href = '/';
    },
    showAlert() {
      alert('준비중입니다.');
    }
  }
};
</script>
  
  <style scoped>
  .header {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 30px;
    background-color: #000000;
    color: white;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    z-index: 1000;
    box-sizing: border-box;
  }
  
  .logo {
    font-size: 1.8em;
    font-weight: bold;
    cursor: pointer;
  }
  
  .icons {
    font-size: 1.5em;
  }
  
  .icons i {
    margin-left: 20px;
    cursor: pointer;
  }
  </style>
  