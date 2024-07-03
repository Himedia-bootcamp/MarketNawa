<template>
    <div>
      <h1>Search Results</h1>
      <ul>
        <li v-for="item in searchResults" :key="item._id">
          {{ item._source.foodName }} - {{ item._source.foodPrice }}
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        searchResults: []
      };
    },
    mounted() {
      this.searchFoodMarket();
    },
    methods: {
      async searchFoodMarket() {
        try {
          const response = await axios.get('http://ec2-3-35-54-47.ap-northeast-2.compute.amazonaws.com:9200/market_food/_search', {
            params: {
              q: JSON.stringify({
                query: {
                  match: {
                    foodMarketBrand: 'Gmarket'
                  }
                }
              })
            }
          });
  
          this.searchResults = response.data.hits.hits;
        } catch (error) {
          console.error('Error fetching search results:', error);
        }
      }
    }
  };
  </script>

  <style>

</style>