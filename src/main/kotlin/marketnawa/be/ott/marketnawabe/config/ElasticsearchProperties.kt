package marketnawa.be.ott.marketnawabe.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticsearchProperties {

    @Value("\${elasticsearch.host}")
    lateinit var host: String

    @Value("\${elasticsearch.port}")
    lateinit var port: String
}