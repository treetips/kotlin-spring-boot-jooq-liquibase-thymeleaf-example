package com.example.base.repository.database

import org.jooq.DSLContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractRepository {

  /**
   * Logger
   */
  protected val log: Logger = LoggerFactory.getLogger(javaClass)

  /**
   * JOOQ context
   */
  @Autowired
  protected lateinit var jooq: DSLContext
}
