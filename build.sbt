import scoverage.ScoverageKeys.coverageExcludedFiles

val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "uno",
    version := "0.1.0-SNAPSHOT",
    //javaHome := Some(file("/usr/lib/jvm/java-21-temurin")),


    scalaVersion := scala3Version,
    Compile / mainClass := Some("UNO.Main"),

    // Add dependencies
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.18",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % Test,
    libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test,
    libraryDependencies += "org.scalafx" %% "scalafx" % "23.0.1-R34",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "net.codingwell" %% "scala-guice" % "7.0.0",
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.3.0",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.5",
    // Enable code coverage
    coverageEnabled := true,
    coverageExcludedPackages := "UNO.*;.*view.*"
   // coverageExcludedFiles := ".*(UnoGUI|TUI).*"
    //coverageExcludedPackages := ".*",

    /*Compile / coverageExcludedPackages := {
        println("Excluded packages: " + coverageExcludedPackages.value)
        coverageExcludedPackages.value
    }*/

  )

