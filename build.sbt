enablePlugins(GitVersioning)

name := "scala-file-event-processor"

lazy val commonSettings =
  (scalaVersion := "2.13.3") ::
  (organization  := "com.github.zycrophat") ::
  (maintainer := "Andreas Steffan") ::
  (git.useGitDescribe := true) ::
  (coverageEnabled.in(Test, test) := true) ::
  (coverageEnabled in(Compile, compile) := false) ::
  Nil

lazy val root = (project in file("."))
  .aggregate(scalaFileEventProcessorFunction)
  .settings(commonSettings)

lazy val scalaFileEventProcessorFunction = (project in file("scala-file-event-processor-function"))
  .settings(commonSettings)
