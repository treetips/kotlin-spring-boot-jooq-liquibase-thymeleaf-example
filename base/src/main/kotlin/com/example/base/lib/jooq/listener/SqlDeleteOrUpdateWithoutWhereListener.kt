package com.example.base.lib.jooq.listener

import org.jooq.ExecuteContext

/**
 * Bad query listener
 * @see <a href="https://www.jooq.org/doc/latest/manual/sql-execution/execute-listeners/#N56A72%22">Bad query execution ExecuteListener</a>
 * @author tree
 */
class SqlDeleteOrUpdateWithoutWhereListener : AbstractJooqListener() {

  /**
   * {@inheritDoc}
   */
  override fun renderEnd(ctx: ExecuteContext) {
    if (ctx.sql().toLowerCase().matches("^(?:(update|delete)(?!.* where ).*)$".toRegex()))
      throw DeleteOrUpdateWithoutWhereException("UPDATE or DELETE statements not contain a WHERE clause! " +
        "SQL = ${ctx.sql()}")
  }

  inner class DeleteOrUpdateWithoutWhereException(msg: String) : RuntimeException(msg)
}
