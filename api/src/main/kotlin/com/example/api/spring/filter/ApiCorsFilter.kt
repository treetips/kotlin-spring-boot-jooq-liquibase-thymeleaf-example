package com.example.api.spring.filter

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

/**
 * Cross origin filter for api
 * @author tree
 */
@Component
class ApiCorsFilter {

  @Bean
  fun apiCorsWebFilter(): CorsWebFilter = CorsWebFilter(
    UrlBasedCorsConfigurationSource().apply {
      CorsConfiguration().apply {
        allowCredentials = true
        addAllowedOrigin(CorsConfiguration.ALL)
        addAllowedHeader(CorsConfiguration.ALL)
        addAllowedMethod(CorsConfiguration.ALL)
      }.let { conf ->
        // Register multiple permit urls
        setCorsConfigurations(
          mapOf(
            "/api/**" to conf,
            "/sample/**" to conf
          )
        )
      }
    }
  )
}
