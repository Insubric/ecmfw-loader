name := "ecmwf-loader"

version := "1.02"

organization := "ch.wsl"

scalaVersion := "2.11.8"

resolvers ++= Seq(  "java.net Maven2 Repository" at "http://download.java.net/maven/2/",
                    "SourceForge" at "http://csvjdbc.sourceforge.net/maven2"
)

libraryDependencies ++= Seq("org.rogach" %% "scallop" % "2.0.1")


exportJars := true    //in order to create MANIFEST.MF


//assembly configuration

mainClass in assembly := Some("ch.wsl.data.app.ConsoleApp")

test in assembly := {}   //skip tests during assembly


scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
