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
import com.amapola.strategos.core.declaracion_riesgos.http.json.EfectosDeclaracionRiesgosJson
import com.amapola.strategos.core.declaracion_riesgos.servicios.EfectosDeclaracionService
import com.amapola.strategos.utils.http.StrategosCorsSettings
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class EfectosDeclaracionRoute(
    efectosDeclaracionService: EfectosDeclaracionService)(
    implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport
    with StrategosCorsSettings {

  /**
    * Devuelve las rutas asociadas al dominio ejercicio estados
    * @return
    */
  def getPaths = cors(settings) {
    pathPrefix("efectos-riesgos") {
      actualizarEfectoRiesgoPorId() ~
        borrarEfectoPorId() ~
        traerEfectosPorRiesgoId()
    }
  }

  /**
    * Actualiza la causa de un riesgo por su identificador con los datos enviados en la peticion
    * @return
    */
  private def actualizarEfectoRiesgoPorId() = pathPrefix(LongNumber) {
    causaId =>
      pathEndOrSingleSlash {
        put {
          entity(as[EfectosDeclaracionRiesgosJson]) { entity =>
            onComplete(
              efectosDeclaracionService.actualizarEfectoDeclaracion(causaId,
                                                                    entity)) {
              case Success(result) =>
                if (result)
                  complete(StatusCodes.OK, "Registro actualizado correctamente")
                else complete(StatusCodes.NotFound, "Registro no encontrado")
              case Failure(ex) =>
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
  private def borrarEfectoPorId() = pathPrefix(LongNumber) { efectoId =>
    pathEndOrSingleSlash {
      delete {
        onComplete(efectosDeclaracionService.borrarEfectoDeclaracion(efectoId)) {
          case Success(result) =>
            if (result)
              complete(StatusCodes.OK, "Registro borrado correctamente")
            else complete(StatusCodes.NotFound, "Registro no encontrado")
          case Failure(ex) =>
            complete(StatusCodes.InternalServerError,
                     s"Ha ocurrido un error interno ${ex.getMessage}")

        }
      }
    }
  }

  /**
    * Devuelve los efectos de un riesgo por el identificador del riesgo
    * @return
    */
  private def traerEfectosPorRiesgoId() =
    pathEndOrSingleSlash {
      parameter('riesgoId) { riesgoId =>
        get {
          onComplete(
            efectosDeclaracionService.listarEfectosDeclaracionPorRiesgoId(
              riesgoId.toLong)) {
            case Success(result) =>
              complete(StatusCodes.OK, result.asJson)
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError,
                       s"Ha ocurrido un error interno ${ex.getMessage}")
          }
        }
      }
    }

}
