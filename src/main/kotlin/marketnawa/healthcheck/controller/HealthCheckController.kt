package marketnawa.healthcheck.controller

import marketnawa.domain.Category
import marketnawa.util.IndexingUtil
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.ArrayList

@RestController
class HealthCheckController(
    private val restHighLevelClient: RestHighLevelClient,
    private val indexingUtil: IndexingUtil
) {
    @GetMapping("/healthcheck")
    fun healthCheck(): String {
        return "Health Check Test OK"
    }

    @GetMapping("/test")
    fun test(): HashMap<String, Any> {
        val searchRequest = SearchRequest("market_nawa")
        val searchSourceBuilder = SearchSourceBuilder()
        searchSourceBuilder.query(QueryBuilders.matchAllQuery())
        searchRequest.source(searchSourceBuilder)

        val response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT)

        var smapList = ArrayList<Map<String, Any>>();
        // 검색 결과 처리 예시
        for (hit in response.hits) {
            var smap = hit.sourceAsMap
            smapList.add(smap as Map<String, Any>);
        }

        var responseMap = HashMap<String, Any>()
        responseMap.put("status", 200);
        responseMap.put("data", smapList);

        return responseMap;
    }

    @GetMapping("/insert")
    fun test2(): IndexResponse? {
        val category = Category(
            "sdfsdfd",
            "larddge",
            "mddedi",
            "smaddll",
            "des",
            "searddddddddddch"
        )

        return indexingUtil.indexInsert(
            "market_nawa",
            category.categoryId!!,
            category
        )
    }
}
