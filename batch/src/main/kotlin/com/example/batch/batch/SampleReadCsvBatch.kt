package com.example.batch.batch

import com.example.batch.service.SampleService
import org.springframework.beans.factory.annotation.Autowired

/**
 * Sample read csv batch
 * @author tree
 */
class SampleReadCsvBatch : AbstractBatch() {

  @Autowired
  private lateinit var sampleService: SampleService

  /**
   * {@inheritDoc}
   */
  override fun getBatchName(): String = "Sample read csv batch"

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
    sampleService.readCsv()
    return true
  }
}
