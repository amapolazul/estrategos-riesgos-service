package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, entity, get, onComplete, pathEndOrSingleSlash, _}
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.http.json.EjerciciosEvaluacionRiesgosJson
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.servicios.EjerciciosEvaluacionesRiesgosService
import com.amapola.strategos.utils.http.StrategosCorsSettings
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class EjerciciosEvaluacionesRiesgosRutas(
    ejerciciosRiesgosService: EjerciciosEvaluacionesRiesgosService)(
    implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport
    with StrategosCorsSettings {

  /**
    * Devuelve las rutas asociadas al dominio ejercicio estados
    * @return
    */
  def getPaths: Route = cors(settings) {
    pathPrefix("ejercicio-riesgos") {
        traerEvaluacionRiesgosPorId ~
        crearEjercicioEvaluacionRiesgo ~
        actualizarEvaluacionRiesgo ~
        borrarEvaluacionRiesgo ~
        traerEvaluacionRiesgosProcesoId
    }
  }

  /**
    * Expone la ruta para consultar los estados disponibles para los ejercicios
    * @return
    */
  private def traerEvaluacionRiesgosPorId = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(ejerciciosRiesgosService.traerEjercicioEvaluacionPorId(id)) {
            case Success(result) =>
              result
                .map(x => complete(StatusCodes.OK, x.asJson))
                .getOrElse(
                  complete(StatusCodes.NotFound, "Registro no encontrado"))
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  /**
    * Expone el servicio para crear un registro de evaluacion
    * @return
    */
  private def crearEjercicioEvaluacionRiesgo = {
    pathEndOrSingleSlash {
      post {
        entity(as[EjerciciosEvaluacionRiesgosJson]) { entity =>
          onComplete(ejerciciosRiesgosService.crearEjerciciosEvaluacion(entity)) {
            case Success(_) =>
              complete(StatusCodes.OK, "Registro creado correctamente")
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  /**
    * Expone servicio para actualizar un ejercicio de evaluacion de riesgo
    * @return
    */
  private def actualizarEvaluacionRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        put {
          entity(as[EjerciciosEvaluacionRiesgosJson]) { entity =>
            onComplete(
              ejerciciosRiesgosService
                .actualizarEjercicioEvaluacion(id, entity)) {
              case Success(result) =>
                if (result)
                  complete(StatusCodes.OK, "Registro actualizado correctamente")
                else complete(StatusCodes.NotFound, "Registro no encontrado")
              case Failure(ex) =>
                complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      }
    }
  }

  /**
    * Expone servicio para borrar una evaluacion riesgo por su id
    * @return
    */
  private def borrarEvaluacionRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        delete {
          onComplete(
            ejerciciosRiesgosService.borrarEjercicioEvaluacionPorId(id)) {
            case Success(result) =>
              if (result)
                complete(StatusCodes.OK, "Registro borrado correctamente")
              else complete(StatusCodes.NotFound, "Registro no encontrado")
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  /**
    * Expone servicio para retornar los ejercicios de evaluacion por un proceso
    * @return
    */
  private def traerEvaluacionRiesgosProcesoId = {
    pathPrefix("procesos" / LongNumber) { procesoId =>
      pathEndOrSingleSlash {
        get {
          onComplete(
            ejerciciosRiesgosService.traerEjerciciosEvaluacionPorProceso(
              procesoId)) {
            case Success(result) =>
              complete(StatusCodes.NotFound, result.asJson)
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }
}
