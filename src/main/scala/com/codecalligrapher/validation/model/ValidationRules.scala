package com.codecalligrapher.validation.model

case class ValidationRule (
  name: String,
  description: String,
  column: String,
  validationType: String,
  parameters: Map[String, String]
)

case class ValidationConfig(
  tableName: String,
  rules: List[ValidationRule]
)