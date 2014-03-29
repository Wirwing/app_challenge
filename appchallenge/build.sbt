name := "appchallenge"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.github.nscala-time" %% "nscala-time" % "0.8.0",
  "postgresql" % "postgresql" % "8.4-702.jdbc4"
)     

play.Project.playScalaSettings