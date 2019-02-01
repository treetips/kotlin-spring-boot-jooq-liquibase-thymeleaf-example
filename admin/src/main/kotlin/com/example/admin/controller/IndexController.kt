package com.example.admin.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

@Controller
@RequestMapping("/")
class IndexController {

  @GetMapping
  fun index(): Mono<String> {
    return Mono.just("redirect:admin/")
  }
}
