package com.example.admin.controller

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.reactor.mono
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono

@Controller
class FreeController {

  @GetMapping("/free")
  fun freeIndex(): Mono<String> = GlobalScope.mono { "free/index.html" }
}
