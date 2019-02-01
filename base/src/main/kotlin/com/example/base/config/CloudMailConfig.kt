package com.example.base.config

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Cloud mail config
 * @author tree
 */
@Configuration
class CloudMailConfig {

  @Bean
  fun setAmazonSimpleEmailService(amazonSimpleEmailService: AmazonSimpleEmailService): AmazonSimpleEmailService = amazonSimpleEmailService
}
