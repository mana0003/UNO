val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "uno",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    mainClass in Compile := Some("scala.Main"),

    // Add dependencies
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.14",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % "test",
    libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.10.0" % Test,
    //libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4",
    //libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2",

    //resolvers += Resolver.sonatypeRepo("public"),


        // Enable code coverage
    coverageEnabled :=
      true
    )