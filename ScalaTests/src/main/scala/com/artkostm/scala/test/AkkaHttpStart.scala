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
  List("Sasha", "Masha", "Dasha", "Glasha").map { name => name.length() } foreach println
}

trait Core{
  def summarize() : String;
}
abstract class DefCore extends Core
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
