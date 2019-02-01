package com.example.base.setting

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * JOOQ setting
 */
@Component
@ConfigurationProperties(prefix = "jooq")
class JooqSetting {

  /**
   * Slow query threshold
   */
  lateinit var slowQueryLogThreshold: Number
}
