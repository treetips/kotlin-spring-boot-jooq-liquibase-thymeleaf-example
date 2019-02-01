package com.example.api.router

import com.example.api.service.SampleService
import com.example.base.model.SampleJsonModel
import org.springframework.stereotype.Component
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
  private val sampleService: SampleService
) {

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
}
