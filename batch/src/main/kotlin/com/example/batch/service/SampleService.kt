package com.example.batch.service

import com.example.base.service.AbstractService
import com.example.base.service.AddressService
import com.example.batch.model.SampleCsvModel
import com.example.tables.pojos.Prefecture
import com.github.mygreen.supercsv.exception.SuperCsvBindingException
import com.github.mygreen.supercsv.io.CsvAnnotationBeanReader
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import org.supercsv.prefs.CsvPreference
import java.nio.charset.Charset
import java.nio.file.Files


/**
 * Sample service
 * @author tree
 */
@Service
class SampleService(
  private val resourceLoader: ResourceLoader,
  private val addressService: AddressService
) : AbstractService() {

  /**
   * Get all prefectures
   * @return prefectures
   */
  fun getAllPrefectures(): List<Prefecture> = addressService.getAllPrefectures()

  /**
   * Read csv by super-csv
   */
  fun readCsv() {
    val resource = resourceLoader.getResource("classpath:sample.csv")
    CsvAnnotationBeanReader(
      SampleCsvModel::class.java,
      Files.newBufferedReader(resource.file.toPath(), Charset.forName("UTF-8")),
      CsvPreference.STANDARD_PREFERENCE
    ).use { csvReader ->
      var record: SampleCsvModel? = null
      try {
        while ({ record = csvReader.read(); record }() != null) {
          log.info(record.toString());
        }
      } catch (e: SuperCsvBindingException) {
        e.processingErrors.forEach { error ->
          log.error("error = $error")
        }
      }
    }
  }
}
