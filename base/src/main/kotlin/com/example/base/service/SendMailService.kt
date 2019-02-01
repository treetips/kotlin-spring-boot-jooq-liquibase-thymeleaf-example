package com.example.base.service

import com.example.base.enums.MailMimeType
import com.example.base.model.MailBodyModel
import com.example.base.model.MailHeaderModel
import com.example.base.setting.DevMailSetting
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.nio.charset.Charset
import javax.mail.internet.InternetAddress


/**
 * Send mail service
 * @author tree
 */
@Service
class SendMailService(
  private var javaMailSender: JavaMailSender,
  private val springTemplateEngine: SpringTemplateEngine,
  private val devMailSetting: DevMailSetting
) : AbstractService() {

  @Value("\${spring.profiles.active}")
  private val activeProfile: String? = null

  companion object {
    private const val LF = "\n"
    private const val DEV_MAILING_LIST_TEMPLATE: String = "mailing-list+%s@example.com"
  }

  /**
   * Send text mail
   * @param header mail header
   * @param body mail body
   */
  fun sendTextMail(
    header: MailHeaderModel,
    body: MailBodyModel
  ) {
    validate(
      mimeType = MailMimeType.TEXT,
      header = header,
      textBody = body
    )
    executeSendMail(
      mimeType = MailMimeType.TEXT,
      header = header,
      textMailContent = createMailContent(body = body)
    )
  }

  /**
   * Send html mail
   * @param header mail header
   * @param body mail body
   */
  fun sendHtmlMail(
    header: MailHeaderModel,
    body: MailBodyModel
  ) {
    validate(
      mimeType = MailMimeType.HTML,
      header = header,
      htmlBody = body
    )
    executeSendMail(
      mimeType = MailMimeType.HTML,
      header = header,
      htmlMailContent = createMailContent(body = body)
    )
  }

  /**
   * Send multipart mail
   * @param header mail header
   * @param textBody mail body for text mail
   * @param htmlBody mail body for html mail
   */
  fun sendMultipartMail(
    header: MailHeaderModel,
    textBody: MailBodyModel,
    htmlBody: MailBodyModel
  ) {
    validate(
      mimeType = MailMimeType.MULTIPART,
      header = header,
      textBody = textBody,
      htmlBody = htmlBody
    )
    executeSendMail(
      mimeType = MailMimeType.MULTIPART,
      header = header,
      textMailContent = createMailContent(body = textBody),
      htmlMailContent = createMailContent(body = htmlBody)
    )
  }

  /**
   * Execute send mail
   * @param mimeType mime-type
   * @param header mail header
   * @param textMailContent text mail content
   * @param htmlMailContent html mail content
   */
  private fun executeSendMail(
    mimeType: MailMimeType,
    header: MailHeaderModel,
    textMailContent: String? = null,
    htmlMailContent: String? = null
  ) {

    // [For local environment]
    // Virtual smtp(MailCatcher) on docker on mac is used.
    // settings.dev-mail.* of application.yml is used.
    //
    // [For production environment]
    // AmazonSES is used.
    // cloud.aws.* of application.yml is used.
    if (activeProfile == "local") {
      javaMailSender = JavaMailSenderImpl().apply {
        protocol = devMailSetting.protocol
        host = devMailSetting.host
        port = devMailSetting.port.toInt()
        username = devMailSetting.username
        password = devMailSetting.password
        defaultEncoding = devMailSetting.defaultEncoding.toString()
      }
    }

    javaMailSender.send { sender ->
      val helper = MimeMessageHelper(
        sender,
        header.addAttachment != null || mimeType == MailMimeType.MULTIPART,
        header.encoding
      )

      val subject = header.subject
      val to = header.to.map { changeRawToDevMailAddress(it) }
      val cc = header.cc?.map { changeRawToDevMailAddress(it) }
      val bcc = header.bcc?.map { changeRawToDevMailAddress(it) }
      val from = header.from?.let { changeRawToDevMailAddress(header.from) }
      val replyTo = header.replyTo?.let { changeRawToDevMailAddress(header.replyTo) }

      // subject
      helper.setSubject(subject)
      // to
      helper.setTo(to.toTypedArray())
      // cc
      cc?.let { helper.setCc(it.toTypedArray()) }
      // bcc
      bcc?.let { helper.setBcc(it.toTypedArray()) }
      // from
      from?.let { helper.setFrom(it) }
      // reply-to
      replyTo?.let { helper.setReplyTo(it) }
      // x-priority
      header.priority?.let { helper.setPriority(it) }
      // sent-date
      header.sentDate?.let { helper.setSentDate(it) }
      // content
      helper.apply {
        when (mimeType) {
          MailMimeType.TEXT -> setText(textMailContent!!, false)
          MailMimeType.HTML -> setText(htmlMailContent!!, true)
          MailMimeType.MULTIPART -> setText(textMailContent!!, htmlMailContent!!)
        }
      }
      // attachment
      header.addAttachment?.let {
        helper.addAttachment(it.fileName, it.filePath.toFile())
      }

      // log mail server info and headers before transmit
      printSendMailInfo(
        MailHeaderModel(
          subject = subject,
          to = to,
          cc = cc,
          bcc = bcc,
          from = from,
          replyTo = replyTo,
          addAttachment = header.addAttachment,
          encoding = header.encoding,
          priority = header.priority,
          sentDate = header.sentDate,
          isLogMailHeaderInfo = header.isLogMailHeaderInfo
        )
      )
    }
  }

  /**
   * Create mail content
   * @param body mail body
   * @return mail content
   */
  private fun createMailContent(
    body: MailBodyModel
  ): String {
    return Context().apply {
      setVariables(body.mailVariables)
    }.let {
      springTemplateEngine.process(body.templatePath, it)
    }
  }

  /**
   * Validate mail
   * @param mimeType mime-type
   * @param header mail header
   * @param textBody text mail body
   * @param htmlBody html mail body
   */
  private fun validate(
    mimeType: MailMimeType,
    header: MailHeaderModel,
    textBody: MailBodyModel? = null,
    htmlBody: MailBodyModel? = null
  ) {
    validateHeader(
      header = header
    )
    validateBody(
      mimeType = mimeType,
      textBody = textBody,
      htmlBody = htmlBody
    )
  }

  /**
   * Validate mail header
   * @param header mail header
   * @return true=invalid, false=valid
   */
  private fun validateHeader(
    header: MailHeaderModel
  ) {

    var error = false

    fun containsAtmark(address: InternetAddress?, mailKind: String) {
      address ?: return
      if (!address.address.contains("@")) {
        error = true
        log.error("$mailKind is not include @. $mailKind=${header.from}")
      }
    }

    if (header.to.isEmpty()) {
      error = true
      log.error("mail-to is empty.")
    }

    containsAtmark(header.from, "mail:from")
    header.to.forEach { containsAtmark(it, "mail:to") }
    header.cc?.forEach { containsAtmark(it, "mail:cc") }
    header.bcc?.forEach { containsAtmark(it, "mail:bcc") }

    if (header.subject.trim().isEmpty()) {
      error = true
      log.error("Subject is empty. subject=${header.subject}")
    }

    header.addAttachment?.let {
      if (header.addAttachment.fileName.trim().isEmpty()) {
        error = true
        log.error("Attachment file name is empty.")
      }
    }

    try {
      Charset.forName(header.encoding)
    } catch (e: Throwable) {
      error = true
      log.error("Unable to set of undefined encoding. encoding=${header.encoding}", e)
    }

    if (header.priority != null && !(1..5).toList().contains(header.priority)) {
      error = true
      log.error("Unable to set of undefined priority. priority=${header.priority}")
    }

    if (error) throw IllegalArgumentException("Mail header include invalid value.")
  }

  /**
   * Validate mail body
   * @param mimeType mime-type
   * @param textBody text mail body
   * @param htmlBody html mail body
   */
  private fun validateBody(
    mimeType: MailMimeType,
    textBody: MailBodyModel? = null,
    htmlBody: MailBodyModel? = null
  ) {

    var error = false

    when (mimeType) {
      MailMimeType.TEXT -> {
        if (textBody == null) {
          log.error("Text mail body is not set.")
          error = true
        }
      }
      MailMimeType.HTML -> {
        if (htmlBody == null) {
          log.error("Html mail body is not set.")
          error = true
        }
      }
      MailMimeType.MULTIPART -> {
        if (textBody == null || htmlBody == null) {
          log.error("Text mail body and html mail body are not set.")
          error = true
        }
      }
    }

    if (error) throw IllegalArgumentException("Mail body include invalid value.")
  }

  /**
   * Change raw mail address to development mail address for gmail
   * @param address mail address
   */
  private fun changeRawToDevMailAddress(
    address: InternetAddress
  ): InternetAddress {
    if (activeProfile == "production") return address
    return InternetAddress(
      DEV_MAILING_LIST_TEMPLATE.format(address.address.replace("@", "_")),
      address.personal
    )
  }

  /**
   * Log mail informations
   * @param header mail header
   */
  private fun printSendMailInfo(
    header: MailHeaderModel
  ) {

    try {
      val headerMap = LinkedMultiValueMap<String, Any>()
      val headerInfoPrints = mutableListOf<String>()
      if (header.isLogMailHeaderInfo) {
        headerInfoPrints.add("#---------------------------------------------------------#")
        headerInfoPrints.add("#                    mail header info                     #")
        headerInfoPrints.add("#---------------------------------------------------------#")
        headerMap["subject"] = header.subject
        header.from?.let { headerMap["from"] = it }
        header.to.forEachIndexed { index, logTo ->
          headerMap["to ${index + 1}"] = logTo
        }
        header.cc?.let {
          it.forEachIndexed { index, logCc ->
            headerMap["cc ${index + 1}"] = logCc
          }
        }
        header.bcc?.let {
          it.forEachIndexed { index, logBcc ->
            headerMap["bcc ${index + 1}"] = logBcc
          }
        }
        header.replyTo?.let { headerMap["reply-to"] = it }
        header.encoding.let { headerMap["encoding"] = it }
        header.addAttachment?.let { headerMap["addAttachment"] = it }
        header.priority?.let { headerMap["priority"] = it }
        header.sentDate?.let { headerMap["sent-date"] = it }
      }

      val padEndDigit = 30

      val logStr = listOf(
        "-",
        // mail headers
        headerInfoPrints.joinToString(LF),
        headerMap.map {
          "# ${it.key.padEnd(padEndDigit, ' ')} = ${it.value}"
        }.joinToString(LF)
      ).filter {
        it.isNotBlank()
      }.joinToString(LF)

      if (logStr.isNotEmpty()) log.info(logStr)
    } catch (e: Throwable) {
      log.warn(e.message, e)
    }
  }
}
