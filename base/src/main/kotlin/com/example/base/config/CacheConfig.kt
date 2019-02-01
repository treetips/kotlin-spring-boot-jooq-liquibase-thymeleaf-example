package com.example.base.config

import com.example.base.model.UserModel
import com.example.base.setting.RedisSetting
import com.example.base.spring.security.AdminUserMixin
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.security.jackson2.SecurityJackson2Modules
import java.time.Duration


/**
 * Cache config
 * @author tree
 */
@Configuration
@EnableCaching
class CacheConfig(
  private val objectMapper: ObjectMapper,
  private val redisSetting: RedisSetting
) {

  /**
   * Redis host
   */
  @Value("\${spring.redis.host}")
  private lateinit var redisHost: String
  /**
   * Redis port
   */
  @Value("\${spring.redis.port}")
  private lateinit var redisPort: String

  /**
   * Redis connection factory
   */
  @Bean
  fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory {
    val configuration = LettuceClientConfiguration.builder()
//      .useSsl().and()
      .commandTimeout(Duration.ofSeconds(redisSetting.commandTimeout.toLong()))
      .build()
    return LettuceConnectionFactory(RedisStandaloneConfiguration(redisHost, redisPort.toInt()), configuration);
  }

  /**
   * Redis cache manager
   */
  @Bean
  fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
    return RedisCacheManager.builder(redisConnectionFactory)
      .cacheDefaults(
        RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofSeconds(redisSetting.defaultTtl.toLong())) // default ttl
      )
      .build()
  }

  /**
   * Session serializer
   */
  @Bean
  fun springSessionDefaultRedisSerializer(): RedisSerializer<Any> {
    // Apply spring security extended module
    objectMapper.registerModules(SecurityJackson2Modules.getModules(this.javaClass.classLoader))
    // Spring security(reactive) support redis session
    objectMapper.addMixIn(UserModel::class.java, AdminUserMixin::class.java)
    return GenericJackson2JsonRedisSerializer(objectMapper)
  }
}
