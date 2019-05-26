package com.example.api.model.request

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

/**
 * Bean validation sample model
 * @author tree
 */
data class BeanValidationSampleRequestModel (
  @field:NotBlank
  @field:Min(value = 1)
  @field:Max(value = 3)
  var id: String
)
