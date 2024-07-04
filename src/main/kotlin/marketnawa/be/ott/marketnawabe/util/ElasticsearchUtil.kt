package marketnawa.be.ott.marketnawabe.util

import com.fasterxml.jackson.databind.ObjectMapper
import marketnawa.be.ott.marketnawabe.config.ElasticsearchProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

@Component
class ElasticsearchUtil @Autowired constructor(
    private val elasticsearchProperties: ElasticsearchProperties
) {
    private val objectMapper = ObjectMapper()

    fun indexDocument(indexName: String, document: Any, documentId: String): Boolean {
        val jsonDocument = convertObjectToJson(document)
        val uri = URI.create("${elasticsearchProperties.host}:${elasticsearchProperties.port}/$indexName/_doc/$documentId")
        val responseCode = executeHttpRequest("PUT", uri, jsonDocument)
        val responseMap: Map<*, *>? = objectMapper.readValue(responseCode, Map::class.java)
        val result = responseMap?.get("result") as String? ?: ""
        return result == "created" // 예시로 "created"인 경우를 성공으로 가정
    }

    fun bulkIndexDocuments(indexName: String, documents: List<Pair<Any, String>>): Boolean {
        val bulkRequest = StringBuilder()

        documents.forEach { (document, documentId) ->
            val jsonDocument = convertObjectToJson(document)
            bulkRequest.append("{\"index\":{\"_index\":\"$indexName\",\"_id\":\"$documentId\"}}\n")
            bulkRequest.append("$jsonDocument\n")
        }

        val uri = URI.create("${elasticsearchProperties.host}/_bulk")
        val responseCode = executeHttpRequest("POST", uri, bulkRequest.toString())
        val responseMap: Map<*, *>? = objectMapper.readValue(responseCode, Map::class.java)
        val result = responseMap?.get("result") as String? ?: ""
        return result == "created" // 예시로 "created"인 경우를 성공으로 가정
    }

    fun findAll(indexName: String, from: Int = 0, size: Int = 10): List<Map<String, Any>> {
        val uri = URI.create("${elasticsearchProperties.host}:${elasticsearchProperties.port}/$indexName/_search")
            val query = """
            {
                "from": $from,
                "size": $size,
                "query": {
                    "match_all": {}
                }
            }
        """.trimIndent()

        val jsonResponse = executeHttpRequest("GET", uri, query)
        return parseSearchResponse(jsonResponse)
    }

    fun getTotalHitsCount(indexName: String): Int {
        val uri = URI.create("${elasticsearchProperties.host}:${elasticsearchProperties.port}/$indexName/_count")
        val query = """
            {
                "query": {
                    "match_all": {}
                }
            }
        """.trimIndent()

        val jsonResponse = executeHttpRequest("GET", uri, query)
        val responseMap: Map<*, *>? = objectMapper.readValue(jsonResponse, Map::class.java)
        return responseMap?.get("count") as Int
    }

    fun search(
        indexName: String,
        keyword: String?,
        market: String?,
        order: String?,
        secondCategory: String?,
        lastCategory: String?,
        representativeName: String?,
        from: Int = 0,
        size: Int = 10
    ): List<Map<String, Any>> {
        val uri = URI.create("${elasticsearchProperties.host}:${elasticsearchProperties.port}/$indexName/_search")

        // 쿼리 JSON 생성
        val query = buildQuery(keyword, market, order, secondCategory, lastCategory, representativeName, from, size)
        // HTTP 요청 실행
        val jsonResponse = executeHttpRequest("POST", uri, query)

        // JSON 파싱
        return parseSearchResponse(jsonResponse)
    }

    private fun executeHttpRequest(method: String, uri: URI, requestBody: String? = null): String {
        val url = URL(uri.toString())
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.setRequestProperty("Content-Type", "application/json")
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        if (requestBody != null) {
            connection.doOutput = true
            val outputStream = connection.outputStream
            outputStream.write(requestBody.toByteArray())
            outputStream.flush()
            outputStream.close()
        }

        val responseCode = connection.responseCode
        val jsonResponse = if (responseCode in 200..299) {
            val inputStream = connection.inputStream
            val response = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
            response
        } else {
            val errorStream = connection.errorStream
            val errorResponse = errorStream?.bufferedReader()?.use { it.readText() } ?: ""
            errorStream?.close()
            errorResponse
        }

        connection.disconnect()
        return jsonResponse
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

    private fun buildQuery(
        keyword: String?,
        market: String?,
        order: String?,
        secondCategory: String?,
        lastCategory: String?,
        representativeName: String?,
        from: Int,
        size: Int
    ): String {

        val sortField = when (order) {
            "highest" -> "foodPrice"
            "lowest" -> "foodPrice"
            else -> null
        }

        val sortOrder = when (order) {
            "highest" -> "desc"
            "lowest" -> "asc"
            else -> null
        }


        val sortQuery = if (sortField != null && sortOrder != null) {
            """
        ,"sort": [
            {
                "$sortField": {
                    "order": "$sortOrder"
                }
            }
        ]
        """
        } else {
            ""
        }

        val boolMustQueries = mutableListOf<String>()

        if (!keyword.isNullOrBlank()) {
            boolMustQueries.add("""
            {
                "match": {
                    "foodName": "$keyword"
                }
            }
        """)
        }

        if (!market.isNullOrBlank()) {
            boolMustQueries.add("""
            {
                "match": {
                    "foodMarketBrand": "$market"
                }
            }
        """)
        }

        if (!secondCategory.isNullOrBlank()) {
            boolMustQueries.add("""
            {
                "match": {
                    "secondCategory": "$secondCategory"
                }
            }
        """)
        }

        if (!lastCategory.isNullOrBlank()) {
            boolMustQueries.add("""
            {
                "match": {
                    "lastCategory": "$lastCategory"
                }
            }
        """)
        }

        if (!representativeName.isNullOrBlank()) {
            boolMustQueries.add("""
            {
                "match": {
                    "representativeName": "$representativeName"
                }
            }
        """)
        }

        val boolQuery = """
        "bool": {
            "must": [${boolMustQueries.joinToString(",")}]
        }
    """

        return """
        {
            "from": $from,
            "size": $size,
            "query": {
                $boolQuery
            }
            $sortQuery
        }
    """.trimIndent()
    }
}
