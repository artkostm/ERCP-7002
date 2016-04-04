package com.artkostm.scala.test


import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.Timeout
import akka.actor.Actor
import spray.routing.HttpService

class MyServiceActor extends Actor with MyService {
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}
trait MyService extends HttpService {

  val myRoute =
    path("dictionaries" / Segment / "suggestions"){ dictionaryId =>
      get{
        parameters("ngr"){ ngr =>
          complete("response")
        }
      }
    }
}

object Main extends App {
  implicit val system = ActorSystem("on-spray-can")
  val service = system.actorOf(Props[MyServiceActor], "demo-service")

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}