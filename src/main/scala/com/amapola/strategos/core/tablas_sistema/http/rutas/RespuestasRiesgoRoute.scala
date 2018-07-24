package com.amapola.strategos.core.tablas_sistema.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.amapola.strategos.core.tablas_sistema.http.json._
import com.amapola.strategos.core.tablas_sistema.servicios.RespuestasRiesgosService
import com.amapola.strategos.utils.http.{
  FileUploadDirectives,
  StrategosCorsSettings
}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Expone los servicios http para respuestas-riesgos
  * @param repuestasRiesgosService
  * @param executionContext
  */
class RespuestasRiesgoRoute(repuestasRiesgosService: RespuestasRiesgosService)(
    implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport
    with FileUploadDirectives
    with StrategosCorsSettings {

  def getPaths: Route = {
    cors(settings) {
      pathPrefix("respuestas-riesgos") {
        traerRespuestasRiesgoPorId ~ traerImpactosRiesgo ~ crearRespuestasRiesgo ~ actualizarRespuestasRiesgo ~ borrarRespuestasRiesgo
      }
    }
  }

  private def traerRespuestasRiesgoPorId = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(repuestasRiesgosService.traerRespuestasRiesgoPorId(id)) {
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

  private def traerImpactosRiesgo = {
    pathEndOrSingleSlash {
      get {
        onComplete(repuestasRiesgosService.traerRespuestasRiesgo()) {
          case Success(result) => complete(StatusCodes.OK, result.asJson)
          case Failure(ex) =>
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }
  }

  private def crearRespuestasRiesgo = {
    pathEndOrSingleSlash {
      post {
        entity(as[RespuestasRiesgoJson]) { entity =>
          onComplete(repuestasRiesgosService.crearRespuestasRiesgo(entity)) {
            case Success(_) =>
              complete(StatusCodes.OK, "Registro creado correctamente")
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def actualizarRespuestasRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        put {
          entity(as[RespuestasRiesgoJson]) { entity =>
            onComplete(
              repuestasRiesgosService.actualizarRespuestasRiesgo(id, entity)) {
              case Success(result) =>
                if (result)
                  complete(StatusCodes.OK, "Registro actualizado correctamente")
                else
                  complete(StatusCodes.NotFound,
                           "No se encuentra el registro a actualizar")
              case Failure(ex) =>
                complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      }
    }
  }

  private def borrarRespuestasRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        delete {
          onComplete(repuestasRiesgosService.borrarRespuestasRiesgo(id)) {
            case Success(result) =>
              if (result)
                complete(StatusCodes.OK, "Registro borrado correctamente")
              else
                complete(StatusCodes.NotFound,
                         "No se encuentra el registro a borrar")
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }
}
