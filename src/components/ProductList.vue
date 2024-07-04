<template>
  <div class="product-list">
    <div v-if="isCategoryDefault" class="no-category">
      <img :src="logo" alt="Marketnawa Logo" class="marketnawa-logo" @click="triggerEasterEgg">
      <div v-if="showEasterEgg" class="easter-egg">
        <p> 🐣 이스터에그 발견!! 🐣 <br> 팀 5tt!~ <br> 저희 MARKETNAWA에서 상품의 가격을 비교해보세요! </p>
      </div>
    </div>
    <div v-else>
      <div class="sort-dropdown">
        <label for="sortOrder">정렬: </label>
        <select id="sortOrder" v-model="selectedSortOrder" @change="onSortOrderChange">
          <option value="accuracy">정확도순</option>
          <option value="lowToHigh">낮은 가격순</option>
          <option value="highToLow">높은 가격순</option>
        </select>
      </div>
      <div class="brand-sections">
        <div v-for="brand in allBrands" :key="brand" class="brand-section">
          <div class="brand-header">
            <img :src="getBrandLogo(brand)" alt="Brand Logo" class="brand-logo">
          </div>
          <div class="products">
            <div v-if="filteredProductsByBrand(brand).length === 0" class="no-products">조회되는 상품이 없습니다.</div>
            <div v-else>
              <div v-for="product in sortedProductsByBrand(brand)" :key="`${product.foodId}-${product.foodName}`" class="product-item" @click="navigateToProduct(product.foodLink)">
                <img :src="product.foodImg" alt="Product Image" class="product-image">
                <div class="product-info">
                  <h3>{{ truncateMiddle(product.foodName, 25) }}</h3>
                  <p>{{ formatPrice(product.foodPrice) }}</p>
                </div>
              </div>
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
import marketnawaLogo from '../assets/test-removebg-preview.png';

export default {
  props: {
    products: {
      type: Array,
      required: true
    },
    sortOrder: {
      type: String,
      required: true
    },
    selectedCategory: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      selectedSortOrder: this.sortOrder,
      allBrands: ['Gmarket', 'SSG', 'coupang'],
      logo: marketnawaLogo,
      showEasterEgg: false
    };
  },
  computed: {
    isCategoryDefault() {
      return this.selectedCategory.description === '선택';
    },
    sortedProductsByBrand() {
      return (brand) => {
        let products = this.filteredProductsByBrand(brand);
        if (this.selectedSortOrder === 'lowToHigh') {
          products.sort((a, b) => a.foodPrice - b.foodPrice);
        } else if (this.selectedSortOrder === 'highToLow') {
          products.sort((a, b) => b.foodPrice - a.foodPrice);
        }
        return products;
      };
    }
  },
  methods: {
    filteredProductsByBrand(brand) {
      return this.products.filter(product => product.foodMarketBrand === brand);
    },
    formatPrice(price) {
      return new Intl.NumberFormat('ko-KR', { style: 'currency', currency: 'KRW' }).format(price);
    },
    getBrandLogo(brand) {
      switch (brand) {
        case 'Gmarket':
          return GmarketLogo;
        case 'SSG':
          return EmartSSGLogo;
        case 'coupang':
          return CoupangLogo;
        default:
          return '';
      }
    },
    onSortOrderChange() {
      this.$emit('update:sortOrder', this.selectedSortOrder);
    },
    navigateToProduct(url) {
      window.open(url, '_blank');
    },
    truncateMiddle(str, maxLength) {
      if (str.length <= maxLength) return str;
      const keep = maxLength / 2;
      return str.slice(0, keep) + '...' + str.slice(str.length - keep);
    },
    triggerEasterEgg() {
      this.showEasterEgg = true;
      setTimeout(() => {
        this.showEasterEgg = false;
      }, 3000); // 2초 후에 이스터에그 창을 숨깁니다.
    }
  },
  watch: {
    sortOrder(newVal) {
      this.selectedSortOrder = newVal;
    }
  }
};
</script>

<style scoped>
.product-list {
  margin-top: 20px;
}

.no-category {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 400px; /* Adjust height as needed */
  position: relative; /* 이스터에그 위치를 상대적으로 설정 */
}

.marketnawa-logo {
  margin-top: 150px;
  max-width: 100%;
  height: auto;
  animation: floating 2s ease-in-out infinite;
  /* cursor: pointer; */
}

.easter-egg {
  position: absolute;
  height: 500px;
  width: 500px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  border: 2px solid #000;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  animation: fade-in-out 2s ease-in-out forwards;
}

@keyframes floating {
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-15px);
  }
  100% {
    transform: translateY(0);
  }
}

@keyframes fade-in-out {
  0% {
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
}

.sort-dropdown {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.sort-dropdown label {
  margin-right: 10px;
  font-weight: bold;
}

.sort-dropdown select {
  padding: 5px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.brand-sections {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap.
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
  max-width: 70px;
  height: auto.
}

.products {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.product-item {
  margin-top : 3px;
  height: 80px;
  background-color: #ffffff;
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
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1).
}

.product-image {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 10px;
}

.product-info {
  flex-grow: 1.
}

.product-info h3 {
  font-size: 10pt;
  font-weight: bold;
  margin: 5px 0;
}

.product-info p {
  color: #007bff;
  font-size: 1em;
  margin: 0.
}

.no-products {
  text-align: center;
  padding: 20px;
  font-size: 1.2em;
  color: #999;
  border: 1px solid #ddd;
  border-radius: 8px.
}
</style>
