package com.example.base.enums

/**
 * Mail mime-type
 * @author tree
 */
enum class MailMimeType(
  val cd: Byte
) {
  TEXT(1.toByte()),
  HTML(2.toByte()),
  MULTIPART(3.toByte())
  ;
}
