package com.amapola.strategos.utils.http

import java.io.File

import akka.http.scaladsl.model.{HttpEntity, MediaTypes, Multipart}
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.directives.BasicDirectives.extractRequestContext
import akka.http.scaladsl.server.directives.FileInfo
import akka.http.scaladsl.server.directives.FutureDirectives.onSuccess
import akka.http.scaladsl.server.directives.MarshallingDirectives.{as, entity}
import akka.stream.scaladsl.{FileIO, Sink, Source}

import akka.http.scaladsl.server.Directives.{
  complete,
  get,
  onComplete,
  pathEndOrSingleSlash,
  _
}

import scala.collection.immutable

trait FileUploadDirectives {
  def tempDestination(fileInfo: FileInfo): File =
    File.createTempFile(fileInfo.fileName, ".tmp")
}

object FileUploadDirectives {

  /**
    * Directiva especial para cargar los archivos a una locación temporal segura para luego guardarla
    * el el directorio especificado
    * @param destFn
    * @return
    */
  def customStoreUploadedFiles(implicit destFn: FileInfo ⇒ File)
    : Directive1[immutable.Seq[(FileInfo, File)]] =
    entity(as[Multipart.FormData]).flatMap { formData ⇒
      extractRequestContext.flatMap { ctx ⇒
        implicit val mat = ctx.materializer
        implicit val ec = ctx.executionContext

        val uploaded: Source[(FileInfo, File), Any] = formData.parts
          .mapConcat { part ⇒
            if (part.filename.isDefined) part :: Nil
            else {
              part.entity.discardBytes()
              Nil
            }
          }
          .mapAsync(1) { part ⇒
            val fileInfo =
              FileInfo(part.name, part.filename.get, part.entity.contentType)
            val dest = destFn(fileInfo)

            part.entity.dataBytes.runWith(FileIO.toPath(dest.toPath)).map { _ ⇒
              (fileInfo, dest)
            }
          }

        val uploadedF = uploaded.runWith(Sink.seq[(FileInfo, File)])

        onSuccess(uploadedF)
      }
    }

}
