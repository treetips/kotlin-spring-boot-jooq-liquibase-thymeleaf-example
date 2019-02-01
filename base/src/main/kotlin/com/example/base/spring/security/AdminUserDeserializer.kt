package com.example.base.spring.security

import com.example.base.model.UserModel
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.types.ULong
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AdminUserDeserializer : JsonDeserializer<UserModel>() {

  override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): UserModel {
    val mapper = jsonParser.codec as ObjectMapper
    val jsonNode: JsonNode = mapper.readTree(jsonParser)

    return UserModel(
      userId = ULong.valueOf(jsonNode.readText("userId")),
      userName = jsonNode.readText("userName"),
      userPassword = jsonNode.readText("userPassword"),
      roleCd = jsonNode.readText("roleCd"),
      roleName = jsonNode.readText("roleName"),
      credentialsExpiredFlg = jsonNode.readText("credentialsExpiredFlg").toBoolean(),
      accountExpiredFlg = jsonNode.readText("accountExpiredFlg").toBoolean(),
      accountLockedFlg = jsonNode.readText("accountLockedFlg").toBoolean(),
      enabledFlg = jsonNode.readText("enabledFlg").toBoolean(),
      createdAt = LocalDateTime.parse(jsonNode.readText("createdAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
      updatedAt = LocalDateTime.parse(jsonNode.readText("updatedAt"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
    )
  }

  private fun JsonNode.readText(field: String, defaultValue: String = ""): String {
    return when {
      has(field) -> get(field).asText(defaultValue)
      else -> defaultValue
    }
  }
}
