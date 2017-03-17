package jartomcat.testapp

import jartomcat.jarservlet.{EmbeddedJars, PersistedProvider}

/**
  * Created by pappmar on 14/03/2017.
  */
class TestingPersistedProvider extends PersistedProvider {
  override def appName: String = "testing"

  override def loadEmbeddedJars: EmbeddedJars = {
    EmbeddedJars(
      jars = Seq.empty,
      className = classOf[TestingPluggedProvider].getName
    )
  }
}
