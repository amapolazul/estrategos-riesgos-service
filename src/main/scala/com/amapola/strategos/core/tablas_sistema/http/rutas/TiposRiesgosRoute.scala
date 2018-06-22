package com.amapola.strategos.core.tablas_sistema.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.{entity, _}
import com.amapola.strategos.core.tablas_sistema.http.json._
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.tablas_sistema.servicios.TipoRiesgosService
import com.amapola.strategos.utils.http.{FileUploadDirectives, StrategosCorsSettings}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class TiposRiesgosRoute(tipoRiesgosServie: TipoRiesgosService)(
    implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport
    with FileUploadDirectives
      with StrategosCorsSettings{

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
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }
}
