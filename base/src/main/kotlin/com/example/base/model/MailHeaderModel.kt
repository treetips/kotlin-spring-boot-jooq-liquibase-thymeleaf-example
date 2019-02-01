package com.example.base.model

import java.nio.charset.StandardCharsets
import java.util.*
import javax.mail.internet.InternetAddress

/**
 * Mail header
 * @author tree
 */
data class MailHeaderModel(
  /**
   * subject
   */
  val subject: String,
  /**
   * to
   */
  val to: List<InternetAddress>,
  /**
   * cc
   */
  val cc: List<InternetAddress>? = null,
  /**
   * bcc
   */
  val bcc: List<InternetAddress>? = null,
  /**
   * from
   */
  val from: InternetAddress? = null,
  /**
   * reply-to
   */
  val replyTo: InternetAddress? = null,
  /**
   * attachment
   */
  val addAttachment: MailAttachmentModel? = null,
  /**
   * encoding
   */
  val encoding: String = StandardCharsets.UTF_8.name(),
  /**
   * X-Priority（not recommended）
   * 1: High
   * 2:
   * 3: Normal
   * 4:
   * 5: Low
   */
  val priority: Int? = null,
  /**
   * sent-date (not recommended)
   */
  val sentDate: Date? = null,
  /**
   * Log mail header before transmit
   */
  val isLogMailHeaderInfo: Boolean = true
)
