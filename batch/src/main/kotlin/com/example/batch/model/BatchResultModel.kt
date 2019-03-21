package com.example.batch.model

import com.example.batch.batch.AbstractBatch
import java.time.LocalDateTime

/**
 * Batch mail variables
 *
 * @author tree
 */
data class BatchResultModel(
  val activeProfile: String,
  val batchName: String,
  val batchResult: AbstractBatch.ReturnCode,
  val startDateTime: LocalDateTime,
  val endDateTime: LocalDateTime,
  val elapsedTime: String,
  val stackTrace: String?,
  val appendNotificationMap: LinkedHashMap<String, Any> = linkedMapOf()
)
