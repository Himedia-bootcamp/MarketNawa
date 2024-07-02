package marketnawa.elasitcsearch

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticSearchConfig {

    @Value("\${es.url}")
    private val hostname: String? = null // localhost

    @Value("\${es.port}")
    private val port: Int? = null // 9200

    @Bean
    fun restHighLevelClient(): RestHighLevelClient {
        return RestHighLevelClient(
            RestClient.builder(
                HttpHost(
                    hostname,
                    port!!, "http"
                )
            )
        )
    }
}