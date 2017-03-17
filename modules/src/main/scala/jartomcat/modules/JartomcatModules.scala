package jartomcat.modules

import mvnmod.builder.{MavenCoordinatesImpl, RootModuleContainer, Scala212Module, ScalaModule}


/**
  * Created by martonpapp on 29/08/16.
  */
object JartomcatModules {

  implicit val Root = RootModuleContainer("jartomcat")

  object Modules extends Scala212Module(
    "modules",
    MavenCoordinatesImpl("mvnmod", "mvnmod-modules_2.12", "2-SNAPSHOT")
  )

  object Packaging extends Scala212Module(
    "packaging",
    Modules,
    mvn.`com.lihaoyi:ammonite-ops_2.12:jar:0.8.2`
  )

  object Shared extends Scala212Module(
    "shared",
    mvn.`com.typesafe.akka:akka-http_2.12:jar:10.0.4`,
    mvn.`io.suzaku:boopickle_2.12:jar:1.2.6`
  )

  object JarServlet extends Scala212Module(
    "jarservlet",
    Shared,
    mvn.`io.monix:monix-eval_2.12:jar:2.2.3`,
    mvn.`javax.servlet:javax.servlet-api:jar:3.1.0`,
    mvn.`javax.websocket:javax.websocket-api:jar:1.1`,
    mvn.`com.typesafe.scala-logging:scala-logging_2.12:jar:3.5.0`,
    mvn.`ch.qos.logback:logback-classic:jar:1.1.7`
  )

  object TestApp extends Scala212Module(
    "testapp",
    JarServlet
  )

  object Clients extends Scala212Module(
    "clients",
    JarServlet,
    mvn.`org.scala-lang:scala-compiler:jar:2.12.1`
  )

  object Client extends Scala212Module(
    "client",
    JarServlet
  )

  object Testing extends Scala212Module(
    "testing",
    JarServlet,
    TestApp,
    Client,
    Clients,
    mvn.`org.apache.tomcat.embed:tomcat-embed-core:jar:8.5.11`,
    mvn.`org.apache.tomcat.embed:tomcat-embed-jasper:jar:8.5.11`,
    mvn.`org.apache.tomcat.embed:tomcat-embed-websocket:jar:8.5.11`
  )


}
