import sbtrelease.Version

name := "beat-gripper"

version := "1.0"

scalaVersion := "2.11.8"

releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }

scalacOptions := Seq("-language:_", "-deprecation", "-unchecked", "-feature", "-Xlint")

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
  "org.seleniumhq.selenium" % "selenium-java" % "2.53.0",
  "org.seleniumhq.selenium" % "selenium-htmlunit-driver" % "2.52.0",
  "org.skinny-framework" %% "skinny-json" % "2.2.0"
)

assemblyJarName in assembly := "beat-gripper.jar"
