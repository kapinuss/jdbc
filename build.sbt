name := "oracle"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= {
  val akkaV = "2.5.11"
  val akkaHttpV = "10.0.12"
  val phantomV = "2.14.5"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "org.scalikejdbc" %% "scalikejdbc"       % "3.2.2",
    "ch.qos.logback"  %  "logback-classic"   % "1.2.3",
    "org.postgresql" % "postgresql" % "42.1.4",
    "org.json4s" %% "json4s-jackson" % "3.5.3",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
  )
}

unmanagedJars in Compile += file("lib/ojdbc8.jar")
unmanagedJars in Compile += file("lib/ucp.jar")