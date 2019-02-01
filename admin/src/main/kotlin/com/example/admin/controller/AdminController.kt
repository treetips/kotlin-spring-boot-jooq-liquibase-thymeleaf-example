package com.example.admin.controller

import com.example.base.model.UserModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.reactor.mono
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.security.Principal

@Controller
@RequestMapping("/admin")
class AdminController {

  private val log: Logger = LoggerFactory.getLogger(javaClass)

  @GetMapping("/")
  fun adminIndex(principal: Principal): Mono<String> {
    val authentication = principal as Authentication
    val userModel = authentication.principal as UserModel
    log.info("authentication = $authentication")
    log.info("adminUserModel = $userModel")
    authentication.authorities.forEach {
      log.info("authority = ${it.authority}")
    }
    return GlobalScope.mono {
      "admin/index.html"
    }
  }

  @GetMapping("/login")
  fun adminLogin(): Mono<String> = GlobalScope.mono { "admin/login.html" }

  @GetMapping("/logout")
  fun adminLogout(): Mono<String> = GlobalScope.mono { "admin/logout.html" }

  @GetMapping("/admin")
  fun adminOwner(): Mono<String> = GlobalScope.mono { "admin/admin.html" }

  @GetMapping("/staff")
  fun adminRegular(): Mono<String> = GlobalScope.mono { "admin/staff.html" }
}
