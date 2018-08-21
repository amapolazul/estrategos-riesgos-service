package com.amapola.strategos.core.declaracion_riesgos.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{
  complete,
  get,
  onComplete,
  pathEndOrSingleSlash,
  _
}
import akka.http.scaladsl.server.PathMatchers.LongNumber
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.declaracion_riesgos.http.json.CausasDeclaracionRiesgosJson
import com.amapola.strategos.core.declaracion_riesgos.servicios.CausasDeclaracionService
import com.amapola.strategos.utils.http.StrategosCorsSettings
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class CausasDeclaracionRoute(causasDeclaracionService: CausasDeclaracionService)(
    implicit executionContext: ExecutionContext,
    logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with StrategosCorsSettings {

  /**
    * Devuelve las rutas asociadas al dominio ejercicio estados
    * @return
    */
  def getPaths = cors(settings) {
    pathPrefix("causas-riesgos") {
      actualizarCausaRiesgoPorId()
    }
  }

  /**
    * Actualiza la causa de un riesgo por su identificador con los datos enviados en la peticion
    * @return
    */
  private def actualizarCausaRiesgoPorId() = pathPrefix(LongNumber) { causaId =>
    pathEndOrSingleSlash {
      put {
        entity(as[CausasDeclaracionRiesgosJson]) { entity =>
          onComplete(
            causasDeclaracionService.actualizarCausaDeclaracion(causaId,
                                                                entity)) {
            case Success(result) =>
              if (result)
                complete(StatusCodes.OK, "Registro actualizado correctamente")
              else complete(StatusCodes.NotFound, "Registro no encontrado")
            case Failure(ex) =>
              logsAuditoriaService.error("Ha ocurrido un error en actualizarCausaRiesgoPorId()", this.getClass.toString, ex)
              complete(StatusCodes.InternalServerError,
                       s"Ha ocurrido un error interno ${ex.getMessage}")
          }
        }
      }
    }
  }

  /**
    * Borra la causa de un riesgo por si Id
    * @return
    */
  private def borrarCausaPorId() = pathPrefix(LongNumber) { causaId =>
    pathEndOrSingleSlash {
      delete {
        onComplete(causasDeclaracionService.borrarCausaDeclaracion(causaId)) {
          case Success(result) =>
            if (result)
              complete(StatusCodes.OK, "Registro borrado correctamente")
            else complete(StatusCodes.NotFound, "Registro no encontrado")
          case Failure(ex) =>
            logsAuditoriaService.error("Ha ocurrido un error en borrarCausaPorId()", this.getClass.toString, ex)
            complete(StatusCodes.InternalServerError,
                     s"Ha ocurrido un error interno ${ex.getMessage}")

        }
      }
    }
  }

  /**
    * Devuelve las causas de un riesgo por el identificador del riesgo
    * @return
    */
  private def traerCausasPorRiesgoId() =
    pathEndOrSingleSlash {
      parameter('riesgoId) { riesgoId =>
        get {
          onComplete(
            causasDeclaracionService.listarCausasDeclaracionPorRiesgoId(
              riesgoId.toLong)) {
            case Success(result) =>
              complete(StatusCodes.OK, result.asJson)
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Error consultando los registros por el riesgoId: ${riesgoId}",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError,
                       s"Ha ocurrido un error interno ${ex.getMessage}")
          }
        }
      }
    }
}
