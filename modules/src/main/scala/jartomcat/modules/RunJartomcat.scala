package jartomcat.modules

import java.io.File

import mvnmod.builder.Module.ConfiguredModule
import mvnmod.builder.{Module, PlacedRoot}

import scala.collection.immutable._

/**
  * Created by pappmar on 29/08/2016.
  */
object Place {
  val RootPath = Seq("..", "jartomcat")
}
object RunJartomcat {

  val RootDir = new File(Place.RootPath.mkString("/"))

  val Roots = Seq[PlacedRoot](
    JartomcatModules.Root -> RootDir
  )

  val Modules = Seq[ConfiguredModule](
    JartomcatModules.Modules,
    JartomcatModules.Shared,
    JartomcatModules.JarServlet,
    JartomcatModules.Clients,
    JartomcatModules.Packaging,
    JartomcatModules.TestApp,
    JartomcatModules.Client,
    JartomcatModules.Testing
  )

  def main(args: Array[String]): Unit = {

    Module.generate(
      Roots,
      Modules
    )

  }

}
