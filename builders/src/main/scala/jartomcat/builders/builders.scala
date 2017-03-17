package jartomcat.builders

object build_jartomcat extends mvnmod.builder.ModuleBuilder(
  jartomcat.modules.Place.RootPath,
  "."
)
           
object build_jartomcat_modules extends mvnmod.builder.ModuleBuilder(
  jartomcat.modules.Place.RootPath,
  "./modules"
)
           
object build_jartomcat_shared extends mvnmod.builder.ModuleBuilder(
  jartomcat.modules.Place.RootPath,
  "./shared"
)
           
object build_jartomcat_jarservlet extends mvnmod.builder.ModuleBuilder(
  jartomcat.modules.Place.RootPath,
  "./jarservlet"
)
           
object build_jartomcat_clients extends mvnmod.builder.ModuleBuilder(
  jartomcat.modules.Place.RootPath,
  "./clients"
)
           
object build_jartomcat_packaging extends mvnmod.builder.ModuleBuilder(
  jartomcat.modules.Place.RootPath,
  "./packaging"
)
           
object build_jartomcat_testapp extends mvnmod.builder.ModuleBuilder(
  jartomcat.modules.Place.RootPath,
  "./testapp"
)
           
object build_jartomcat_client extends mvnmod.builder.ModuleBuilder(
  jartomcat.modules.Place.RootPath,
  "./client"
)
           
object build_jartomcat_testing extends mvnmod.builder.ModuleBuilder(
  jartomcat.modules.Place.RootPath,
  "./testing"
)
           
       