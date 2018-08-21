package com.amapola.strategos.core.tablas_sistema.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{as, complete, delete, entity, get, onComplete, pathEndOrSingleSlash, pathPrefix, post, put, _}
import akka.http.scaladsl.server.PathMatchers.LongNumber
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.tablas_sistema.http.json.CalificacionRiesgosJson
import com.amapola.strategos.core.tablas_sistema.servicios.CalificacionRiesgosService
import com.amapola.strategos.utils.http.{FileUploadDirectives, StrategosCorsSettings}
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Expone las rutas http para el dominio calificacion-riesgo
  * @param calificacionRiesgosService
  * @param executionContext
  */
class CalificacionRiesgosRoute(
    calificacionRiesgosService: CalificacionRiesgosService)(
    implicit executionContext: ExecutionContext,
    logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with FileUploadDirectives
    with StrategosCorsSettings {

  def getPaths: Route = cors(settings) {
    pathPrefix("calificacion-riesgo") {
      traerCalificacionRiesgoPorId ~
        traerCalificacionRiesgo ~
        crearCalificacionRiesgo ~
        actualizarCalificacionRiesgo ~
        borrarCalificacionRiesgo
    }
  }

  private def traerCalificacionRiesgoPorId = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(
            calificacionRiesgosService.traerCalificacionRiesgoPorId(id)) {
            case Success(result) =>
              result
                .map(x => {
                  complete(StatusCodes.OK, x.asJson)
                })
                .getOrElse(
                  complete(StatusCodes.NotFound, "Registro no encontrado"))
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en traerCalificacionRiesgoPorId",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def traerCalificacionRiesgo = {
    pathEndOrSingleSlash {
      get {
        onComplete(calificacionRiesgosService.traerCalificacionRiesgo()) {
          case Success(result) => complete(StatusCodes.OK, result.asJson)
          case Failure(ex) =>
            logsAuditoriaService.error(
              s"Ha ocurrido un error en traerCalificacionRiesgo",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }
  }

  private def crearCalificacionRiesgo = {
    pathEndOrSingleSlash {
      post {
        entity(as[CalificacionRiesgosJson]) { entity =>
          onComplete(calificacionRiesgosService.crearCalificacionRiesgo(entity)) {
            case Success(_) =>
              complete(StatusCodes.OK, "Registro creado correctamente")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en crearCalificacionRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def actualizarCalificacionRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        put {
          entity(as[CalificacionRiesgosJson]) { entity =>
            onComplete(
              calificacionRiesgosService
                .actualizarCalificacionRiesgo(id, entity)) {
              case Success(result) =>
                if (result)
                  complete(StatusCodes.OK, "Registro actualizado correctamente")
                else complete(StatusCodes.NotFound, "Registro no encontrado")
              case Failure(ex) =>
                logsAuditoriaService.error(
                  s"Ha ocurrido un error en actualizarCalificacionRiesgo",
                  this.getClass.toString,
                  ex)
                complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      }
    }
  }

  private def borrarCalificacionRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        delete {
          onComplete(calificacionRiesgosService.borrarCalificacionRiesgo(id)) {
            case Success(result) =>
              if (result)
                complete(StatusCodes.OK, "Registro borrado correctamente")
              else complete(StatusCodes.NotFound, "Registro no encontrado")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en borrarCalificacionRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }
}
