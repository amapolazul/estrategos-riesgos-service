package com.amapola.strategos.core.tablas_sistema.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.amapola.strategos.core.tablas_sistema.http.json._
import com.amapola.strategos.core.tablas_sistema.servicios.ImpactoRiesgosService
import com.amapola.strategos.utils.http.FileUploadDirectives
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ImpactoRiesgosRoute(impactoRiesgosService: ImpactoRiesgosService)(
    implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport
    with FileUploadDirectives {

  def getRoutes() = {
    cors() {
      traerImpactoRiesgosPorId
    }
  }

  private def traerImpactoRiesgosPorId = {
    pathPrefix("impacto-riesgos" / LongNumber) { id =>
      get {
        onComplete(impactoRiesgosService.traerImpactoRiesgoPorId(id)) {
          case Success(result) =>
            result
              .map({ x =>
                complete(StatusCodes.OK, x.asJson)
              })
              .getOrElse(
                complete(StatusCodes.NotFound, "Registro no encontrado"))
          case Failure(ex) =>
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }
  }
}
