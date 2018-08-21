package com.amapola.strategos.core.tablas_sistema.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{entity, _}
import com.amapola.strategos.core.tablas_sistema.http.json._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.tablas_sistema.servicios.TipoRiesgosService
import com.amapola.strategos.utils.http.{FileUploadDirectives, StrategosCorsSettings}
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Expone los servicios http para el dominio tipo-riesgo
  * @param tipoRiesgosServie
  * @param executionContext
  */
class TiposRiesgosRoute(tipoRiesgosServie: TipoRiesgosService)(
    implicit executionContext: ExecutionContext,
    logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with FileUploadDirectives
    with StrategosCorsSettings {

  def getPaths: Route = cors(settings) {
    pathPrefix("tipo-riesgo") {
      traerTipoRiesgoPorId ~
        traerTipoRiesgo ~
        crearTipoRiesgo ~
        actualizarTipoRiesgo ~
        borrarTipoRiesgo
    }
  }

  private def traerTipoRiesgoPorId = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(tipoRiesgosServie.traerTipoRiesgosPorId(id)) {
            case Success(result) =>
              result
                .map(x => {
                  complete(StatusCodes.OK, x.asJson)
                })
                .getOrElse(
                  complete(StatusCodes.NotFound, "Registro no encontrado"))
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en traerTipoRiesgoPorId",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def traerTipoRiesgo = {
    pathEndOrSingleSlash {
      get {
        onComplete(tipoRiesgosServie.traerTipoRiesgos()) {
          case Success(result) => complete(StatusCodes.OK, result.asJson)
          case Failure(ex) =>
            logsAuditoriaService.error(
              s"Ha ocurrido un error en traerTipoRiesgo",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }
  }

  private def crearTipoRiesgo = {
    pathEndOrSingleSlash {
      post {
        entity(as[TipoRiesgosJson]) { entity =>
          onComplete(tipoRiesgosServie.crearTipoRiesgos(entity)) {
            case Success(_) =>
              complete(StatusCodes.OK, "Registro creado correctamente")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en crearTipoRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def actualizarTipoRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        put {
          entity(as[TipoRiesgosJson]) { entity =>
            onComplete(
              tipoRiesgosServie
                .actualizarTipoRiesgos(id, entity)) {
              case Success(result) =>
                if (result)
                  complete(StatusCodes.OK, "Registro actualizado correctamente")
                else complete(StatusCodes.NotFound, "Registro no encontrado")
              case Failure(ex) =>
                logsAuditoriaService.error(
                  s"Ha ocurrido un error en actualizarTipoRiesgo",
                  this.getClass.toString,
                  ex)
                complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      }
    }
  }

  private def borrarTipoRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        delete {
          onComplete(tipoRiesgosServie.borrarTipoRiesgos(id)) {
            case Success(result) =>
              if (result)
                complete(StatusCodes.OK, "Registro borrado correctamente")
              else complete(StatusCodes.NotFound, "Registro no encontrado")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en borrarTipoRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }
}
