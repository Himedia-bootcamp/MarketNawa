package marketnawa.be.ott.marketnawabe.controller

import marketnawa.be.ott.marketnawabe.common.ResponseDTO
import marketnawa.be.ott.marketnawabe.dto.MarketFoodDTO
import marketnawa.be.ott.marketnawabe.service.SearchService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController (
    private val searchService: SearchService,
){

    @GetMapping("/search")
    fun searchByFoodName(
        @RequestParam keyword: String,
        @RequestParam order: String = "",
        @RequestParam secondCategory: String = "",
        @RequestParam lastCategory: String = "",
        @RequestParam representativeName: String = "",
        @RequestParam from: Int = 0,
        @RequestParam size: Int = 10,
    ) : ResponseEntity<Any> {
        val indexName = "market_food"
        val brandResults: Map<String, MutableList<MarketFoodDTO>> =
            searchService.searchByFoodName(
                indexName,
                keyword,
                order,
                secondCategory,
                lastCategory,
                representativeName,
                from,
                size
            )
        return ResponseEntity.ok().body(ResponseDTO(HttpStatus.OK, "조회성공", brandResults))
    }
}