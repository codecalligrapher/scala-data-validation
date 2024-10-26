package com.codecalligrapher.validation

import org.apache.spark.sql.{SparkSession, DataFrame}
import com.codecalligrapher.validation.model.{ValidationConfig, ValidationRule}
import com.codecalligrapher.validation.rules._
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import scala.io.Source

class ValidationProcessor(spark: SparkSession) {
  private val validators = Map[String, RuleValidator](
    "notNull" -> new NotNullValidator,
    "range" -> new RangeValidator
  )
  def loadValidationConfig(path: String): ValidationConfig = {
    val yaml = new Yaml(new Constructor(classOf[ValidationConfig]))
    val input = Source.fromFile(path).mkString
    yaml.load(input).asInstanceOf[ValidationConfig]
  }
  
  def processValidations(df: DataFrame, config: ValidationConfig): DataFrame = {
    var resultDf = df
    
    config.rules.foreach { rule =>
      validators.get(rule.validationType) match {
        case Some(validator) =>
          resultDf = validator.validate(resultDf, rule)
        case None =>
          throw new IllegalArgumentException(s"Unknown validation type: ${rule.validationType}")
      }
    }
    
    resultDf
  }
}