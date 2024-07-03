<!-- src/components/SearchBar.vue -->
<template>
    <div class="search-bar">
      <input v-model="query" @keyup.enter="search" placeholder="Search for products..."  style="width: 80%;"/>
      <button @click="search">
      <i class="bi bi-search"></i>
    </button>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        query: ''
      };
    },
    methods: {
      search() {
        this.$emit('search', this.query);
      }
    }
  };
  </script>
  
  <style scoped>
  .search-bar {
    display: flex;
    justify-content: space-evenly;
    margin: 20px 0;
  }
  
  .search-bar input[type="text"] {
    width: 70%;
    padding: 15px;
    margin-right: 10px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 1.2em;
    transition: all 0.3s ease;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .search-bar input[type="text"]:focus {
    border-color: #000000;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
  
  .search-bar button {
    padding: 15px 30px;
    border: none;
    background-color: #202020;
    color: white;
    border-radius: 8px;
    cursor: pointer;
    font-size: 1.2em;
    transition: all 0.3s ease;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .search-bar button:hover {
    background-color: #858585;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
  
  .search-bar button:active {
    transform: translateY(2px);
  }
  </style>
  