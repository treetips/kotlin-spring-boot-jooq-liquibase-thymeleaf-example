package com.example.base.lib.jooq.listener

import com.example.base.setting.JooqSetting
import org.jooq.ExecuteContext
import org.jooq.tools.StopWatch

/**
 * Slow query log listener
 * @author tree
 */
class SqlSlowQueryLogListener(
  /**
   * JOOQ設定
   */
  private val jooqSetting: JooqSetting
) : AbstractJooqListener() {

  private lateinit var stopWatch: StopWatch

  /**
   * {@inheritDoc}
   */
  override fun executeStart(ctx: ExecuteContext) {
    super.executeStart(ctx)
    stopWatch = StopWatch()
  }

  /**
   * {@inheritDoc}
   */
  override fun executeEnd(ctx: ExecuteContext) {
    super.executeEnd(ctx)
    val elapsedNanoTime = stopWatch.split()
    val elapsedNanoTimeStr = StopWatch.format(elapsedNanoTime)
    val query = ctx.query()
    if (elapsedNanoTime > (jooqSetting.slowQueryLogThreshold.toLong() * 1000000))
      log.warn("Slow query found. elapsed time = $elapsedNanoTimeStr \n$query")
  }
}
