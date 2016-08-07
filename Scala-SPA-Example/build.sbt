import sbt.Keys._


name := "uBlog"

scalaVersion in ThisBuild := "2.11.8"

lazy val root = project.in(file(".")).
  aggregate(uBlogJS, uBlogJVM).
  settings()

lazy val uBlog = crossProject.settings(
  version := "0.1-SNAPSHOT",
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle" % "0.4.1",
    "com.lihaoyi" %%% "autowire" % "0.2.5",
    "com.lihaoyi" %%% "scalatags" % "0.6.0"
  )
).jsSettings(
  name := "uBlogClient",
  persistLauncher in Compile := true,
  persistLauncher in Test := false,
  skip in packageJSDependencies := false,
  jsDependencies ++= Seq(
    "org.webjars.bower" % "react" % "15.2.1"
      /        "react-with-addons.js"
      minified "react-with-addons.min.js"
      commonJSName "React",

    "org.webjars.bower" % "react" % "15.2.1"
      /         "react-dom.js"
      minified  "react-dom.min.js"
      dependsOn "react-with-addons.js"
      commonJSName "ReactDOM",

    "org.webjars" % "bootstrap" % "3.3.6"
      /         "bootstrap.js"
      minified  "bootstrap.min.js",

    "org.webjars" % "jquery" % "2.1.4"
      /         "jquery.js"
      minified  "jquery.min.js"
  ),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    "org.scala-js" %%% "scalajs-java-time" % "0.2.0",
    "com.github.japgolly.scalajs-react" %%% "core" % "0.11.1",
    "com.github.japgolly.scalajs-react" %%% "extra" % "0.11.1",
    "com.github.japgolly.scalacss" %%% "core" % "0.4.1",
    "com.github.japgolly.scalacss" %%% "ext-react" % "0.4.1"
  )
).jvmSettings(
  Revolver.settings:_*
).jvmSettings(
  name := "uBlogServer",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.4.8",
    "com.typesafe.akka" %% "akka-http-core" % "2.4.8",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.4.8",
    "com.typesafe.akka" %% "akka-slf4j" % "2.4.8",
    "com.typesafe.akka" %% "akka-actor" % "2.4.8",
    "org.webjars" % "bootstrap" % "3.3.7",
    "org.webjars" % "font-awesome" % "4.5.0",
    "org.webjars" % "webjars-locator" % "0.31"
  )
)

lazy val uBlogJS = uBlog.js
lazy val uBlogJVM = uBlog.jvm.settings(
  (resources in Compile) ++= {
    Seq(
        (fastOptJS in (uBlogJS, Compile)).value.data
      , (packageScalaJSLauncher in(uBlogJS, Compile)).value.data
      , (packageJSDependencies in(uBlogJS, Compile)).value
      , (packageMinifiedJSDependencies in(uBlogJS, Compile)).value
    ) ++
    Seq(file((fastOptJS in (uBlogJS, Compile)).value.data + ".map"))
  }
)

addCommandAlias("go", s"~ ;uBlogJVM/re-start ; uBlogJS/fastOptJS")