package com.example.base.service

import com.example.base.repository.database.UserRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

/**
 * Admin user service
 * @author tree
 */
@Service
class AdminUserService(
  private val userRepository: UserRepository
) : ReactiveUserDetailsService, AbstractService() {

  /**
   * {@inheritDoc}
   */
  @Transactional(readOnly = true)
  override fun findByUsername(username: String): Mono<UserDetails> {
    val user = userRepository.findByUserName(userName = username)
    user ?: return Mono.empty()
    return Mono.just(user)
  }
}
