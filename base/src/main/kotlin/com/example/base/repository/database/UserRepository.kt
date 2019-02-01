package com.example.base.repository.database

import com.example.Tables
import com.example.base.model.UserModel
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Repository

/**
 * User repository
 * @author tree
 */
@Repository
class UserRepository : AbstractRepository() {

  /**
   * Find a user
   * @param userName user name
   * @return user
   */
  fun findByUserName(
    userName: String
  ): UserDetails? {
    val u = Tables.USER.`as`("u")
    val r = Tables.ROLE.`as`("r")
    return jooq
      .select()
      .from(u)
      .innerJoin(r)
      .on(r.ROLE_CD.eq(u.ROLE_CD))
      .where()
      .and(u.USER_NAME.eq(userName))
      .and(u.ENABLED_FLG.isTrue)
      .and(u.ACCOUNT_EXPIRED_FLG.isFalse)
      .and(u.ACCOUNT_LOCKED_FLG.isFalse)
      .and(u.CREDENTIALS_EXPIRED_FLG.isFalse)
      .fetchOneInto(UserModel::class.java)
  }
}
