package com.amapola.strategos.core.procesos.http.rutas

import akka.http.scaladsl.model.{Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.BasicDirectives.{extractRequestContext, provide}
import akka.http.scaladsl.server.directives.FileInfo
import akka.http.scaladsl.server.directives.FutureDirectives.onSuccess
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import akka.http.scaladsl.server.directives.RouteDirectives.reject
import akka.http.scaladsl.server.{Directive1, MissingFormFieldRejection}
import akka.stream.scaladsl.{Sink, Source}
import akka.util.ByteString
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.amapola.strategos.core.procesos.http.json.ProcesoProcedimientoRequest
import com.amapola.strategos.core.procesos.servicios.ProcesosServiciosService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class ProcesosRutas(procesosService: ProcesosServiciosService)(implicit executionContext: ExecutionContext)
    extends FailFastCirceSupport {

  def getPaths = crearProcesos

  /**
    * Define la ruta del servicio rest encargado de recibir el objeto JSON con la información
    * de un proceso
    *
    * @return
    */
  def crearProcesos = {
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

  def crearProcesosSubProcesos = {
    cors() {
      pathPrefix("upload") {
//        (post & formFieldMap) { fields =>
//
//          val a = fields.get("fileKey")
//          println(fields)
//          complete("")
//        }
        fileUploadNew() {
          case (metadata, byteSource) =>
            onSuccess(Future.successful(true)) { v =>
              complete(s"Mas bien lokita")
            }
        }
      }
    }
  }

  /**
    * Collects each body part that is a multipart file as a tuple containing metadata and a `Source`
    * for streaming the file contents somewhere. If there is no such field the request will be rejected,
    * if there are multiple file parts with the same name, the first one will be used and the subsequent
    * ones ignored.
    *
    * @group fileupload
    */
  def fileUploadNew(): Directive1[(FileInfo, Source[ByteString, Any])] =
    entity(as[Multipart.FormData])
      .flatMap { formData =>
        extractRequestContext.flatMap { ctx =>
          implicit val mat = ctx.materializer
          implicit val ec = ctx.executionContext

//        val onePartSource: Source[(FileInfo, Source[ByteString, Any]), Any] = formData.parts
//          .map(part => {
//            println(part.dispositionParams)
//            (FileInfo(part.name, part.filename.get, part.entity.contentType), part.entity.dataBytes)
//          }).take(1)

          val parameters: Source[(FileInfo, Null), Any] = formData.parts
            .filter(!_.filename.isDefined)
            .map(z => {
              println(z.contentDispositionHeader.value)
              z.entity.dataBytes
                .map(_.utf8String)
                .runWith(Sink.foreach(println(_)))
              println(z.dispositionParams.get("name").get)
              (FileInfo(null, null, null), null)
            })

          val a = parameters.runWith(
            Sink.headOption[(FileInfo, Source[ByteString, Any])])

          //val onePartF = onePartSource.runWith(Sink.headOption[(FileInfo, Source[ByteString, Any])])

          onSuccess(a)
        }

      }
      .flatMap {
        case Some(tuple) ⇒ provide(tuple)
        case None ⇒ reject(MissingFormFieldRejection(""))
      }
}
