package com.example.batch.batch

import com.example.batch.service.SampleService
import org.springframework.beans.factory.annotation.Autowired

/**
 * Sample batch 1
 * @author tree
 */
class Sample1Batch : AbstractBatch() {

  @Autowired
  private lateinit var sampleService: SampleService

  /**
   * {@inheritDoc}
   */
  override fun getBatchName(): String = "Sample batch 1"

  /**
   * Call by CommandLineRunner
   */
  override fun run(args: Array<String>) {
    execute(args = args)
  }

  /**
   * {@inheritDoc}
   */
  public override fun process(): Boolean {
    sampleService.getAllPrefectures().forEach { prefecture ->
      log.info(prefecture.toString())
    }
    return true
  }
}
