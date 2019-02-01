package com.example.base.setting

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.nio.charset.Charset

/**
 * Mail setting for local
 */
@Component
@ConfigurationProperties(prefix = "dev-mail")
class DevMailSetting {

  lateinit var protocol: String
  lateinit var host: String
  lateinit var port: Number
  lateinit var username: String
  lateinit var password: String
  lateinit var defaultEncoding: Charset
}
