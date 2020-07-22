import sbt._

private object V {
  lazy val scalaTest = "3.2.0"
  lazy val azureFunctions = "1.3.1"
  lazy val scalaLogging = "3.9.2"
  lazy val azureStorage = "12.7.0"
}

object Deps {

  lazy val scalaTest = "org.scalatest" %% "scalatest" % V.scalaTest % Test

  lazy val azureFunctions = "com.microsoft.azure.functions" % "azure-functions-java-library" % V.azureFunctions
  lazy val azureStorage = "com.azure" % "azure-storage-blob" % V.azureStorage
}
