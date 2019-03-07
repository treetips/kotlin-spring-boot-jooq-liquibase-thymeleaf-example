package com.example.api.spring.actuator

import org.springframework.boot.actuate.health.AbstractHealthIndicator
import org.springframework.boot.actuate.health.Health
import org.springframework.stereotype.Component

/**
 * Append custom health check to actuator health
 *
 * @author tree
 */
@Component
class CustomHealthCheck : AbstractHealthIndicator() {

  override fun doHealthCheck(bldr: Health.Builder) {
    val running = true
    if (running) {
      bldr.up().withDetail("up key!!", "up value!!")
    } else {
      bldr.down().withDetail("down key!!", "down value!!")
    }
  }
}
