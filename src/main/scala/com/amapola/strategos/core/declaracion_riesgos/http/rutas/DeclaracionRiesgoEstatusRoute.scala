package com.amapola.strategos.core.declaracion_riesgos.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, get, onComplete, pathEndOrSingleSlash, pathPrefix}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.declaracion_riesgos.servicios.DeclaracionRiesgosEstatusService
import com.amapola.strategos.utils.http.StrategosCorsSettings
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class DeclaracionRiesgoEstatusRoute(
    declaracionEstatusService: DeclaracionRiesgosEstatusService)(
    implicit executionContext: ExecutionContext,
    logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with StrategosCorsSettings {

  /**
    * Devuelve las rutas asociadas al dominio ejercicio estados
    * @return
    */
  def getPaths = cors(settings) {
    pathPrefix("declaracion-estados") {
      traerDeclaracionEstados
    }
  }

  /**
    * Expone la ruta para consultar los estados disponibles para los ejercicios
    * @return
    */
  private def traerDeclaracionEstados = pathEndOrSingleSlash {
    get {
      onComplete(declaracionEstatusService.traerDeclaracionRiesgosEstatus()) {
        case Success(result) => complete(StatusCodes.OK, result.asJson)
        case Failure(ex) =>
          logsAuditoriaService.error("Ha ocurrido un error en traerDeclaracionEstados",this.getClass.toString, ex)
          complete(StatusCodes.InternalServerError, ex.getMessage)
      }
    }
  }
}
