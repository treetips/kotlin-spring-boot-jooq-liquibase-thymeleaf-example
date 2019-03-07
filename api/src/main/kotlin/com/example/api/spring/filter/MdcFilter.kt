package com.example.api.spring.filter

import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

/**
 * MDC for slf4j filter
 *
 * @author tree
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
class MdcFilter : WebFilter {

  override fun filter(serverWebExchange: ServerWebExchange,
                      webFilterChain: WebFilterChain): Mono<Void> {
    MDC.remove("x-test")
    val chain = webFilterChain.filter(serverWebExchange)
    MDC.put("x-test", "hello")
    return chain
  }
}
