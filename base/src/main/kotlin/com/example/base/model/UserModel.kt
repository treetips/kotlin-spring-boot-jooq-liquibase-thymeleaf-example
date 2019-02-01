package com.example.base.model

import org.jooq.types.ULong
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

data class UserModel(
  val userId: ULong,
  val userName: String,
  val userPassword: String,
  val roleCd: String,
  val roleName: String,
  val credentialsExpiredFlg: Boolean,
  val accountExpiredFlg: Boolean,
  val accountLockedFlg: Boolean,
  val enabledFlg: Boolean,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime
) : UserDetails {

  /**
   * {@inheritDoc}
   */
  override fun getUsername(): String = userName

  /**
   * {@inheritDoc}
   */
  override fun getPassword(): String = userPassword

  /**
   * {@inheritDoc}
   */
  override fun getAuthorities(): MutableCollection<out GrantedAuthority> = AuthorityUtils.createAuthorityList(roleName)

  /**
   * {@inheritDoc}
   */
  override fun isCredentialsNonExpired(): Boolean = credentialsExpiredFlg

  /**
   * {@inheritDoc}
   */
  override fun isAccountNonExpired(): Boolean = accountExpiredFlg

  /**
   * {@inheritDoc}
   */
  override fun isAccountNonLocked(): Boolean = accountLockedFlg

  /**
   * {@inheritDoc}
   */
  override fun isEnabled(): Boolean = enabledFlg

}
