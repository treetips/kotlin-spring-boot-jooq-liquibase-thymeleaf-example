package com.example.api.service

import com.example.api.setting.SampleMailSetting
import com.example.base.model.MailBodyModel
import com.example.base.model.MailHeaderModel
import com.example.base.service.AbstractService
import com.example.base.service.SendMailService
import org.springframework.stereotype.Service
import javax.mail.internet.InternetAddress

/**
 * Sample service
 * @author tree
 */
@Service
class SampleService(
  private val sendMailService: SendMailService,
  private val sampleMailSetting: SampleMailSetting
) : AbstractService() {

  companion object {
    private val LIST = listOf("kotlin", "springboot", "jooq", "LiquiBase")
  }

  /**
   * Send text mail
   */
  fun sendTextMail() {
    sendMailService.sendTextMail(
      header = MailHeaderModel(
        subject = "text mail sample",
        from = InternetAddress(sampleMailSetting.to, "FROM"),
        to = listOf(InternetAddress(sampleMailSetting.to, "TO"))
      ),
      body = MailBodyModel(
        templatePath = "mail/sample_mail.text",
        mailVariables = mapOf("hello" to "world", "list" to LIST)
      )
    )
  }

  /**
   * Send html mail
   */
  fun sendHtmlMail() {
    sendMailService.sendHtmlMail(
      header = MailHeaderModel(
        subject = "html mail sample",
        from = InternetAddress(sampleMailSetting.to, "FROM"),
        to = listOf(InternetAddress(sampleMailSetting.to, "TO"))
      ),
      body = MailBodyModel(
        templatePath = "mail/sample_mail.html",
        mailVariables = mapOf("hello" to "world", "list" to LIST)
      )
    )
  }

  /**
   * Send multipart(text + html) mail
   */
  fun sendMultipartMail() {
    sendMailService.sendMultipartMail(
      header = MailHeaderModel(
        subject = "multipart mail sample",
        from = InternetAddress(sampleMailSetting.to, "FROM"),
        to = listOf(InternetAddress(sampleMailSetting.to, "TO"))
      ),
      textBody = MailBodyModel(
        templatePath = "mail/sample_mail.text",
        mailVariables = mapOf("hello" to "world", "list" to LIST)
      ),
      htmlBody = MailBodyModel(
        templatePath = "mail/sample_mail.html",
        mailVariables = mapOf("hello" to "world", "list" to LIST)
      )
    )
  }
}
