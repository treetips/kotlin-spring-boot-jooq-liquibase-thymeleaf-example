package com.example.batch.batch

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner

/**
 * Base batch
 * @author tree
 */
abstract class AbstractBatch : CommandLineRunner {

  protected val log: Logger = LoggerFactory.getLogger(javaClass)

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

    val start = System.currentTimeMillis()
    return try {
      log.info("[${getBatchName()}] start.")
      if (!process()) return ReturnCode.FAILURE
      log.info("[${getBatchName()}] succeeded. elapsed time = ${System.currentTimeMillis() - start} ms")
      ReturnCode.SUCCESS
    } catch (t: Throwable) {
      log.error("[${getBatchName()}] failed. elapsed time = ${System.currentTimeMillis() - start} ms", t)
      ReturnCode.FAILURE
    }
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
}
