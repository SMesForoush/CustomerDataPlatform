val cassandraVersion = "4.5.0"
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """play-scala-hello-world-tutorial""",
    organization := "com.example",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
      "com.github.jwt-scala" %% "jwt-play" % "9.1.0",
      "com.github.jwt-scala" %% "jwt-play-json" % "9.1.0",
      "com.datastax.oss" % "java-driver-core" % cassandraVersion,
      "com.datastax.oss" % "java-driver-mapper-runtime" % cassandraVersion,
      "com.datastax.oss" % "java-driver-query-builder" % cassandraVersion,
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )
