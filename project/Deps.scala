import sbt._

object V {
  lazy val scalaTest = "3.2.0"
  lazy val azureFunctions = "1.3.1"
}

object Deps {

  lazy val scalaTest = "org.scalatest" %% "scalatest" % V.scalaTest % Test
  lazy val azureFunctions = "com.microsoft.azure.functions" % "azure-functions-java-library" % V.azureFunctions
}
