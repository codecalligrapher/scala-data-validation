package com.codecalligrapher.validation

import org.apache.spark.sql.SparkSession

object DataValidationJob {
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println("Usage: DataValidationJob <input-path> <validation-config-path>")
      System.exit(1)
    }
    
    val inputPath = args(0)
    val configPath = args(1)
    
    val spark = SparkSession.builder()
      .appName("Data Validation Pipeline")
      .getOrCreate()
    
    try {
      val processor = new ValidationProcessor(spark)
      val config = processor.loadValidationConfig(configPath)
      
      val inputDf = spark.read.format("delta").load(inputPath)
      val validatedDf = processor.processValidations(inputDf, config)
      
      // Write validation results
      validatedDf.write
        .format("delta")
        .mode("overwrite")
        .save(inputPath + "_validated")
        
    } finally {
      spark.stop()
    }
  }
}