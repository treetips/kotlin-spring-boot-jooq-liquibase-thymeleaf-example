package com.example.batch.config

import com.example.batch.batch.Sample1Batch
import com.example.batch.batch.Sample2Batch
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * アプリケーション設定です。
 * @Author tree
 */
@Configuration
@EnableCaching
class BatchConfig {

  companion object {
    /**
     * (ex) java -jar -Dbatch.name=XXX batch.jar
     */
    private const val VM_OPTION = "batch.name"
  }

  @Bean
  @ConditionalOnProperty(value = [VM_OPTION], havingValue = "Sample1Batch")
  fun sample1Batch() = Sample1Batch()

  @Bean
  @ConditionalOnProperty(value = [VM_OPTION], havingValue = "Sample2Batch")
  fun sample2Batch() = Sample2Batch()
}
