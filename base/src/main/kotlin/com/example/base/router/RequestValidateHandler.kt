package com.example.base.router

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

/**
 * Request handoer with bean validation
 * @see https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-fn-handler-validation
 * @author tree
 */
@Component
class RequestValidateHandler (
  private val validator: Validator
) {

  private val log: Logger = LoggerFactory.getLogger(javaClass)

  /**
   * handle request with bean validation
   * @param request ServerRequest
   * @param responseClass request binder
   * @param onSuccess valid function
   * @param onValidationError invalid function
   * @return ServerResponse
   */
  fun <BODY> validate(
    request: ServerRequest,
    responseClass: Class<BODY>,
    onSuccess: (body: Mono<BODY>) -> Mono<ServerResponse>,
    onValidationError: (() -> Mono<ServerResponse>)? = null
  ): Mono<ServerResponse> {
    return request
      .bodyToMono(responseClass)
      .flatMap {body ->
        val bindingResult = BeanPropertyBindingResult(body, "body")
        validator.validate(body!!, bindingResult)
        if (bindingResult.hasErrors()) {
          if (onValidationError != null) {
            onValidationError()
          } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.toString())
          }
        }
        onSuccess(Mono.just(body))
      }
  }
}
