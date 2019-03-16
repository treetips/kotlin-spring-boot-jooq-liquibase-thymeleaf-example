package com.example.batch.model

import com.github.mygreen.supercsv.annotation.CsvBean
import com.github.mygreen.supercsv.annotation.CsvColumn
import com.github.mygreen.supercsv.annotation.constraint.CsvRequire

/**
 * Sample csv model
 *
 * @author tree
 */
@CsvBean(header = true)
data class SampleCsvModel(

  @CsvColumn(number = 1)
  @CsvRequire
  var no: Int? = null,

  @CsvColumn(number = 2, label = "名前")
  @CsvRequire
  var name: String? = null
) {
  constructor() : this(no = null, name = null)
}
