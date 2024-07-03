package marketnawa.be.ott.marketnawabe.controller

import marketnawa.be.ott.marketnawabe.common.Criteria
import marketnawa.be.ott.marketnawabe.common.PagingResponseDTO
import marketnawa.be.ott.marketnawabe.common.ResponseDTO
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
        @RequestParam detail : String,
        @RequestParam sort : String = "",
        @RequestParam offset: String = "1",
    ) : ResponseEntity<ResponseDTO>{
        val cri = Criteria(offset.toInt(), 80)
        val brandResults: Map<String, PagingResponseDTO> = searchService.searchByFoodName(keyword, cri, sort, detail)

        return ResponseEntity.ok().body(ResponseDTO(HttpStatus.OK, "조회", brandResults))
    }
}