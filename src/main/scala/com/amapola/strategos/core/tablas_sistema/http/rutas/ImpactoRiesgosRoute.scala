package com.amapola.strategos.core.tablas_sistema.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import com.amapola.strategos.core.tablas_sistema.http.json._
import com.amapola.strategos.core.tablas_sistema.servicios.ImpactoRiesgosService
import com.amapola.strategos.utils.http.{
  FileUploadDirectives,
  StrategosCorsSettings
}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ImpactoRiesgosRoute(impactoRiesgosService: ImpactoRiesgosService)(
    implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport
    with FileUploadDirectives
    with StrategosCorsSettings {

  def getPaths: Route = {
    cors(settings) {
      pathPrefix("impacto-riesgos") {
        traerImpactoRiesgosPorId ~ traerImpactosRiesgo ~ crearImpactoRiesgo ~ actualizarImpactoRiesgo ~ borrarImpactoRiesgo
      }
    }
  }

  private def traerImpactoRiesgosPorId = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(impactoRiesgosService.traerImpactoRiesgoPorId(id)) {
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
        onComplete(impactoRiesgosService.traerImpactoRiesgos()) {
          case Success(result) => complete(StatusCodes.OK, result.asJson)
          case Failure(ex) =>
            complete(StatusCodes.InternalServerError, ex.getMessage)
        }
      }
    }
  }

  private def crearImpactoRiesgo = {
    pathEndOrSingleSlash {
      post {
        entity(as[ImpactoRiesgosJson]) { entity =>
          onComplete(impactoRiesgosService.crearImpactoRiesgos(entity)) {
            case Success(_) =>
              complete(StatusCodes.OK, "Impacto creado correctamente")
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    }
  }

  private def actualizarImpactoRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        put {
          entity(as[ImpactoRiesgosJson]) { entity =>
            onComplete(
              impactoRiesgosService.actualizarImpactoRiesgo(id, entity)) {
              case Success(result) =>
                if (result)
                  complete(StatusCodes.OK, "Impacto actualizado correctamente")
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

  private def borrarImpactoRiesgo = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        delete {
          onComplete(impactoRiesgosService.borrarImpactoRiesgo(id)) {
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
