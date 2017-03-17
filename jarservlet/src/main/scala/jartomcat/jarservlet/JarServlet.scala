package jartomcat.jarservlet

import javax.servlet.ServletContext
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.StrictLogging
import jartomcat.jarservlet.JarServlet.Plugged
import monix.execution.atomic.AtomicAny


class JarServlet(
  service: AtomicAny[Plugged]
) extends HttpServlet with StrictLogging {

  override def service(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    service.get.handler(req, resp)
  }
}

object JarServlet {

  case class Plugged(
    handler: Handler = { (_, _) => },
    control: Any = (),
    stop: () => Unit = () => ()
  )

  val DefaultPlugged = Plugged()

  type Handler = (HttpServletRequest, HttpServletResponse) => Unit

}

trait JarServletProvider {
  def apply(servletContext: ServletContext, global: Global) : Plugged
}
