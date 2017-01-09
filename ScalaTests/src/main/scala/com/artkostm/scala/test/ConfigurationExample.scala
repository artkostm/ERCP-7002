package com.artkostm.scala.test

import akka.actor.{Actor, ActorSystem}
import com.artkostm.configuration.Configuration

/**
  * Created by artsiom.chuiko on 09/11/2016.
  */
class TestClass extends Actor {
  override def receive: Receive = ???
}

object ConfigurationExample extends App {
  implicit val actorSystem = ActorSystem()

  Configuration.get(actorSystem).createRoutes()
}