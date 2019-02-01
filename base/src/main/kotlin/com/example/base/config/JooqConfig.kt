package com.example.base.config

import com.example.base.lib.jooq.listener.SqlDeleteOrUpdateWithoutWhereListener
import com.example.base.lib.jooq.listener.SqlSlowQueryLogListener
import com.example.base.setting.JooqSetting
import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.conf.RenderNameStyle
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy

/**
 * JOOQ config
 * @Author tree
 */
@Configuration
class JooqConfig(
  private val jooqSetting: JooqSetting,
  private val hikariDataSource: HikariDataSource
) {

  /**
   * Jooq context
   */
  @Bean
  fun dslContext(): DSLContext {
    return DSL.using(
      DefaultConfiguration().apply {
        set(DataSourceConnectionProvider(TransactionAwareDataSourceProxy(hikariDataSource)))
        set(Settings().apply {
          // Log sql
          withExecuteLogging(true)
          // Output schema
          withRenderSchema(false)
          // Pretty print
          withRenderFormatted(true)
          // Style
          withRenderNameStyle(RenderNameStyle.LOWER)
        })
        set(
          // https://www.jooq.org/doc/latest/manual/sql-execution/execute-listeners/
          DefaultExecuteListenerProvider(SqlSlowQueryLogListener(jooqSetting)),
          DefaultExecuteListenerProvider(SqlDeleteOrUpdateWithoutWhereListener())
        )
      }
    )
  }
}
