package com.artkostm.configuration

import akka.actor.{ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import com.typesafe.config.Config

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
    def getOptString(path: String) : Option[String] = if (underlying.hasPath(path)) {
      Some(underlying.getString(path))
    } else {
      None
    }

    def getOptInt(path: String) : Option[Int] = if (underlying.hasPath(path)) {
      Some(underlying.getInt(path))
    } else {
      None
    }
  }
}


class ConfigExtensionImpl(config: Config) extends Extension {

  import Configuration.RichConfig
  val template = Template(config.getOptString("app.template.directory"))
  val netty = Netty(config.getOptString("app.netty.host"), config.getOptInt("app.netty.port"))

}

case class Template(directory: Option[String])
case class Netty(host: Option[String], port: Option[Int])
case class RouteHolder(clazz: Option[String], name: Option[String], spin: Option[Int] = Some(1))

