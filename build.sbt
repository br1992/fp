ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.7.1"

lazy val root = (project in file("."))
  .settings(
    name := "fp",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.19",
      "io.higherkindness" %% "droste-core" % "0.9.0",
      "org.typelevel" %% "kittens" % "3.5.0"
    )
  )
