package com.example.batch.batch

import com.example.base.model.MailBodyModel
import com.example.base.model.MailHeaderModel
import com.example.base.service.SendMailService
import com.example.batch.model.BatchResultModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.mail.internet.InternetAddress


/**
 * Base batch
 * @author tree
 */
abstract class AbstractBatch : CommandLineRunner {

  protected val log: Logger = LoggerFactory.getLogger(javaClass)
  protected val appendNotificationMap = linkedMapOf<String, Any>()
  @Value("\${spring.profiles.active}")
  private lateinit var activeProfile: String
  @Autowired
  private lateinit var sendMailService: SendMailService

  /**
   * Batch name
   * @return batch name
   */
  protected abstract fun getBatchName(): String

  /**
   * Implements batch logic.
   * @return batch result
   */
  protected abstract fun process(): Boolean

  /**
   * Optionally override and write validation logic.
   * @param args Command line parameters
   * @return validation result
   */
  protected fun validate(args: Array<String>): Boolean = true

  /**
   * Execute batch.
   * @param args Command line parameters
   * @return ReturnCode
   */
  protected fun execute(args: Array<String>): ReturnCode {
    if (!validate(args)) {
      log.warn("Validation error occurred.")
      return ReturnCode.FAILURE
    }

    var result: ReturnCode = ReturnCode.SUCCESS
    var exception: Throwable? = null
    val startDateTime = LocalDateTime.now()

    try {
      log.info("[${getBatchName()}] start.")
      if (!process()) result = ReturnCode.FAILURE
    } catch (t: Throwable) {
      result = ReturnCode.FAILURE
      exception = t
    } finally {
      val endDateTime = LocalDateTime.now()
      val elapsedTime = getElapsedTime(start = startDateTime, end = endDateTime)

      when (result) {
        ReturnCode.SUCCESS -> log.info("[${getBatchName()}] success. elapsed time=[$elapsedTime]m")
        else -> log.error("[${getBatchName()}] failure. elapsed time=[$elapsedTime]m", exception)
      }

      try {
        sendNotificationMail(
          BatchResultModel(
            activeProfile = activeProfile,
            batchName = getBatchName(),
            batchResult = result,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            elapsedTime = elapsedTime,
            stackTrace = exception?.toString(),
            appendNotificationMap = appendNotificationMap
          )
        )
      } catch (t: Throwable) {
        log.error(t.message, t)
      }
    }

    return result
  }

  /**
   * Command line exit code.
   */
  enum class ReturnCode(
    val returnCd: Int
  ) {
    SUCCESS(0),
    FAILURE(1),
    ;
  }

  /**
   * get elapsed time
   * @param start batch start time
   * @param end batch end time
   * @return time of minutes
   */
  private fun getElapsedTime(start: LocalDateTime, end: LocalDateTime): String {
    val elapsedMills = ChronoUnit.MILLIS.between(start, end)
    val result = BigDecimal(elapsedMills).divide(BigDecimal(60 * 1000), 2, RoundingMode.FLOOR)
    return DecimalFormat("#,###.##").format(result)
  }

  /**
   * send batch result mail
   * @param batchResultModel BatchResultModel
   */
  private fun sendNotificationMail(batchResultModel: BatchResultModel) {
    println(batchResultModel)
    sendMailService.sendHtmlMail(
      header = MailHeaderModel(
        subject = "[$activeProfile] ${getBatchName()} ${if (batchResultModel.batchResult == ReturnCode.SUCCESS) "success" else "failure"}",
        from = InternetAddress("sample@example.com", "FROM"),
        to = listOf(InternetAddress("sample@example.com", "TO"))
      ),
      body = MailBodyModel(
        templatePath = "mail/batch_result_notification.html",
        mailVariables = mapOf<String, Any>("batchResultModel" to batchResultModel)
      )
    )
  }
}
