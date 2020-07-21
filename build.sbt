enablePlugins(GitVersioning)

name := "scala-file-event-processor"

ThisBuild / credentials ++= (sys.env.get("SYSTEM_ACCESSTOKEN") match {
  case Some(value) => Credentials("", "pkgs.dev.azure.com", "scalafeed", value) :: Nil
  case None => Nil
})


lazy val commonSettings =
  (scalaVersion := "2.13.3") ::
  (organization  := "com.github.zycrophat") ::
  (maintainer := "Andreas Steffan") ::
  (git.useGitDescribe := true) ::
  (coverageEnabled.in(Test, test) := true) ::
  (coverageEnabled in(Compile, compile) := false) ::
  (publishMavenStyle := true) ::
    (publishTo := {
      val mavenRepositoryLocation = "https://pkgs.dev.azure.com/zycrophat/scala-file-event-processor/_packaging/scalafeed/maven/v1"
      if (isSnapshot.value)
        Some("snapshots" at mavenRepositoryLocation)
      else
        Some("releases"  at mavenRepositoryLocation)
    }) ::
  Nil

publishArtifact := false

lazy val root = (project in file("."))
  .aggregate(scalaFileEventProcessorFunction)
  .settings(commonSettings)

lazy val scalaFileEventProcessorFunction = (project in file("scala-file-event-processor-function"))
  .settings(commonSettings)
