name := "PullRequestAssistant"

version := "1.0"

lazy val `pullrequestassistant` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.6-play26"
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

