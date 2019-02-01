package com.example.api.spring.aop

import com.example.base.spring.aop.AbstractAspect
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

/**
 * Start end logging
 */
@Component
@Aspect
class LogAspect : AbstractAspect() {

  /**
   * Router handler
   */
  @Pointcut("execution(* com.example.*.router..*.*(*))")
  fun routerHandlerMethodExecution() {
  }

  @Pointcut("@within(org.springframework.stereotype.Repository)")
  fun repositoryAnnotation() {
  }

  @Pointcut("@within(org.springframework.stereotype.Service)")
  fun serviceAnnotation() {
  }

  /**
   * Method start end logging
   * @param proceedingJoinPoint ProceedingJoinPoint
   * @return method return
   */
  @Around("routerHandlerMethodExecution()")
  fun logRouterHandlerStartEnd(
    proceedingJoinPoint: ProceedingJoinPoint
  ): Any? = logStartEnd(
    proceedingJoinPoint = proceedingJoinPoint,
    label = "ROUTER"
  )

  /**
   * Repository method start end logging
   * @param proceedingJoinPoint ProceedingJoinPoint
   * @return method return
   */
  @Around("repositoryAnnotation()")
  fun logRepositoryStartEnd(
    proceedingJoinPoint: ProceedingJoinPoint
  ): Any? = logStartEnd(
    proceedingJoinPoint = proceedingJoinPoint,
    label = "REPOSITORY"
  )

  /**
   * Repository method start end logging
   * @param proceedingJoinPoint ProceedingJoinPoint
   * @return method return
   */
  @Around("repositoryAnnotation()")
  fun logServiceStartEnd(
    proceedingJoinPoint: ProceedingJoinPoint
  ): Any? = logStartEnd(
    proceedingJoinPoint = proceedingJoinPoint,
    label = "SERVICE"
  )

  /**
   * Method start end logging
   * @param proceedingJoinPoint ProceedingJoinPoint
   * @param label log label
   * @return method return
   */
  private fun logStartEnd(
    proceedingJoinPoint: ProceedingJoinPoint,
    label: String
  ): Any? {
    val signature = proceedingJoinPoint.signature
    val classMethodName = "${signature.declaringType.simpleName}.${signature.name}()"
    log.info("[$label START] $classMethodName")

    val startTime = System.currentTimeMillis()
    return try {
      proceedingJoinPoint.proceed()
    } finally {
      val elapseTime = System.currentTimeMillis() - startTime
      log.info("[$label END] $classMethodName elapseTime = $elapseTime ms")
    }
  }
}
