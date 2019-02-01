package com.example.batch.service

import com.example.base.service.AbstractService
import com.example.base.service.AddressService
import com.example.tables.pojos.Prefecture
import org.springframework.stereotype.Service

/**
 * Sample service
 * @author tree
 */
@Service
class SampleService(
  private val addressService: AddressService
) : AbstractService() {

  /**
   * Get all prefectures
   * @return prefectures
   */
  fun getAllPrefectures(): List<Prefecture> = addressService.getAllPrefectures()
}
