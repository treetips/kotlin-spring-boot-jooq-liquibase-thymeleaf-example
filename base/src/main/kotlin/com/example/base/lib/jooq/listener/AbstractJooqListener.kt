package com.example.base.lib.jooq.listener

import org.jooq.impl.DefaultExecuteListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * JOOQ listener
 */
abstract class AbstractJooqListener : DefaultExecuteListener() {

  /**
   * Logger
   */
  protected val log: Logger = LoggerFactory.getLogger(javaClass)
}
