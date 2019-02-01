package com.example.base.setting

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Redis setting
 */
@Component
@ConfigurationProperties(prefix = "redis")
class RedisSetting {

  /**
   * Default ttl
   */
  lateinit var defaultTtl: Number

  /**
   * Command timeout
   */
  lateinit var commandTimeout: Number
}
