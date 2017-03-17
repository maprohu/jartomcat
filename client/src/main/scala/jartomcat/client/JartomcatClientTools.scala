package jartomcat.client

import java.nio.file.Path
import java.security.MessageDigest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, WebSocketRequest}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{BidiFlow, Concat, FileIO, Flow, Sink, Source}
import akka.util.ByteString
import com.typesafe.scalalogging.StrictLogging
import jartomcat.shared._

import scala.collection.immutable._

/**
  * Created by pappmar on 17/03/2017.
  */
object JartomcatClientTools extends StrictLogging {

  def pickling(implicit
    materializer: ActorMaterializer
  ) = BidiFlow.fromFlows(
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

  def clientFlow(flow: Flow[ServerToClient, ClientToServer, _])(implicit
    materializer: ActorMaterializer
  ) = {
    pickling.join(flow)
  }

  val StopFlow = Flow.fromSinkAndSource(Sink.ignore, Source.maybe)

  def verifyAndUpload(
    jars: Seq[(JarCoords, Path)],
    andThen: Flow[ServerToClient, ClientToServer, _] = StopFlow
  ) = {
    val jarSeq = jars.map(_._1)
    val pathMap = jars.toMap

    Flow[ServerToClient]
      .prefixAndTail(1)
      .flatMapConcat({
        case (Seq(v : JarsVerified), source) =>
          logger.info(s"verified: ${v}")

          jarSeq
            .foldLeft(source)({ (acc, item) =>
              acc
                .prefixAndTail(1)
                .flatMapConcat({
                  case (Seq(JarUploaded(`item`, true)), s2) =>
                    logger.info(s"uploaded: ${item}")
                    s2
                })
            })
            .via(andThen)
            .prepend(
              Source(
                v
                  .missing
                  .to[Iterable]
                  .map({ coords =>
                    Source
                      .combine(
                        Source
                          .single(UploadJarStart(coords)),
                        FileIO
                          .fromPath(pathMap(coords))
                          .map(bs => UploadJarPiece(bs.toArray))
                          .concat(
                            Source
                              .single(UploadJarEnd(Array()))
                          )
                          .statefulMapConcat({ () =>
                            val hash = MessageDigest.getInstance("MD5")

                            {
                              case p : UploadJarPiece =>
                                hash.digest(p.data)
                                Iterable(p)
                              case e : UploadJarEnd =>
                                Iterable(
                                  e.copy(md5 = hash.digest())
                                )
                              case _ => ???
                            }
                          })
                      )(Concat.apply)
                  })
              ).flatMapConcat(identity)
            )
      })
      .prepend(
        Source.single(
          VerifyJars(jarSeq)
        )
      )
  }

  def connect(
    wsFlow : Flow[ServerToClient, ClientToServer, _],
    host: String = "localhost",
    port: Int = 8080
  ) = {

    implicit val actorSystem = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import actorSystem.dispatcher

    val flow =
      clientFlow(wsFlow)

    val (response, _) =
      Http()
        .singleWebSocketRequest(
          WebSocketRequest(s"ws://${host}:${port}/private/ws"),
          flow
        )

    response.onComplete(println)

  }


}
