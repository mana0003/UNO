val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "uno",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    mainClass in Compile := Some("Main"),

    // Add dependencies
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.14",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test",
    libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test,
      libraryDependencies += "org.scalafx" %% "scalafx" % "23.0.1-R34",
      libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
        // Enable code coverage
    coverageEnabled :=
      true
    )