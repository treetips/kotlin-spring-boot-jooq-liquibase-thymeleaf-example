package com.example.api.setting

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Sample mail setting
 */
@Component
@ConfigurationProperties(prefix = "settings.sample.mail")
class SampleMailSetting {

  lateinit var to: String
}
