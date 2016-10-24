package com.artkostm.scala.test

import com.typesafe.config.ConfigFactory
import configs.Configs
import configs.Result.Success

object AkkaHttpStart extends App{

  //List("Sasha", "Masha", "Dasha", "Glasha").map { name => name.length() } foreach println

  val myConfig = ConfigFactory.parseString(
    """
      my-config {
       foo = My config
       bar = 1234
       baz = [1h, 2m, 3s]
      }

      routes {
      	GET /path1 {
      		class : example.controller.ApachePageController
      		spin: 1
      		name: first
      	}

          GET /path2 {
      		class : example.controller.IndexController
      		name: index
      		spin: 5
      	}

      	POST /path1 {
      		class : example.controller.NettyActor
      		name: postactor
      	}
      }
    """)
  import configs.syntax._
  val bars = myConfig.get[Any]("routes").fold(error => Vector.empty[Any], v => v.asInstanceOf[Vector[Any]])
  bars.foreach(x => x match {
    case Tuple2(path, data) => println(s"Path: $path, Data: $data")
    case _ => println("Fail!")
  })
//  bars match {
//    case Success(x) => println(x.getClass)
//    case _ => println("Fail!")
//  }
}
