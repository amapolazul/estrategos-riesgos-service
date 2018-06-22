package com.amapola.strategos.utils.http

import akka.http.scaladsl.model.HttpMethods._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings

import scala.collection.immutable

trait StrategosCorsSettings {

  val settings = CorsSettings.defaultSettings.copy(allowedMethods = immutable.Seq(
    GET, PUT, POST, HEAD, OPTIONS, DELETE
  ))

}
