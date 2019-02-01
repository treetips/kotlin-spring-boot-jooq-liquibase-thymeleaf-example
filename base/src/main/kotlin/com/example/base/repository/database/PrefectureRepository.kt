package com.example.base.repository.database

import com.example.Tables
import com.example.tables.pojos.Prefecture
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

/**
 * Prefecture repository
 * @author tree
 */
@Repository
class PrefectureRepository : AbstractRepository() {

  /**
   * Get all prefectures
   * @return All prefectures
   */
  @Cacheable(
    cacheNames = ["TEST"],
    key = "#root.targetClass.name + '.' + #root.methodName"
  )
  fun findAll(): List<Prefecture> {
    val pref = Tables.PREFECTURE
    return jooq
      .selectFrom(pref)
      .fetchInto(Prefecture::class.java)
  }

  /**
   * Get prefecture
   * @param prefectureCd prefecture code
   * @return Prefecture
   */
  @Cacheable(
    cacheNames = ["TEST"],
    key = "#root.targetClass.name + '.' + #root.methodName + '-' + #prefectureCd"
  )
  fun findByPrefectureCd(
    prefectureCd: String
  ): Prefecture? {
    val pref = Tables.PREFECTURE
    return jooq
      .selectFrom(pref)
      .where()
      .and(pref.PREFECTURE_CD.eq(prefectureCd))
      .fetchOneInto(Prefecture::class.java)
  }
}
