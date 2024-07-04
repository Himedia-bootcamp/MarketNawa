package com.akatsuki.pioms.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = mutableListOf(
            "http://localhost:5173", "http://localhost:8080", "http://localhost:5000", "http://localhost:9090",
            "http://market-nawa.store", "http://api.market-nawa.store"
        )
        config.allowedHeaders = mutableListOf("Origin", "Content-Type", "Accept", "Authorization")
        config.allowedMethods = mutableListOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        config.exposedHeaders = mutableListOf("Authorization", "Set-Cookie", "Cookie")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}
