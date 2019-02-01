package com.example.base.model

import java.nio.file.Path

/**
 * Mail attachment
 * @author tree
 */
data class MailAttachmentModel(
  val fileName: String,
  val filePath: Path
)
