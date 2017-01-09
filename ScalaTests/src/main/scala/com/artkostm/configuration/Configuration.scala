package com.artkostm.configuration

import akka.actor.{Actor, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import com.typesafe.config.{Config, ConfigObject}

import scala.reflect.ClassTag
import scala.util.Try

/**
  * Created by artsiom.chuiko on 24/10/2016.
  */
class Configuration {

}

object Configuration extends ExtensionId[ConfigExtensionImpl] with ExtensionIdProvider {

  override def lookup = Configuration

  override def createExtension(system: ExtendedActorSystem): ConfigExtensionImpl = new ConfigExtensionImpl(system.settings.config)

  override def get(system: ActorSystem): ConfigExtensionImpl = super.get(system)

  implicit class RichConfig(val underlying: Config) extends AnyVal {
    def getOptString(path: String) : Option[String] = if (underlying.hasPath(path)) Some(underlying.getString(path)) else None

    def getOptInt(path: String) : Option[Int] = if (underlying.hasPath(path)) Some(underlying.getInt(path)) else None
  }

  implicit class RichConfigObject(val underlying: ConfigObject) extends AnyVal {
    def getClazz(path: String) : Class[_ <: Actor] =  getClassFor[Actor](underlying.get(path).render()).get
    def getInt(path: String, default: Option[Int] = None) =
      if (underlying.containsKey(path)) Some(underlying.get(path).render().toInt)
      else default
    def getString(path: String, default: Option[String] = None) =
      if (underlying.containsKey(path)) Some(underlying.get(path).render())
      else default

    def getClassFor[T: ClassTag](fqcn: String): Try[Class[_ <: T]] =
      Try[Class[_ <: T]]({
        val c = Class.forName(fqcn, false, getClass.getClassLoader).asInstanceOf[Class[_ <: T]]
        val t = implicitly[ClassTag[T]].runtimeClass
        if (t.isAssignableFrom(c)) c else throw new ClassCastException(t + " is not assignable from " + c)
      })
  }
}

class ConfigExtensionImpl(val config: Config) extends Extension {

  import Configuration.RichConfig
  val template = Template(config.getOptString("app.template.directory"))
  val netty = Netty(config.getOptString("app.netty.host"), config.getOptInt("app.netty.port"))

  def createRoutes()(implicit system: ActorSystem): Unit = {
    println(template)
    println(netty)
    println(readRoutes())
  }

  private def readRoutes(): Vector[(String, RouteHolder)] = {
    import configs.syntax._
    import Configuration.RichConfigObject
    val routes = config.get[Any]("routes").map(a => a.asInstanceOf[Vector[(String, ConfigObject)]])
    routes.value.map(tuple => tuple match {
      case (s, c) => (s, RouteHolder(c.getClazz("class"), c.getString("name"), c.getInt("spin", Some(1))))
    })
  }
}

case class Template(directory: Option[String])
case class Netty(host: Option[String], port: Option[Int])
case class RouteHolder(clazz: Class[_ <:Actor], name: Option[String], spin: Option[Int] = Some(1))

