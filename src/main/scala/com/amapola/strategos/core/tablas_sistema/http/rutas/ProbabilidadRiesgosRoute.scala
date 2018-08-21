package com.amapola.strategos.core.tablas_sistema.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.amapola.strategos.core.tablas_sistema.http.json._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.tablas_sistema.servicios.ProbabilidadRiesgoService
import com.amapola.strategos.utils.http.{FileUploadDirectives, StrategosCorsSettings}
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Expone los servicios http para el dominio probabilidad-riesgo
  * @param probabilidadRiesgoService
  * @param executionContext
  */
class ProbabilidadRiesgosRoute(
    probabilidadRiesgoService: ProbabilidadRiesgoService)(
    implicit executionContext: ExecutionContext,
    logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with FileUploadDirectives
    with StrategosCorsSettings {

  def getPaths: Route = cors(settings) {
    pathPrefix("probabilidad-riesgo") {
      traerProbabilidadRiesgoPorId ~
        traerProbabilidadesRiesgo ~
        crearProbabilidadRiesgo ~
        actualizarProbabilidadRiesgo ~
        borrarProbabilidadRiesgo
    }
  }

  private def traerProbabilidadRiesgoPorId = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(
            probabilidadRiesgoService.traerProbabilidadRiesgosPorId(id)) {
            case Success(result) =>
              result
                .map(x => {
                  complete(StatusCodes.OK, x.asJson)
                })
                .getOrElse(
                  complete(StatusCodes.NotFound, "Registro no encontrado"))
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en traerProbabilidadRiesgoPorId",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def traerProbabilidadesRiesgo = {
    pathEndOrSingleSlash {
      get {
        onComplete(probabilidadRiesgoService.traerProbabilidadRiesgos()) {
          case Success(result) => complete(StatusCodes.OK, result.asJson)
          case Failure(ex) =>
            logsAuditoriaService.error(
              s"Ha ocurrido un error en traerProbabilidadesRiesgo",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }
  }

  private def crearProbabilidadRiesgo = {
    pathEndOrSingleSlash {
      post {
        entity(as[ProbabilidadRiesgosJson]) { entity =>
          onComplete(probabilidadRiesgoService.crearProbabilidadRiesgos(entity)) {
            case Success(_) =>
              complete(StatusCodes.OK, "Registro creado correctamente")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en crearProbabilidadRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def actualizarProbabilidadRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        put {
          entity(as[ProbabilidadRiesgosJson]) { entity =>
            onComplete(
              probabilidadRiesgoService
                .actualizarProbabilidadRiesgos(id, entity)) {
              case Success(result) =>
                if (result)
                  complete(StatusCodes.OK, "Registro actualizado correctamente")
                else complete(StatusCodes.NotFound, "Registro no encontrado")
              case Failure(ex) =>
                logsAuditoriaService.error(
                  s"Ha ocurrido un error en actualizarProbabilidadRiesgo",
                  this.getClass.toString,
                  ex)
                complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      }
    }
  }

  private def borrarProbabilidadRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        delete {
          onComplete(probabilidadRiesgoService.borrarProbabilidadRiesgos(id)) {
            case Success(result) =>
              if (result)
                complete(StatusCodes.OK, "Registro borrado correctamente")
              else complete(StatusCodes.NotFound, "Registro no encontrado")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en borrarProbabilidadRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }
}
