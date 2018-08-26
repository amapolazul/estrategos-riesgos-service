package com.amapola.strategos.core.procesos.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers.LongNumber
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.procesos.servicios.ProductosServiciosService
import com.amapola.strategos.utils.http.StrategosCorsSettings
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ProductosServiciosRutas(
    productosServiciosService: ProductosServiciosService,
)(implicit executionContext: ExecutionContext,
  logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with StrategosCorsSettings {

  def getPaths = cors(settings) {
    pathPrefix("productos-servicios") {
      borrarProductoServicio()
    }
  }

  def borrarProductoServicio() = {
    pathPrefix(LongNumber) { productoId =>
      pathEndOrSingleSlash {
        delete {
          onComplete(
            productosServiciosService.borrarProductoServicios(productoId)) {
            case Success(result) =>
              if (result) {
                complete(StatusCodes.OK,
                         "Registro de producto servicio borrado correctamente")
              } else {
                complete(StatusCodes.NotFound,
                         "Registro de producto servicio no encontrado")
              }
            case Failure(ex) =>
              logsAuditoriaService.error(
                s"Ha ocurrido un error en borrarProductoServicio",
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
