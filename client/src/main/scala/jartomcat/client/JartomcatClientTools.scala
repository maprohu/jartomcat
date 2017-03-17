package jartomcat.client

import java.nio.file.Path

import akka.http.scaladsl.model.ws.{BinaryMessage, Message}
import akka.stream.scaladsl.{BidiFlow, Flow, Sink, Source}
import akka.util.ByteString
import jartomcat.shared._

/**
  * Created by pappmar on 17/03/2017.
  */
object JartomcatClientTools {

  val pickling = BidiFlow.fromFlows(
    Flow[Message]
      .map({
        case b : BinaryMessage => b
        case _ => ???
      })
      .via(BinaryMessages.toStrictFlow)
      .map({ m =>
        PicklingClient.unpickle(m.data.asByteBuffer)
      }),
    Flow[ClientToServer]
      .map({ m =>
        BinaryMessage(
          ByteString(
            PicklingClient.pickle(m)
          )
        )
      })

  )

  def clientFlow(flow: Flow[ServerToClient, ClientToServer, _]) = {
    pickling.join(flow)
  }

  def verifyAndUpload(
    jars: Seq[(JarCoords, Path)]
  ) = {
    Flow[ServerToClient]
      .prepend(
        Source.single(
          VerifyJars(jars.map(_._1))
        )
      )
  }


}
