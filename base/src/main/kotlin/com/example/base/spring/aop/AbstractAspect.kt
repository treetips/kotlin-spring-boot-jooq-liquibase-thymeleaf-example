package com.example.base.spring.aop

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Aspect
 * @author tree
 */
abstract class AbstractAspect {

  /**
   * Logger
   */
  protected val log: Logger = LoggerFactory.getLogger(javaClass)
}
