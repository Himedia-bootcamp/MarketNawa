package marketnawa.util

import com.fasterxml.jackson.databind.ObjectMapper
import marketnawa.elasitcsearch.ElasticsearchProperties
import org.apache.http.HttpEntity
import org.apache.http.HttpHost
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter
import org.springframework.stereotype.Component

@Component
class ElasticsearchUtil @Autowired constructor(
    private val elasticsearchProperties: ElasticsearchProperties
) {
    private val objectMapper = ObjectMapper()
    private val httpClient = HttpClients.createDefault()

    fun indexDocument(indexName: String, document: Any, documentId: String): Boolean {
        val jsonDocument = convertObjectToJson(document)
        val entity = StringEntity(jsonDocument, ContentType.APPLICATION_JSON)

        val request = HttpPut("$indexName/_doc/$documentId")
        request.entity = entity

        return executeHttpRequest(request)
    }

    fun bulkIndexDocuments(indexName: String, documents: List<Pair<Any, String>>): Boolean {
        val bulkRequest = StringBuilder()

        documents.forEach { (document, documentId) ->
            val jsonDocument = convertObjectToJson(document)
            bulkRequest.append("{\"index\":{\"_index\":\"$indexName\",\"_id\":\"$documentId\"}}\n")
            bulkRequest.append("$jsonDocument\n")
        }

        val entity = StringEntity(bulkRequest.toString(), ContentType.APPLICATION_JSON)

        val request = HttpPost("_bulk")
        request.entity = entity

        return executeHttpRequest(request)
    }

    fun findAll(indexName: String): List<Map<String, Any>> {
        val request = HttpGet("$indexName/_search")
        val response = httpClient.execute(HttpHost(elasticsearchProperties.host, elasticsearchProperties.port.toInt()), request)

        return try {
            val entity: HttpEntity? = response.entity
            val jsonResponse = entity?.let { EntityUtils.toString(it) }
            jsonResponse?.let { parseSearchResponse(it) } ?: emptyList()
        } finally {
            response.close()
        }
    }

    private fun parseSearchResponse(jsonResponse: String): List<Map<String, Any>> {
        val responseMap: Map<*, *>? = objectMapper.readValue(jsonResponse, Map::class.java)

        val hitsList = responseMap?.get("hits") as Map<String, Any>?
            ?: error("No 'hits' found in the response")

        val hitsArray = hitsList["hits"] as List<Map<String, Any>>?
            ?: error("No 'hits' array found in the response")

        return hitsArray
    }

    private fun convertObjectToJson(obj: Any): String {
        return objectMapper.writeValueAsString(obj)
    }

    private fun executeHttpRequest(request: HttpUriRequest): Boolean {
        val response = httpClient.execute(HttpHost(elasticsearchProperties.host, elasticsearchProperties.port.toInt()), request)
        val responseEntity: HttpEntity? = response.entity

        return responseEntity?.let {
            EntityUtils.consume(it)
            response.statusLine.statusCode in 200..299
        } ?: false
    }
}