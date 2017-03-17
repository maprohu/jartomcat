package jartomcat.testing

import java.nio.file.Paths

import jartomcat.client.JartomcatClientTools
import jartomcat.shared.JarCoords

import scala.collection.immutable._

/**
  * Created by pappmar on 17/03/2017.
  */
object RunJartomcatUpload {

  def main(args: Array[String]): Unit = {
    JartomcatClientTools.connect(
      JartomcatClientTools.verifyAndUpload(
        Seq(
          (
            JarCoords("jartomcat", "clients", "2-SNAPSHOT"),
            Paths.get("../jartomcat/clients/target/product.jar")
          )
        )
      )
    )

  }

}
