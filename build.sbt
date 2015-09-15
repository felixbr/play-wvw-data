import com.github.play2war.plugin._

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.1"

Play2WarKeys.targetName := Some("app")

name := """play-wvw-data"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,

  "org.reactivecouchbase" %% "reactivecouchbase-core" % "0.3",
  "pl.matisoft" %% "swagger-play24" % "1.4"
)

resolvers ++= Seq(
  "Local Maven"                at Path.userHome.asFile.toURI.toURL + ".m2/repository",
  "scalaz-bintray"             at "http://dl.bintray.com/scalaz/releases",
  "ReactiveCouchbase Releases" at "https://raw.github.com/ReactiveCouchbase/repository/master/releases/"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

scalacOptions in Test += "-Dconfig.file=conf/application.test.conf"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8", // yes, this is 2 args
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  //  "-Ywarn-value-discard",
  "-Xfuture"
)