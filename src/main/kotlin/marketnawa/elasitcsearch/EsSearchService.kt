package marketnawa.elasitcsearch

import marketnawa.util.ElasticsearchUtil
import marketnawa.util.ElasticsearchUtil2
import org.apache.coyote.Request
import org.elasticsearch.index.reindex.ScrollableHitSource.Hit
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*

@Service
class EsSearchService @Autowired constructor(
    private val elasticsearchUtil: ElasticsearchUtil2
) {

    fun indexDocument(indexName: String, docEntity: Any): Boolean {
        val documentId = UUID.randomUUID().toString()
        return elasticsearchUtil.indexDocument(indexName, docEntity, documentId)
    }

    fun bulkIndexDocuments(indexName: String, documents: List<Pair<Any, String>>): Boolean {
        return elasticsearchUtil.bulkIndexDocuments(indexName, documents)
    }

    fun findAll(indexName: String, from: Int = 0, size: Int = 10): List<Map<String, Any>> {
        return elasticsearchUtil.findAll(indexName, from, size)
    }

    fun getTotalHitsCount(indexName: String): Int {
        return elasticsearchUtil.getTotalHitsCount(indexName)
    }
}