package com.example.base.exception

class BadRequestException() : RuntimeException("Validation error.") {

  /**
   * Validation error info
   */
  var errors: Map<String, Map<String, String>>? = null

  /**
   * constructor
   * @param errors error info
   */
  constructor(errors: Map<String, Map<String, String>>) : this() {
    this.errors = errors
  }
}
