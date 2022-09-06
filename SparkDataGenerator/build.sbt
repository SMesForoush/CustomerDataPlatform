
ThisBuild / scalaVersion := "2.13.8"

libraryDependencies += "redis.clients" % "jedis" % "4.2.3"
libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.32.0"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "3.2.1"
lazy val root = (project in file("."))
  .settings(
    name := "SparkDataGenerator"
  )
