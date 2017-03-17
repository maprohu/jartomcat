package jartomcat.testapp

import javax.servlet.ServletContext

import jartomcat.jarservlet.JarServlet.Plugged
import jartomcat.jarservlet.{Global, JarServletProvider}

/**
  * Created by pappmar on 14/03/2017.
  */
class TestingPluggedProvider extends JarServletProvider {
  override def apply(servletContext: ServletContext, global: Global): Plugged = {
    Plugged(
      handler = { (req, res) =>
        val out = res.getWriter
        out.write("plugged")
        out.flush()
      }
    )
  }
}
