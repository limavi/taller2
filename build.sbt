name := """play-angular-standalone"""

version := "0.8.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  "com.pauldijou" %% "jwt-play" % "0.8.0",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.play" %% "play-slick" % "1.1.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0",
  // WebJars (i.e. client-side) dependencies
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "jquery" % "1.11.3",
  "org.webjars" % "bootstrap" % "3.3.6" exclude("org.webjars", "jquery"),
  "org.webjars" % "angularjs" % "1.5.5" exclude("org.webjars", "jquery"),
  "org.webjars" % "angular-ui-bootstrap" % "1.3.2" exclude("org.webjars", "jquery"),
  "com.nimbusds" % "nimbus-jose-jwt" % "3.1"
)
