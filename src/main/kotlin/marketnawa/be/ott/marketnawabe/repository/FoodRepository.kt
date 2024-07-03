package marketnawa.be.ott.marketnawabe.repository

import marketnawa.be.ott.marketnawabe.document.MarketFood
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface FoodRepository : ElasticsearchRepository<MarketFood, String> {
    @Query("{\"bool\":{\"must\":[{\"match\":{\"food_name\":{\"query\":\"?0\",\"analyzer\":\"nori_analyzer\"}}},{\"term\":{\"last_category\":\"?1\"}},{\"term\":{\"food_marketbrand\":\"?2\"}}]}}")
    fun findByFoodNameContaining(foodName: String, detail: String, marketbrand: String, paging: Pageable): Page<MarketFood>
}