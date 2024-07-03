package marketnawa.be.ott.marketnawabe.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.springframework.stereotype.Component

@Component
class IndexingUtil(
    private val restHighLevelClient: RestHighLevelClient
) {

    fun indexInsert(index: String, indexId: String, domainData: Any): IndexResponse? {
        val indexRequest = IndexRequest(index)
            .id(indexId)
            .source(ObjectMapper().writeValueAsString(domainData), XContentType.JSON)

        val indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT)
//        println("response id: ${indexResponse.id}")
//        println("response name: ${indexResponse.result.name}")
        return indexResponse
    }

    fun convertIndexRequest(index: String, indexId: String, domainData: Any): IndexRequest {
        return IndexRequest(index)
            .id(indexId)
            .source(ObjectMapper().writeValueAsString(domainData), XContentType.JSON)
    }
}