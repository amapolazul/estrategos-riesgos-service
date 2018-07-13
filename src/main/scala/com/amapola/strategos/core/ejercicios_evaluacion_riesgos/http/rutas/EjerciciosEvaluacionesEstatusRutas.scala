package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{
  complete,
  get,
  onComplete,
  pathEndOrSingleSlash,
  _
}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.servicios.EjerciciosEvaluacionesEstatusService
import com.amapola.strategos.utils.http.StrategosCorsSettings
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class EjerciciosEvaluacionesEstatusRutas(
    ejerciciosStatusService: EjerciciosEvaluacionesEstatusService)(
    implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport
    with StrategosCorsSettings {

  /**
    * Devuelve las rutas asociadas al dominio ejercicio estados
    * @return
    */
  def getPaths = cors(settings) {
    pathPrefix("ejercicio-estados") {
      traerEvaluacionEstados
    }
  }

  /**
    * Expone la ruta para consultar los estados disponibles para los ejercicios
    * @return
    */
  private def traerEvaluacionEstados = pathEndOrSingleSlash {
    get {
      onComplete(ejerciciosStatusService.traerEjerciciosEstatus()) {
        case Success(result) => complete(StatusCodes.OK, result.asJson)
        case Failure(ex) =>
          complete(StatusCodes.InternalServerError, ex.getMessage)
      }
    }
  }
}
