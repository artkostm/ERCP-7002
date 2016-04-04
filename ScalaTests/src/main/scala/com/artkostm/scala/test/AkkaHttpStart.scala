package com.artkostm.scala.test

import akka.actor.ActorSystem

/*trait Core  {
  implicit val system: ActorSystem = ActorSystem("Suggestion")
  implicit val materializer: ActorFlowMaterializer = ActorFlowMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher
}*/

object AkkaHttpStart extends App{
  /*val http = new VanilaHttp with Core{
    override def config: Config = ConfigFactory.load()
  }

  http.start() */
}
/*
trait AkkaHttp {
  this: Core =>

  def config: Config

  def start() = {
    val route: Route = {
      path("dictionaries" / Segment / "suggestions"){ dictionaryId =>
        get{
          parameters("ngr"){ ngr =>
              complete("response")
          }
        }
      }
    }

    Http().bind(
      interface = config.getString("frontend.interface"),
      port = config.getInt("frontend.port")
    ).to(Sink.foreach { conn =>
      conn.flow.join(route).run()
    }).run()
  }
} */