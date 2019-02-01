package com.example.admin.exception

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.reactor.mono
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono


/**
 * Exception handler for inside controller (not supported outside controller)
 * @author tree
 */
@ControllerAdvice
class AdminExceptionHandler {

  private val log: Logger = LoggerFactory.getLogger(javaClass)

  /**
   * Handle response
   *
   * @param statusCode HTTP status code
   * @param exception Exception
   * @param htmlPath html template path
   * @return html
   */
  private fun handleResponse(
    statusCode: HttpStatus,
    exception: Throwable,
    htmlPath: String,
    isError: Boolean = false
  ): Mono<String> {
    if (isError) {
      log.error(exception.message, exception)
    } else {
      log.warn(exception.message)
    }
    return GlobalScope.mono { htmlPath }
  }

  /**
   * Handle admin access denied
   *
   * @param exception Exception
   * @return html
   */
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler(AccessDeniedException::class)
  fun handleAdminAccessDenied(exception: Throwable): Mono<String> = handleResponse(
    statusCode = HttpStatus.FORBIDDEN,
    exception = exception,
    htmlPath = "error/403.html"
  )

  /**
   * Handle default bad request error
   *
   * @param exception Exception
   * @return html
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResponseStatusException::class)
  fun handleServerWebInputException(exception: Throwable): Mono<String> = handleResponse(
    statusCode = HttpStatus.NOT_FOUND,
    exception = exception,
    htmlPath = "error/404.html"
  )

  /**
   * Handle internal server error
   *
   * @param exception Exception
   * @return html
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Throwable::class)
  fun handleInternalServerError(exception: Throwable): Mono<String> = handleResponse(
    statusCode = HttpStatus.INTERNAL_SERVER_ERROR,
    exception = exception,
    htmlPath = "error/500.html",
    isError = true
  )
}
