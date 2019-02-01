package com.example.base.model

/**
 * Mail body
 * @author tree
 */
data class MailBodyModel(
  /**
   * Thymeleaf template path
   * (ex) mail/sample_html_mail
   */
  val templatePath: String,
  /**
   * Thymeleaf variables
   */
  val mailVariables: Map<String, Any>
)
