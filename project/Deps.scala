import sbt._

private object V {
  lazy val scalaTest = "3.2.0"
  lazy val azureFunctions = "1.3.1"
  lazy val scalaLogging = "3.9.2"
  lazy val logback = "1.2.3"
  lazy val azureStorage = "12.7.0"
  lazy val aiLoggingLogback = "2.6.1+"
}

object Deps {

  lazy val scalaTest = "org.scalatest" %% "scalatest" % V.scalaTest % Test
  private lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % V.scalaLogging
  private lazy val logback = "ch.qos.logback" % "logback-classic" % V.logback % Runtime
  private lazy val aiLoggingLogback = "com.microsoft.azure" % "applicationinsights-logging-logback"  % V.aiLoggingLogback % Runtime

  lazy val logging: Seq[ModuleID] = scalaLogging :: logback :: aiLoggingLogback :: Nil
  lazy val azureFunctions = "com.microsoft.azure.functions" % "azure-functions-java-library" % V.azureFunctions
  lazy val azureStorage = "com.azure" % "azure-storage-blob" % V.azureStorage
}
