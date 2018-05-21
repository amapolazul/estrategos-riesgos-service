package com.amapola.strategos.core.procesos.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.amapola.strategos.core.procesos.http.json.ProcesoProcedimientoRequest
import com.amapola.strategos.core.procesos.servicios.ProcesosServiciosService
import com.amapola.strategos.infraestructura.AdministradorArchivosServiceImpl
import com.amapola.strategos.utils.http.FileUploadDirectives
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ProcesosRutas(procesosService: ProcesosServiciosService, directorioDestino: String)(implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport with FileUploadDirectives {

  def getPaths = cors() {
    crearProcesos ~ crearProcesosSubProcesos
  }

  /**
    * Define la ruta del servicio rest encargado de recibir el objeto JSON con la información
    * de un proceso
    *
    * @return
    */
  def crearProcesos: Route = {
    pathPrefix("procesos") {
      post {
        entity(as[ProcesoProcedimientoRequest]) { entity =>
          onComplete(procesosService.crearProcesos(entity)) {
            case Success(_) => complete(StatusCodes.Created, "Proceso creado correctamente")
            case Failure(ex) => complete(StatusCodes.InternalServerError, s"Ocurrio un error: ${ex.getMessage}")
          }
        }
      }
    }
  }

  def crearProcesosSubProcesos: Route = {
      pathPrefix("upload") {
        post {
          FileUploadDirectives.customStoreUploadedFiles(tempDestination) { files =>
            val finalStatus = files.foldLeft(StatusCodes.OK) {
              case (status, (metadata, file)) =>
                AdministradorArchivosServiceImpl.crearArchivo(file, directorioDestino)
                file.delete()
                status
            }

            complete(finalStatus)
          }
        }
      }

  }
}
