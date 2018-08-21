package com.amapola.strategos.core.responsables.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.responsables.servicios.ResponsablesService
import com.amapola.strategos.utils.http.FileUploadDirectives
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
import io.circe.generic.auto._
import io.circe.syntax._

class ResponsablesRutas(responsablesService: ResponsablesService)(
    implicit executionContext: ExecutionContext,
    logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with FileUploadDirectives {

  /**
    * Devuelve el conjunto de rutas para el dominio de responsables
    * @return
    */
  def getPaths = {
    path("responsable") {
      cors() {
        traerResponsables
      }
    }
  }

  /**
    * Devuelve
    * @return
    */
  def traerResponsables = {
    pathEndOrSingleSlash {
      get {
        onComplete(responsablesService.traerListResponsables()) {
          case Success(result) => complete(StatusCodes.OK, result.asJson)
          case Failure(ex) =>
            logsAuditoriaService.error(
              s"Ha ocurrido un error en traerResponsables",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }
  }
}
