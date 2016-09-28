name := "fptoy"

version := "0.1"

organization := "com.coltfred"

scalaVersion := "2.11.8"
 
libraryDependencies += "org.typelevel" %% "cats" % "0.7.2"
libraryDependencies += "com.ironcorelabs" %% "cats-scalatest" % "1.4.0" % "test"

    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8", // yes, this is 2 args
      "-feature",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Xfuture",
      "-language:higherKinds"
  ) 
 
initialCommands in console := "import cats._, cats.data._ cats.implicits._"

com.typesafe.sbt.SbtScalariform.scalariformSettings
