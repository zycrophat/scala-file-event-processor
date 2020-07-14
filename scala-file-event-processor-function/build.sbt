import java.io.PrintWriter

import NativePackagerHelper._

name := "scala-file-event-processor-function"

enablePlugins(UniversalPlugin)

libraryDependencies += Deps.scalaTest
libraryDependencies += Deps.azureFunctions
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3" % Runtime
libraryDependencies += "com.azure" % "azure-storage-blob" % "12.7.0"

assemblyMergeStrategy in assembly := {
  case PathList(x @ _*) if x.head.startsWith("META-INF") => MergeStrategy.discard
  case PathList("module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}


val generateFunctionAppFiles = taskKey[File]("Generated Azure Function App files")


generateFunctionAppFiles := {
  import org.fusesource.scalate._
  import Path.rebase

  val sourceDir = baseDirectory.value / "scala-function-app"
  val targetDir = baseDirectory.value / "target" / "scala-function-app-generated"
  val engine = new TemplateEngine
  val fatJar = (assembly in Compile).value
  val fatJarTarget = targetDir / fatJar.getName
  IO.copyFile(fatJar, fatJarTarget)
  val templateVariables = Map("appjar" -> s"../${fatJarTarget.getName}")

  (((sourceDir ** "*") filter { !_.isDirectory }).get() pair rebase(sourceDir, targetDir))
    .map { case (src, dest) =>
      if (dest.ext == "mustache") {
        (src, file(dest.getParent) / dest.base)
      } else {
        (src, dest)
      }
    }
    .foreach { case(src, dest) =>
      if (src.ext == "mustache") {
        dest.getParentFile.mkdirs()
        val pw = new PrintWriter(dest)
        engine.layout(src.toString, pw, templateVariables)
        pw.close()

      } else {
        IO.copyFile(src, dest)
      }
    }

  targetDir
}

topLevelDirectory := None
mappings in Universal := {
  val azureFunctionAppFiles = contentOf(generateFunctionAppFiles.value)
  azureFunctionAppFiles
}