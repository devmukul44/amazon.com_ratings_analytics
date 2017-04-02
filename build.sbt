name := "analytics-core"

version := "0.1.0"

scalaVersion := "2.10.4"

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

//*** Resolvers
resolvers += "Job Server Bintray" at "https://dl.bintray.com/spark-jobserver/maven"

//*** Library Dependencies
// Spark Core Libraries
val sparkVersion = "1.5.2"
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion

//*** Source and Sink Library Dependencies
libraryDependencies += "com.databricks" %% "spark-csv" % "1.3.0"

//*** Spark Job Server Dependencies
libraryDependencies += "org.mongodb" % "mongodb-driver" % "3.0.4"

//Mongo Dependencies
// Scala MongoDB Library
libraryDependencies += "org.mongodb" % "mongodb-driver" % "3.0.4" excludeAll(ExclusionRule(organization = "javax.servlet", name = "javax.servlet-api"), ExclusionRule(organization = "org.mortbay.jetty", name = "jetty"), ExclusionRule(organization = "org.mortbay.jetty", name = "servlet-api-2.5"))

//*** Scala Unit Test Library
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0"

//*** Assembly Setting
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
parallelExecution in Test:= false


    