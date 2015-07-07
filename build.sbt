name := "Scala.js Test"

scalaVersion := "2.11.7"

// libraryDependencies += "de.sciss" %% "scalacollider" % "1.17.3"

libraryDependencies ++= Seq(
  "be.doeraene"   %%% "scalajs-jquery"  % "0.8.0",
  "org.scala-js"  %%% "scalajs-dom"     % "0.8.1"
)

enablePlugins(ScalaJSPlugin)
