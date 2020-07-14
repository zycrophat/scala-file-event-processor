enablePlugins(GitVersioning)

name := "scala-file-event-processor"

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / organization := "com.github.zycrophat"
ThisBuild / git.useGitDescribe := true
ThisBuild / coverageEnabled.in(Test, test) := true
ThisBuild / coverageEnabled in(Compile, compile) := false

lazy val root = (project in file("."))
  .aggregate(scalaFileEventProcessorFunction)

lazy val scalaFileEventProcessorFunction = project in file("scala-file-event-processor-function")
