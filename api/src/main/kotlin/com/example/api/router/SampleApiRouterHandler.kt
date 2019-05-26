package com.example.api.router

import com.example.api.model.request.BeanValidationSampleRequestModel
import com.example.api.service.SampleService
import com.example.base.model.SampleJsonModel
import com.example.base.router.RequestValidateHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

/**
 * Sample api router handlers
 * @author tree
 */
@Component
class SampleApiRouterHandler(
  private val validator: Validator,
  private val requestValidateHandler: RequestValidateHandler,
  private val sampleService: SampleService
) {

  private val log: Logger = LoggerFactory.getLogger(javaClass)

  /**
   * Send text mail
   * @param request ServerRequest
   * @return ServerResponse
   */
  fun handleSendTextMail(
    request: ServerRequest
  ): Mono<ServerResponse> {
    sampleService.sendTextMail()
    return ServerResponse.ok().body(BodyInserters.fromObject("Text mail transmitted."))
  }

  /**
   * Send html mail
   * @param request ServerRequest
   * @return ServerResponse
   */
  fun handleSendHtmlMail(
    request: ServerRequest
  ): Mono<ServerResponse> {
    sampleService.sendHtmlMail()
    return ServerResponse.ok().body(BodyInserters.fromObject("Html mail transmitted."))
  }

  /**
   * Send multipart mail
   * @param request ServerRequest
   * @return ServerResponse
   */
  fun handleSendMultipartMail(
    request: ServerRequest
  ): Mono<ServerResponse> {
    sampleService.sendMultipartMail()
    return ServerResponse.ok().body(BodyInserters.fromObject("Multipart mail transmitted."))
  }

  /**
   * Json request
   * @param request ServerRequest
   * @return ServerResponse
   */
  fun handleJsonRequest(
    request: ServerRequest
  ): Mono<ServerResponse> = request.bodyToMono(Array<SampleJsonModel>::class.java)
    .flatMap { array ->
      array.forEach {
        println(it)
      }
      ServerResponse.ok().body(BodyInserters.fromObject(
        mapOf("result" to true, "message" to "", "data" to array)
      ))
    }
    .switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromObject(
      mapOf("result" to false, "message" to "Request body is empty.")
    )))

  /**
   * Bean Validation
   * @param request ServerRequest
   * @return ServerResponse
   */
  fun handleBeanValidation(
    request: ServerRequest
  ): Mono<ServerResponse> {
    return requestValidateHandler.validate(
      request = request,
      responseClass = BeanValidationSampleRequestModel::class.java,
      onSuccess = {
        ServerResponse.ok().body(BodyInserters.fromObject("ok"))
      }
    )
  }
}
