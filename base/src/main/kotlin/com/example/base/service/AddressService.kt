package com.example.base.service

import com.example.base.repository.database.PrefectureRepository
import com.example.tables.pojos.Prefecture
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Address service
 */
@Service
class AddressService(
  private val prefectureRepository: PrefectureRepository
) : AbstractService() {

  /**
   * Get all prefectures
   * @return all prefectures
   */
  @Transactional(readOnly = true)
  fun getAllPrefectures(): List<Prefecture> = prefectureRepository.findAll()

  /**
   * Get prefecture
   * @param prefectureCd prefecture code
   * @return specify prefecture
   */
  @Transactional(readOnly = true)
  fun getPrefecture(
    prefectureCd: String
  ): Prefecture? = prefectureRepository.findByPrefectureCd(prefectureCd)
}
