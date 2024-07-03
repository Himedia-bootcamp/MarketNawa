<!-- src/components/CategorySelect.vue -->
<template>
    <div class="category-select">
      <div class="dropdown-container">
        <label for="secondCategory">분류:</label>
        <select v-model="selectedSecondCategory" @change="onSecondCategoryChange">
          <option value="">선택</option>
          <option v-for="category in secondCategories" :key="category" :value="category">{{ category }}</option>
        </select>
      </div>
  
      <div class="dropdown-container" v-if="selectedSecondCategory">
        <label for="lastCategory">상세:</label>
        <select v-model="selectedLastCategory" @change="onLastCategoryChange">
          <option value="">선택</option>
          <option v-for="category in uniqueLastCategories" :key="category" :value="category">{{ category }}</option>
        </select>
      </div>
  
      <div class="radio-container" v-if="selectedLastCategory">
        <div v-for="description in descriptions" :key="description" class="radio-item">
          <input type="radio" :id="description" :value="description" v-model="selectedDescription">
          <label :for="description">{{ description }}</label>
        </div>
      </div>
    </div>
  </template> 
  
  <script>
  import { categories } from '../data/categories.js';
  
  export default {
    data() {
      return {
        categories: categories,
        selectedSecondCategory: '',
        selectedLastCategory: '',
        selectedDescription: ''
      };
    },
    computed: {
      secondCategories() {
        const secondCategoriesSet = new Set(this.categories.map(c => c.second_category));
        return [...secondCategoriesSet];
      },
      uniqueLastCategories() {
        const lastCategoriesSet = new Set();
        this.categories
          .filter(c => c.second_category === this.selectedSecondCategory)
          .forEach(c => lastCategoriesSet.add(c.last_category));
        return [...lastCategoriesSet];
      },
      descriptions() {
        return [...new Set(
          this.categories
            .filter(c => c.second_category === this.selectedSecondCategory && c.last_category === this.selectedLastCategory)
            .map(c => c.description)
        )];
      }
    },
    methods: {
      onSecondCategoryChange() {
        this.selectedLastCategory = '';
        this.selectedDescription = '';
        this.$emit('category-selected', {
          secondCategory: this.selectedSecondCategory,
          lastCategory: this.selectedLastCategory,
          description: this.selectedDescription
        });
      },
      onLastCategoryChange() {
        this.selectedDescription = '';
        this.$emit('category-selected', {
          secondCategory: this.selectedSecondCategory,
          lastCategory: this.selectedLastCategory,
          description: this.selectedDescription
        });
      }
    },
    watch: {
      selectedDescription() {
        this.$emit('category-selected', {
          secondCategory: this.selectedSecondCategory,
          lastCategory: this.selectedLastCategory,
          description: this.selectedDescription
        });
      }
    }
  };
  </script>
  
  <style scoped>
  .category-select {
    margin: 20px 0;
  }
  
  .dropdown-container, .radio-container {
    margin-bottom: 15px;
  }
  
  .radio-container {
    display: flex;
    flex-wrap: wrap;
  }
  
  .radio-item {
    margin-right: 20px;
    margin-bottom: 10px;
    width: 150px; /* 고정 너비 설정 */
    text-align: center; /* 텍스트 가운데 정렬 */
    white-space: nowrap; /* 텍스트 줄바꿈 방지 */
    overflow: hidden; /* 넘치는 텍스트 숨김 */
    text-overflow: ellipsis; /* 텍스트 넘칠 경우 '...' 처리 */
  }
  
  label {
    font-weight: bold;
    display: block;
    margin-bottom: 5px;
  }
  
  select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
  }
  
  input[type="radio"] {
    margin-right: 10px;
  }
  </style>
  