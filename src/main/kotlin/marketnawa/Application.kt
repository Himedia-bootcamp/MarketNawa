package marketnawa

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery
import marketnawa.crawling.product.gmarket.GmarketCrawler
import marketnawa.crawling.category.danawa.getFoodCategoryDetails
import marketnawa.crawling.product.coupang.CoupCrawler
import marketnawa.crawling.product.ssg.SsgCrawler
import marketnawa.domain.Category
import marketnawa.util.IndexingUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.RestClient
import org.apache.http.HttpHost
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.client.elc.NativeQuery
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.SearchHitsIterator
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import java.util.ArrayList

@SpringBootApplication
class Application {

    @Value("\${es.url}")
    private lateinit var elasticsearchUrl: String

    @Value("\${es.port}")
    private var elasticsearchPort: Int = 9200

    @Bean(name = ["restHighLevelClientCustom"])
    fun restHighLevelClient(): RestHighLevelClient {
        val httpHost = HttpHost(elasticsearchUrl, elasticsearchPort, "http")
        return RestHighLevelClient(RestClient.builder(httpHost))
    }

    @Bean
    fun indexingUtil(restHighLevelClient: RestHighLevelClient): IndexingUtil {
        return IndexingUtil(restHighLevelClient)
    }
}

fun findAllCategory(
    elasticsearchOperations: ElasticsearchOperations
): SearchHitsIterator<Category> {
    val index = IndexCoordinates.of("category")
    val searchQuery: NativeQuery = NativeQuery.builder()
        .withQuery { q: co.elastic.clients.elasticsearch._types.query_dsl.Query.Builder ->
            q
                .matchAll { ma: MatchAllQuery.Builder? -> ma }
        }
        .withFields("message")
        .withPageable(PageRequest.of(0, 10))
        .build()

    val stream: SearchHitsIterator<Category> = elasticsearchOperations.searchForStream(
        searchQuery,
        Category::class.java,
        index
    )
    return stream
}

fun main(args: Array<String>) {
    val context = SpringApplicationBuilder(Application::class.java)
        .properties("spring.jmx.enabled=false", "spring.application.admin.enabled=false")
        .run(*args)

//    val restHighLevelClient = context.getBean("restHighLevelClientCustom", RestHighLevelClient::class.java)
//    val indexingUtil = context.getBean(IndexingUtil::class.java)


//    // GmarketCrawling 실행
    val elasticsearchOperations = context.getBean("elasticsearchOperations", ElasticsearchOperations::class.java)
    val stream : SearchHitsIterator<Category> = findAllCategory(elasticsearchOperations)
    while (stream.hasNext()) {
        val category = stream.next().content
        val gmarketCrawler = GmarketCrawler(category, elasticsearchOperations)
        val ssgCrawler = SsgCrawler(category, elasticsearchOperations)
        val coupCrawler = CoupCrawler(category, elasticsearchOperations)
        ssgCrawler.execute()

        gmarketCrawler.execute()
        coupCrawler.execute()
    }
    stream.close()

//
//    // Category 크롤링 실행
//    val categories = getFoodCategoryDetails(restHighLevelClient, indexingUtil)
//    // categories.forEach { println(it) }
}
