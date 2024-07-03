<!-- src/App.vue -->
<template>
  <div id="app">
    <Header />
    <div class="content">
      <CategorySelect @category-selected="onCategorySelected" />
      <SearchBar @search="performSearch" />
      
      <ProductList :products="filteredProducts" :sortOrder="sortOrder" />
      <Pagination :totalItems="filteredProducts.length" :itemsPerPage="itemsPerPage" @page-changed="onPageChanged" />
    </div>
  </div>
</template>

<script>
import Header from './components/Header.vue';
import SearchBar from './components/SearchBar.vue';
import ProductList from './components/ProductList.vue';
import CategorySelect from './components/CategorySelect.vue';
import Pagination from './components/Pagination.vue';
import './assets/style.css';

export default {
  components: {
    Header,
    SearchBar,
    ProductList,
    CategorySelect,
    Pagination
  },
  data() {
    return {
      selectedCategory: {},
      currentPage: 1,
      itemsPerPage: 20,
      sortOrder: 'lowToHigh', // 초기 정렬 순서
      allProducts: [
        // 예시 데이터
        { food_name: 'G마켓 상품 1', food_price: 10000, food_marketbrand: 'G마켓', food_image_url: 'https://via.placeholder.com/100' },
        { food_name: 'G마켓 상품 2', food_price: 15000, food_marketbrand: 'G마켓', food_image_url: 'https://via.placeholder.com/100' },
        { food_name: '이마트SSG 상품 1', food_price: 20000, food_marketbrand: '이마트SSG', food_image_url: 'https://via.placeholder.com/100' },
        { food_name: '쿠팡 상품 1', food_price: 25000, food_marketbrand: '쿠팡', food_image_url: 'https://via.placeholder.com/100' },
        { food_name: '쿠팡 상품 2', food_price: 30000, food_marketbrand: '쿠팡', food_image_url: 'https://via.placeholder.com/100' },
        // 더 많은 데이터 추가
      ]
    };
  },
  computed: {
    filteredProducts() {
      // 카테고리 필터링 로직 추가
      return this.allProducts.filter(product => {
        const { secondCategory, lastCategory, description } = this.selectedCategory;
        return (
          (!secondCategory || product.second_category === secondCategory) &&
          (!lastCategory || product.last_category === lastCategory) &&
          (!description || product.food_description === description)
        );
      });
    }
  },
  methods: {
    performSearch(query) {
      // 검색 필터링 로직 추가
      this.filteredProducts = this.allProducts.filter(product =>
        product.food_name.toLowerCase().includes(query.toLowerCase())
      );
      this.currentPage = 1; // 검색 시 페이지를 초기화
    },
    onCategorySelected(category) {
      this.selectedCategory = category;
      this.currentPage = 1; // 카테고리 선택 시 페이지를 초기화
    },
    onPageChanged(page) {
      this.currentPage = page;
    },
    setSortOrder(order) {
      this.sortOrder = order;
    }
  }
};
</script>

<style>
/* 기존 스타일 유지 */
#app {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  box-sizing: border-box;
}

.content {
  padding-top: 70px; /* 헤더의 높이 만큼 패딩 추가 */
}

.sort-buttons {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.sort-buttons button {
  padding: 10px 20px;
  margin: 0 10px;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1em;
  transition: background-color 0.3s ease;
}

.sort-buttons button:hover {
  background-color: #0056b3;
}
</style>
