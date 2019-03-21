package com.example.base.config

import nz.net.ultraq.thymeleaf.LayoutDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver

/**
 * Thymeleaf config
 * @author tree
 */
@Configuration
class ThymeleafConfig(
  private val springResourceTemplateResolver: SpringResourceTemplateResolver
) {

  @Bean
  fun layoutDialect(): LayoutDialect = LayoutDialect()

  @Bean
  fun springTemplateEngine(): SpringTemplateEngine = SpringTemplateEngine().apply {
    addTemplateResolver(springResourceTemplateResolver)
    addDialect(layoutDialect())
    addDialect(Java8TimeDialect())
  }
}
