package marketnawa.be.ott.marketnawabe.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration

@Configuration
class ElasticsearchConfig : ElasticsearchConfiguration() {
    override fun clientConfiguration(): ClientConfiguration {
        return ClientConfiguration.builder()
            .connectedTo("http://ec2-3-35-54-47.ap-northeast-2.compute.amazonaws.com:9200")
            .build()
    }
}