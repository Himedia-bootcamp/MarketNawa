<template>
  <div id="app">
    <Header />
    <div class="content">
      <CategorySelect @category-selected="onCategorySelected" />
      <SearchBar :searchQuery="selectedCategory.description" @search="performSearch" />
      <ProductList :products="filteredProducts" :sortOrder.sync="sortOrder" :selectedCategory="selectedCategory" />
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import Header from './components/Header.vue';
import SearchBar from './components/SearchBar.vue';
import ProductList from './components/ProductList.vue';
import CategorySelect from './components/CategorySelect.vue';
import './assets/style.css';

export default {
  components: {
    Header,
    SearchBar,
    ProductList,
    CategorySelect
  },
  data() {
    return {
      selectedCategory: { description: '선택' },
      sortOrder: 'accuracy', // 초기 정렬 순서
      allProducts: [],
      searchQuery: '' // 검색어를 저장하는 데이터
    };
  },
  computed: {
    filteredProducts() {
      // 카테고리 필터링 및 중복 제거 로직 추가
      const uniqueProducts = [];
      const productSet = new Set();
      this.allProducts.forEach(product => {
        const identifier = `${product.foodName}`;
        if (!productSet.has(identifier)) {
          productSet.add(identifier);
          uniqueProducts.push(product);
        }
      });
      console.log('Filtered Products:', uniqueProducts); // 필터링된 제품 로그
      return uniqueProducts;
    }
  },
  methods: {
    async fetchProducts() {
      try {
        const response = await axios.get('http://api.market-nawa.store/search', {
          params: {
            keyword: this.searchQuery,
            detailCategory: this.selectedCategory.description !== '선택' ? this.selectedCategory.description : '',
            order: this.sortOrder,
            size: 1000 // 최대 1000개의 결과를 가져오도록 설정
          }
        });
        if (response.data && response.data.data) {
          this.allProducts = Object.values(response.data.data).flat(); // Assuming the data structure is a dictionary with brand-wise lists
          console.log('All Products:', this.allProducts); // 가져온 모든 제품 로그
        } else {
          console.error('Invalid response format:', response.data);
        }
      } catch (error) {
        console.error('Error fetching products:', error);
        if (error.response) {
          console.error('Response error data:', error.response.data); // 서버 응답 에러 데이터
          console.error('Response status:', error.response.status); // 서버 응답 상태 코드
        } else if (error.request) {
          console.error('Request error:', error.request); // 요청이 이루어졌으나 응답이 없을 때
        } else {
          console.error('General error:', error.message); // 기타 오류 메시지
        }
      }
    },
    performSearch(query) {
      this.searchQuery = query;
      this.fetchProducts(); // Perform search with new query
    },
    onCategorySelected(category) {
      this.selectedCategory = category;
      this.performSearch(category.description); // Perform search with new category
    },
    setSortOrder(order) {
      this.sortOrder = order;
      this.fetchProducts(); // Perform search with new sort order
    },
    async loadProducts() {
      // Perform an initial load of products
      await this.fetchProducts();
    }
  },
  mounted() {
    this.loadProducts();
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
