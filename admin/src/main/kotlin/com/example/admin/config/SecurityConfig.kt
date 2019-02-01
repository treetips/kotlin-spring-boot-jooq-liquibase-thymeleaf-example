package com.example.admin.config

import com.example.admin.enums.UserRole
import com.example.base.service.AdminUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import reactor.core.publisher.Mono
import java.net.URI


/**
 * Admin security settings
 */
@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
  private val adminUserService: AdminUserService
) {

  @Bean
  fun springSecurityFilterChain(
    http: ServerHttpSecurity
  ): SecurityWebFilterChain {
    val httpSecurity = http.authorizeExchange()
      //--------------------------------------------------------
      // not require authentication
      //--------------------------------------------------------
      .pathMatchers(
        "/admin/login",
        "/admin/logout",
        "/public/**",
        "/static/**",
        "/error/**",
        "/free/**",
        "/favicon.ico",
        "/actuator/**"
      ).permitAll()
      //--------------------------------------------------------
      // require authentication
      //--------------------------------------------------------
      .pathMatchers(HttpMethod.GET, "/admin/login").permitAll()
      .pathMatchers(HttpMethod.GET, "/admin/admin").hasAuthority(UserRole.ADMIN.name)
      .pathMatchers(HttpMethod.GET, "/admin/staff").access(HasAnyAuthority(UserRole.ADMIN, UserRole.STAFF))
      .pathMatchers(HttpMethod.GET, "/admin").access(HasAnyAuthority(UserRole.ADMIN, UserRole.STAFF))
      .anyExchange().authenticated().and()
      //--------------------------------------------------------
      // login
      //--------------------------------------------------------
      .formLogin()
      .loginPage("/admin/login")
      .authenticationManager(authenticationManager())
      //--------------------------------------------------------
      // filter
      //--------------------------------------------------------
      .and()
      .addFilterAt(logoutWebFilter(), SecurityWebFiltersOrder.LOGOUT)
      //--------------------------------------------------------
      // header
      //--------------------------------------------------------
      .headers()
      .cache().disable()
      .and()
      .csrf().disable()
    return httpSecurity.build()
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(10)

  @Bean
  fun authenticationManager(): ReactiveAuthenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(adminUserService).apply {
    setPasswordEncoder(passwordEncoder())
  }

  /**
   * Handle logout
   */
  private fun logoutWebFilter(): LogoutWebFilter = LogoutWebFilter().apply {
    setRequiresLogoutMatcher(
      ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/admin/logout")
    )
    setLogoutSuccessHandler(RedirectServerLogoutSuccessHandler().apply {
      setLogoutSuccessUrl(URI.create("/admin/login?fromLogout"))
    })
  }

  /**
   * Like a old hasAnyRole
   */
  inner class HasAnyAuthority(
    private vararg val pageAuthorities: UserRole
  ) : ReactiveAuthorizationManager<AuthorizationContext> {

    /**
     * {@inheritDoc}
     */
    override fun check(
      authentication: Mono<Authentication>,
      authorizationContext: AuthorizationContext
    ): Mono<AuthorizationDecision> {
      return authentication
        .flatMap {
          //          val user = it.principal as UserModel
          val userAuthorities = it.authorities.map { grantedAuthority ->
            grantedAuthority.authority
          }
          val granted = listOf(*pageAuthorities).any { pageRole ->
            userAuthorities.contains(pageRole.name)
          }
          Mono.just(AuthorizationDecision(granted))
        }
    }
  }
}
