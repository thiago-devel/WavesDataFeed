logLevel := Level.Warn
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2+")
libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0")

