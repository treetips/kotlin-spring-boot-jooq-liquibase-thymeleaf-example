package com.example.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ComponentScans

@SpringBootApplication
@ComponentScans(
  ComponentScan("com.example.base.service"),
  ComponentScan("com.example.base.repository"),
  ComponentScan("com.example.base.setting"),
  ComponentScan("com.example.base.config")
)
class ApiApplication

fun main(args: Array<String>) {
  runApplication<ApiApplication>(*args)
}
