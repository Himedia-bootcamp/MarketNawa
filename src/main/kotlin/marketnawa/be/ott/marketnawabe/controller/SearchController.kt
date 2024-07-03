package marketnawa.be.ott.marketnawabe.controller

import marketnawa.be.ott.marketnawabe.common.Criteria
import marketnawa.be.ott.marketnawabe.common.PagingResponseDTO
import marketnawa.be.ott.marketnawabe.common.ResponseDTO
import marketnawa.be.ott.marketnawabe.document.SortingOrder
import marketnawa.be.ott.marketnawabe.service.SearchService
import marketnawa.be.ott.marketnawabe.util.IndexingUtil
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
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
        @RequestParam lastCategory: String,
        @RequestParam order: String,
        @RequestParam from: Int = 0,
        @RequestParam size: Int = 10,
    ) : ResponseEntity<ResponseDTO>{
        val sortingOrder = SortingOrder.fromString(order)
        val brandResults: Map<String, PagingResponseDTO> = searchService.searchByFoodName(keyword, lastCategory, sortingOrder, from, size)
        return ResponseEntity.ok().body(ResponseDTO(HttpStatus.OK, "조회", brandResults))
    }
}