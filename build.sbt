ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "com.codecalligrapher"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Hello",
    libraryDependencies ++= Seq(
      "org.scala-lang" %% "toolkit" % "0.1.7",
      "org.scalanlp" %% "breeze" % "2.1.0",  // Linear algebra library
      "org.apache.spark" %% "spark-core" % "3.5.0",
      "org.apache.spark" %% "spark-sql" % "3.5.0",
      "org.scala-lang" %% "toolkit-test" % "0.1.7" % Test
    )
  )