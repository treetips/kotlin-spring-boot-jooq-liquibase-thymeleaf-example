package com.example.api.router

import com.example.base.service.AddressService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

/**
 * Api router handlers
 * @author tree
 */
@Component
class ApiRouterHandler(
  private val addressService: AddressService
) {

  /**
   * All prefectures
   * @param request ServerRequest
   * @return ServerResponse
   */
  fun handlePrefectures(
    request: ServerRequest
  ): Mono<ServerResponse> {
    val prefectures = addressService.getAllPrefectures()
    return ServerResponse.ok().body(fromObject(prefectures))
  }

  /**
   * Specify prefecture
   * @param request ServerRequest
   * @return ServerResponse
   */
  fun handlePrefecture(
    request: ServerRequest
  ): Mono<ServerResponse> {
    val prefectureCd = request.pathVariable("prefectureCd")
    val prefecture = addressService.getPrefecture(prefectureCd)
    prefecture ?: throw RuntimeException("Prefecture [$prefectureCd] is not found.")
    return ServerResponse.ok().body(fromObject(prefecture))
  }

  /**
   * Convert bcrypt password
   * @param request ServerRequest
   * @return ServerResponse
   */
  fun handleBcrypt(
    request: ServerRequest
  ): Mono<ServerResponse> {
    val rawPassword = request.pathVariable("rawPassword")
    val encoder: PasswordEncoder = BCryptPasswordEncoder(10)
    val encodedPassword = encoder.encode(rawPassword)
    return ServerResponse.ok().body(fromObject(encodedPassword))
  }

  /**
   * Download file response
   * @param fileName file name
   * @param content file content
   * @return response
   */
  private fun download(
    fileName: String,
    content: String
  ): Mono<ServerResponse> {
    val resource = ByteArrayResource(content.toByteArray())
    return ServerResponse.ok()
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
      .body(BodyInserters.fromObject(resource))
  }
}
