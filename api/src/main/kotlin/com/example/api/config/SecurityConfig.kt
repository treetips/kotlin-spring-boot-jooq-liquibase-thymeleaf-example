package com.example.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

  @Bean
  fun springSecurityFilterChain(
    http: ServerHttpSecurity
  ): SecurityWebFilterChain {
    val httpSecurity = http.authorizeExchange()
      //--------------------------------------------------------
      // not require authentication
      //--------------------------------------------------------
      .pathMatchers(
        "/actuator/**",
        "/api/**"
      ).permitAll()
      .and().csrf().disable()
    return httpSecurity.build()
  }
}
