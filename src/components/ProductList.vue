<!-- src/components/ProductList.vue -->
<template>
    <div class="product-list">
      <div class="brand-sections">
        <div v-for="brand in brands" :key="brand" class="brand-section">
          <div class="brand-header">
            <img :src="getBrandLogo(brand)" alt="Brand Logo" class="brand-logo">
          </div>
          <div class="products">
            <div v-for="product in filteredProductsByBrand(brand)" :key="product.food_name" class="product-item">
              <img :src="product.food_image_url" alt="Product Image" class="product-image">
              <div class="product-info">
                <h3>{{ product.food_name }}</h3>
                <p>{{ formatPrice(product.food_price) }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import GmarketLogo from '../assets/GmarketLogo-removebg.png';
  import EmartSSGLogo from '../assets/emartSSGLogo-removebg.png';
  import CoupangLogo from '../assets/coupangLogo-removebg.png';
  
  export default {
    props: {
      products: {
        type: Array,
        required: true
      }
    },
    computed: {
      brands() {
        return [...new Set(this.products.map(product => product.food_marketbrand))];
      }
    },
    methods: {
      filteredProductsByBrand(brand) {
        return this.products.filter(product => product.food_marketbrand === brand);
      },
      formatPrice(price) {
        return new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW' }).format(price);
      },
      getBrandLogo(brand) {
        switch (brand) {
          case 'G마켓':
            return GmarketLogo;
          case '이마트SSG':
            return EmartSSGLogo;
          case '쿠팡':
            return CoupangLogo;
          default:
            return '';
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .product-list {
    margin-top: 20px;
  }
  
  .brand-sections {
    display: flex;
    justify-content: space-between;
    flex-wrap: wrap;
  }
  
  .brand-section {
    flex: 1;
    margin: 0 10px;
    min-width: 250px;
  }
  
  .brand-header {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 10px;
  }
  
  .brand-logo {
    max-width: 100px;
    height: auto;
  }
  
  .products {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .product-item {
    display: flex;
    align-items: center;
    border: 1px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
    text-align: left;
    padding: 10px;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    cursor: pointer;
  }
  
  .product-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .product-image {
    width: 100px;
    height: 100px;
    object-fit: cover;
    border-radius: 4px;
    margin-right: 10px;
  }
  
  .product-info {
    flex-grow: 1;
  }
  
  .product-info h3 {
    font-size: 1.2em;
    margin: 5px 0;
  }
  
  .product-info p {
    color: #007bff;
    font-size: 1em;
    margin: 0;
  }
  </style>
  