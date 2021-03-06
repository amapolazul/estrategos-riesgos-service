package com.amapola.strategos.core.tablas_sistema.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.amapola.strategos.core.tablas_sistema.http.json._
import com.amapola.strategos.core.tablas_sistema.servicios.CausasRiesgosService
import com.amapola.strategos.utils.http.{FileUploadDirectives, StrategosCorsSettings}
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Expone las rutas http para el dominio de causas riesgos
  * @param causasRiesgosService
  * @param executionContext
  */
class CausasRiesgosRoute(causasRiesgosService: CausasRiesgosService)(
    implicit executionContext: ExecutionContext,
    logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with FileUploadDirectives
    with StrategosCorsSettings {

  def getPaths: Route = handleRejections(corsRejectionHandler) {
    cors(settings) {
      pathPrefix("causas-riesgos") {
        crearCausasRiesgos ~ traerCausasRiesgoPorId ~ traerCausasRiesgos ~ actualizarCausasRiesgo ~ borrarCausasRiesgo
      }
    }
  }

  private def crearCausasRiesgos: Route = {
    pathEndOrSingleSlash {
      post {
        entity(as[CausasRiesgosJson]) { entidad =>
          onComplete(causasRiesgosService.crearCausasRiesgo(entidad)) {
            case Success(_) =>
              complete(StatusCodes.Created, "Registro creado correctamente")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en crearCausasRiesgos",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def traerCausasRiesgoPorId: Route = {
    pathPrefix(IntNumber) { id =>
      get {
        onComplete(causasRiesgosService.traerCausasRiesgoPorId(id.toLong)) {
          case Success(resultado) =>
            resultado
              .map(x => {
                complete(StatusCodes.OK, x.asJson)
              })
              .getOrElse(
                complete(StatusCodes.NotFound, "Registro no encontrado")
              )
          case Failure(ex) =>
            logsAuditoriaService.error(
              s"Ha ocurrido un error en traerCausasRiesgoPorId",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }
  }

  private def traerCausasRiesgos: Route = {

    pathEndOrSingleSlash {
      get {
        onComplete(causasRiesgosService.traerCausasRiesgo()) {
          case Success(resultado) =>
            complete(StatusCodes.OK, resultado.asJson)
          case Failure(ex) =>
            logsAuditoriaService.error(
              s"Ha ocurrido un error en traerCausasRiesgos",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }

  }

  private def borrarCausasRiesgo: Route = {
    pathPrefix(IntNumber) { id =>
      pathEndOrSingleSlash {
        delete {
          onComplete(causasRiesgosService.borrarCausasRiesgo(id.toLong)) {
            case Success(resultado) =>
              if (resultado)
                complete(StatusCodes.OK, "Registro borrado correctamente")
              else complete(StatusCodes.NotFound, "Registro no encontrado")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en borrarCausasRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def actualizarCausasRiesgo: Route = {
    pathPrefix(IntNumber) { id =>
      put {
        entity(as[CausasRiesgosJson]) { entidad =>
          onComplete(
            causasRiesgosService.actualizarCausasRiesgo(id.toLong, entidad)) {
            case Success(result) =>
              if (result)
                complete(StatusCodes.OK, "Registro actualizado correctamente")
              else complete(StatusCodes.NotFound, "Registro no encontrado")
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en actualizarCausasRiesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }
}
