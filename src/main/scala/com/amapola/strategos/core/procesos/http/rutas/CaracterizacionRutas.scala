package com.amapola.strategos.core.procesos.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.LongNumber
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.procesos.servicios.CaracterizacionService
import com.amapola.strategos.utils.http.StrategosCorsSettings
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class CaracterizacionRutas(
    caracterizacionService: CaracterizacionService,
)(implicit executionContext: ExecutionContext,
  logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with StrategosCorsSettings {

  def getPaths = cors(settings) {
    pathPrefix("caracterizaciones") {
      borrarCaracterizacion()
    }
  }

  def borrarCaracterizacion() = {
    pathPrefix(LongNumber) { productoId =>
      pathEndOrSingleSlash {
        delete {
          onComplete(caracterizacionService.borrarCaracterizacion(productoId)) {
            case Success(result) =>
              if (result) {
                complete(StatusCodes.OK,
                         "Registro de caracterizacion borrado correctamente")
              } else {
                complete(StatusCodes.NotFound,
                         "Registro de caracterizacion no encontrado")
              }
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en borrarCaracterizacion",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError,
                       s"Ocurrio un error: ${ex.getMessage}")
          }
        }
      }
    }
  }
}
