package com.example.api.exception

import com.example.base.exception.BadRequestException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.HandlerStrategies
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.result.view.ViewResolver
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

/**
 * Api exception handler
 * @author tree
 */
@Component
@Order(-2)
class ApiExceptionHandler : WebExceptionHandler {

  private val log: Logger = LoggerFactory.getLogger(javaClass)

  /**
   * {@inheritDoc}
   */
  override fun handle(
    exchange: ServerWebExchange,
    e: Throwable
  ): Mono<Void> = createErrorResponse(
    request = exchange.request,
    response = exchange.response,
    e = e
  )
    .flatMap {
      it.writeTo(
        exchange,
        HandlerStrategiesContext(
          strategies = HandlerStrategies.withDefaults()
        )
      )
    }
    .flatMap {
      Mono.empty<Void>()
    }

  /**
   * Create error response
   * @param request http request
   * @param response http response
   * @param e exception
   * @return error response
   */
  private fun createErrorResponse(
    request: ServerHttpRequest,
    response: ServerHttpResponse,
    e: Throwable
  ): Mono<ServerResponse> {
    val url = request.uri
    var validations: Map<String, Map<String, String>>? = null
    val httpStatus = when (e) {
      is ResponseStatusException -> e.status
      is UsernameNotFoundException -> HttpStatus.UNAUTHORIZED
      is BadRequestException -> {
        validations = e.errors
        HttpStatus.BAD_REQUEST
      }
      else -> {
        if (e is OutOfMemoryError) HttpStatus.SERVICE_UNAVAILABLE
        else response.statusCode ?: HttpStatus.INTERNAL_SERVER_ERROR
      }
    }
    val httpStatusCd = httpStatus.value()

    return when (httpStatus) {
      HttpStatus.BAD_REQUEST -> {
        ServerResponse.status(httpStatus).body(
          fromObject(validations ?: mapOf("errors" to e.message))
        )
      }
      HttpStatus.NOT_FOUND -> {
        // Ignore browser access
        if (!url.toString().endsWith("favicon.ico") && !url.toString().endsWith("/login"))
          log.warn("${e.message} URL=$url", e)
        createResponse(
          httpStatus = HttpStatus.NOT_FOUND,
          systemMessage = e.message,
          message = "$url is not found."
        )
      }
      else -> {
        when {
          httpStatus.is4xxClientError -> log.warn("${e.message} httpStatusCd=$httpStatusCd, URL=$url", e)
          httpStatus.is5xxServerError -> log.error("${e.message} httpStatusCd=$httpStatusCd, URL=$url", e)
        }
        createResponse(
          httpStatus = httpStatus,
          systemMessage = e.message,
          message = httpStatus.reasonPhrase
        )
      }
    }
  }

  /**
   * Create internal server error response
   * @param httpStatus HttpStatus
   * @param systemMessage e.message
   * @param message error message
   * @return error response
   */
  private fun createResponse(
    httpStatus: HttpStatus,
    systemMessage: String?,
    message: String
  ): Mono<ServerResponse> {
    return ServerResponse.status(httpStatus).body(
      fromObject(
        ApiResponseModel(
          httpStatus = httpStatus,
          systemMessage = systemMessage,
          message = message
        )
      )
    )
  }
}

private data class ApiResponseModel(
  val httpStatus: HttpStatus,
  val systemMessage: String?,
  val message: String
)

private class HandlerStrategiesContext(val strategies: HandlerStrategies) : ServerResponse.Context {
  /**
   * {@inheritDoc}
   */
  override fun messageWriters(): List<HttpMessageWriter<*>> = strategies.messageWriters()

  /**
   * {@inheritDoc}
   */
  override fun viewResolvers(): List<ViewResolver> = strategies.viewResolvers()
}
