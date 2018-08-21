package com.amapola.strategos.core.tablas_sistema.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.amapola.strategos.core.tablas_sistema.http.json._
import com.amapola.strategos.core.tablas_sistema.servicios.EfectividadRiesgosService
import com.amapola.strategos.utils.http.{FileUploadDirectives, StrategosCorsSettings}
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Expone servicios http para la entidad efectividad_riesgos
  * @param efectividadRiesgosService
  * @param executionContext
  */
class EfectividadRiesgosRoute(efectividadRiesgosService: EfectividadRiesgosService)(
  implicit executionContext: ExecutionContext,
  logsAuditoriaService: LogsAuditoriaService)
  extends FailFastCirceSupport
    with FileUploadDirectives
    with StrategosCorsSettings {

  def getPaths: Route = {
    cors(settings) {
      pathPrefix("efectividad-riesgos") {
        traerEfectividadRiesgoPorId ~ traerImpactosRiesgo ~ crearEfectividadRiesgo ~ actualizarEfectividadRiesgo ~ borrarEfectividadRiesgo
      }
    }
  }

  private def traerEfectividadRiesgoPorId = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(efectividadRiesgosService.traerEfectividadRiesgoPorId(id)) {
            case Success(result) =>
              result
                .map({ x =>
                  complete(StatusCodes.OK, x.asJson)
                })
                .getOrElse(
                  complete(StatusCodes.NotFound, "Registro no encontrado"))
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en traerEfectividadRiesgoPorId",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def traerImpactosRiesgo = {
    pathEndOrSingleSlash {
      get {
        onComplete(efectividadRiesgosService.traerEfectividadRiesgo()) {
          case Success(result) => complete(StatusCodes.OK, result.asJson)
          case Failure(ex) =>
            logsAuditoriaService.error(
              s"Ha ocurrido un error en traerImpactosRiesgo",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }
  }

  private def crearEfectividadRiesgo = {
    pathEndOrSingleSlash {
      post {
        entity(as[EfectividadRiesgosJson]) { entity =>
          onComplete(efectividadRiesgosService.crearEfectividadRiesgo(entity)) {
            case Success(_) =>
              complete(StatusCodes.OK, "Registro creado correctamente")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en crearEfectividadRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def actualizarEfectividadRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        put {
          entity(as[EfectividadRiesgosJson]) { entity =>
            onComplete(
              efectividadRiesgosService.actualizarEfectividadRiesgo(id, entity)) {
              case Success(result) =>
                if (result)
                  complete(StatusCodes.OK, "Registro actualizado correctamente")
                else
                  complete(StatusCodes.NotFound,
                    "No se encuentra el registro a actualizar")
              case Failure(ex) =>
                logsAuditoriaService.error(
                  s"Ha ocurrido un error en actualizarEfectividadRiesgo",
                  this.getClass.toString,
                  ex)
                complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      }
    }
  }

  private def borrarEfectividadRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        delete {
          onComplete(efectividadRiesgosService.borrarEfectividadRiesgo(id)) {
            case Success(result) =>
              if (result)
                complete(StatusCodes.OK, "Registro borrado correctamente")
              else
                complete(StatusCodes.NotFound,
                  "No se encuentra el registro a borrar")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en borrarEfectividadRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }
}
