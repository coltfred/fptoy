name := "fptoy"

version := "0.1"

organization := "com.coltfred"

scalaVersion := "2.10.0"
 
libraryDependencies += "org.scalaz" % "scalaz-core_2.10" % "7.0.0-M8"
 
scalacOptions += "-feature"
 
initialCommands in console := "import scalaz._, Scalaz._"

parallelExecution in Test := false

scalariformSettings

