package com.example.batch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans


@SpringBootApplication
@EnableBatchProcessing
@ComponentScans(
  ComponentScan("com.example.base.service"),
  ComponentScan("com.example.base.repository"),
  ComponentScan("com.example.base.setting"),
  ComponentScan("com.example.base.config")
)
class BatchApplication

fun main(args: Array<String>) {
  runApplication<BatchApplication>(*args)
}
