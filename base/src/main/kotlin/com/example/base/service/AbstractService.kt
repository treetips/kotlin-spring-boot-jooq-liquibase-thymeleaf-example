package com.example.base.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Service
 */
abstract class AbstractService {

  /**
   * Logger
   */
  protected val log: Logger = LoggerFactory.getLogger(javaClass)

}
