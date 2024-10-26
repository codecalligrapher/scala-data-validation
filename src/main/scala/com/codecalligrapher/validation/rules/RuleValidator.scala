package com.codecalligrapher.validation.rules

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.functions._
import com.codecalligrapher.validation.model.ValidationRule

trait RuleValidator {
  def validate(df: DataFrame, rule: ValidationRule): DataFrame
}

class NotNullValidator extends RuleValidator {
  override def validate(df: DataFrame, rule: ValidationRule): DataFrame = {
    df.withColumn(
      s"${rule.column}_validation",
      when(col(rule.column).isNull, "FAILED")
        .otherwise("PASSED")
    )
  }
}

class RangeValidator extends RuleValidator {
  override def validate(df: DataFrame, rule: ValidationRule): DataFrame = {
    val min = rule.parameters("min").toDouble
    val max = rule.parameters("max").toDouble
    
    df.withColumn(
      s"${rule.column}_validation",
      when(col(rule.column).between(min, max), "PASSED")
        .otherwise("FAILED")
    )
  }
}