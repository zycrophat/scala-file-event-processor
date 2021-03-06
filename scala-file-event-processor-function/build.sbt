import java.io.PrintWriter

import NativePackagerHelper._

name := "scala-file-event-processor-function"

enablePlugins(UniversalPlugin)
enablePlugins(BuildInfoPlugin)
buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := "zycrophat.fileeventprocessor"

libraryDependencies += Deps.scalaTest
libraryDependencies += Deps.azureFunctions
//libraryDependencies ++= Deps.logging
libraryDependencies += Deps.azureStorage
libraryDependencies += "com.azure" % "azure-cosmos" % "4.1.0"
//libraryDependencies += "org.json4s" %% "json4s-native" % "3.6.9"
//libraryDependencies += "org.json4s" %% "json4s-ext" % "3.6.9"

libraryDependencies ++= Seq(
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core"   % "2.5.0",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.5.0" % Provided // or "provided", but it is required only in compile-time
)

assemblyMergeStrategy in assembly := {
  case PathList(x @ _*) if x.head.startsWith("META-INF") => MergeStrategy.discard
  case PathList("module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}


val generateFunctionAppFiles = taskKey[File]("Generated Azure Function App files")
generateFunctionAppFiles / fileInputs += baseDirectory.value.toGlob / "scala-function-app" / "**.*"

generateFunctionAppFiles := {
  import org.fusesource.scalate._
  import Path.rebase
  import scala.sys.process.Process
  import sbt.Keys.streams
  val sourceDir = baseDirectory.value / "scala-function-app"
  val targetDir = baseDirectory.value / "target" / "scala-function-app-generated"
  val fatJar = (assembly in Compile).value
  val log = streams.value.log
  if (generateFunctionAppFiles.inputFileChanges.hasChanges) {
    val engine = new TemplateEngine
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

    Process("dotnet" :: "build" :: "-o" :: "bin" :: Nil, targetDir) ! log
  }

  targetDir
}

topLevelDirectory := None
mappings in Universal := {
  val azureFunctionAppFiles = contentOf(generateFunctionAppFiles.value)
  azureFunctionAppFiles
}
