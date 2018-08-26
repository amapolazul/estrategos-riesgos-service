package com.amapola.strategos.core.procesos.servicios

import com.amapola.strategos.core.procesos.persistencia.daos.DocumentosCaracterizacionDao
import com.amapola.strategos.infraestructura.AdministradorArchivosServiceImpl

import scala.concurrent.{ExecutionContext, Future}

trait DocumentosCaracterizacionService {

  /**
    * Borra un documento caracterizacion por su identificador
    * @param idDocumentoCarac
    * @return
    */
  def borrarDocumentosCaracterizacion(idDocumentoCarac: Long): Future[Boolean]
}

class DocumentosCaracterizacionServiceImpl(
    documentosCaracterizacionDao: DocumentosCaracterizacionDao)(
    implicit executionContext: ExecutionContext)
    extends DocumentosCaracterizacionService {

  /**
    * Borra un documento caracterizacion por su identificador
    *
    * @param idDocumentoCarac
    * @return
    */
  override def borrarDocumentosCaracterizacion(
      idDocumentoCarac: Long): Future[Boolean] = {
    documentosCaracterizacionDao
      .getDocumentoCaracterizacionPorId(idDocumentoCarac)
      .flatMap(result => {
        result match {
          case Some(doc) =>
            if (AdministradorArchivosServiceImpl.borrarArchivo(
                  doc.procedimiento_Documento_Arch)) {
              documentosCaracterizacionDao.borrarDocumentoCaracterizacion(
                idDocumentoCarac)
            } else {
              Future.failed(new Exception(
                s"Error al borrar el archivo ${doc.procedimiento_Documento_Nombre} con id ${doc.procedimiento_Documento_Id}"))
            }
          case None =>
            Future.successful(false)
        }
      })
  }
}
