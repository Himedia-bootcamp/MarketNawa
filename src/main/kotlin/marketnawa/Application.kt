package marketnawa

import marketnawa.crawling.product.gmarket.GmarketCrawler
import marketnawa.crawling.category.danawa.getFoodCategoryDetails
import marketnawa.util.IndexingUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.RestClient
import org.apache.http.HttpHost

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

fun main(args: Array<String>) {
    val context = SpringApplicationBuilder(Application::class.java)
        .properties("spring.jmx.enabled=false", "spring.application.admin.enabled=false")
        .run(*args)

    val restHighLevelClient = context.getBean("restHighLevelClientCustom", RestHighLevelClient::class.java)
    val indexingUtil = context.getBean(IndexingUtil::class.java)

    // GmarketCrawling 실행
    val gmarketCrawler = GmarketCrawler()
    gmarketCrawler.execute()

    // Category 크롤링 실행
    val categories = getFoodCategoryDetails(restHighLevelClient, indexingUtil)
    // categories.forEach { println(it) }
}
