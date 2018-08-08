package com.amapola.strategos.core.procesos.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.amapola.strategos.core.procesos.http.json.ProcesoProcedimientoJson
import com.amapola.strategos.core.procesos.servicios.ProcesosServiciosService
import com.amapola.strategos.infraestructura.AdministradorArchivosServiceImpl
import com.amapola.strategos.utils.http.{
  FileUploadDirectives,
  StrategosCorsSettings
}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ProcesosRutas(
    procesosService: ProcesosServiciosService,
    directorioDestino: String)(implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport
    with FileUploadDirectives
    with StrategosCorsSettings {

  def getPaths = cors(settings) {
    pathPrefix("procesos") {
      crearProcesos ~ crearProcesosSubProcesos ~ getProcesosPorPadreId ~ getProcesoById ~ getProcesos
    }
  }

  /**
    * Define la ruta del servicio rest encargado de recibir el objeto JSON con la información
    * de un proceso
    *
    * @return
    */
  def crearProcesos: Route = {
    pathEndOrSingleSlash {
      post {
        entity(as[ProcesoProcedimientoJson]) { entity =>
          onComplete(procesosService.crearProcesos(entity)) {
            case Success(_) =>
              complete(StatusCodes.Created, "Proceso creado correctamente")
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError,
                       s"Ocurrio un error: ${ex.getMessage}")
          }
        }
      }
    }
  }

  def getProcesoById = {
    pathPrefix(LongNumber) { id =>
      pathEndOrSingleSlash {
        get {
          onComplete(procesosService.traerProcesoPorId(id)) {
            case Success(result) =>
              result
                .map(x => { complete(StatusCodes.OK, x.asJson) })
                .getOrElse(
                  complete(StatusCodes.NotFound, "Proceso no encontrado"))
            case Failure(ex) =>
              complete(StatusCodes.InternalServerError, ex.getMessage())
          }
        }
      }
    }
  }

  def getProcesosPorPadreId = {
    pathPrefix(LongNumber) { id =>
      path("sub-procesos") {
        pathEndOrSingleSlash {
          get {
            onComplete(procesosService.traerProcesosPorIdPadre(id)) {
              case Success(results) =>
                complete(StatusCodes.OK, results.asJson)
              case Failure(ex) =>
                complete(StatusCodes.InternalServerError, ex.getMessage())
            }
          }
        }
      }
    }
  }

  def getProcesos = {
    pathEndOrSingleSlash {
      get {
        onComplete(procesosService.traerProcesos()) {
          case Success(results) =>
            complete(StatusCodes.OK, results.asJson)
          case Failure(ex) =>
            complete(StatusCodes.InternalServerError, ex.getMessage())
        }
      }
    }
  }

  def crearProcesosSubProcesos: Route = {
    pathPrefix("upload") {
      post {
        FileUploadDirectives.customStoreUploadedFiles(tempDestination) {
          files =>
            val finalStatus = files.foldLeft(StatusCodes.OK) {
              case (status, (metadata, file)) =>
                AdministradorArchivosServiceImpl.crearArchivo(file,
                                                              metadata.fileName,
                                                              directorioDestino)
                file.delete()
                status
            }

            complete(finalStatus, "Archivos cargados correctamente")
        }
      }
    }
  }
}
