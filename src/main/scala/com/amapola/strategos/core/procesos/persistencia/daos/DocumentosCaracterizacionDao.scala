package com.amapola.strategos.core.procesos.persistencia.daos

import com.amapola.strategos.core.procesos.persistencia.entidades.ProcesoDocumentosEntidad
import com.amapola.strategos.core.procesos.persistencia.tablas.ArchivosCaracterizacionesTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait DocumentosCaracterizacionDao {

  /**
    * Retorna los documentos asociados a una caracterizacion
    *
    * @param caracterizacionId
    * @return
    */
  def getDocumentosPorCaracterizacion(
      caracterizacionId: Long): Future[Seq[ProcesoDocumentosEntidad]]

  /**
    * Devuelve un Option de un registro asociado a un documento para una caracterizacion
    *
    * @param prodDocId
    * @return Some si el documento existe None si no se encuentra documento asociado
    */
  def getDocumentoCaracterizacionPorId(
      prodDocId: Long): Future[Option[ProcesoDocumentosEntidad]]

  /**
    * Borra un documento dado un procDocId
    *
    * @param prodDocId
    * @return
    */
  def borrarDocumentoCaracterizacion(prodDocId: Long): Future[Boolean]

  /**
    * Actualiza un archivo de una caracterizacion dado un prodDocId
    *
    * @param prodDocId
    * @param entidad
    * @return
    */
  def actualizarDocumentoCaracetizacion(
      prodDocId: Long,
      entidad: ProcesoDocumentosEntidad): Future[Boolean]

  /**
    * Crea un documento asociado a una caracterizacion
    * @param entidad
    */
  def crearDocumentoCaracterizacion(
      entidad: ProcesoDocumentosEntidad): Future[Long]

}

class DocumentosCaracterizacionDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends DocumentosCaracterizacionDao
    with ArchivosCaracterizacionesTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Retorna los documentos asociados a una caracterizacion
    *
    * @param caracterizacionId
    * @return
    */
  override def getDocumentosPorCaracterizacion(
      caracterizacionId: Long): Future[Seq[ProcesoDocumentosEntidad]] =
    db.run(
      caracterizacionDocumentos
        .filter(_.caracterizacionId === caracterizacionId)
        .result)

  /**
    * Devuelve un Option de un registro asociado a un documento para una caracterizacion
    *
    * @param prodDocId
    * @return Some si el documento existe None si no se encuentra documento asociado
    */
  override def getDocumentoCaracterizacionPorId(
      prodDocId: Long): Future[Option[ProcesoDocumentosEntidad]] =
    db.run(
      caracterizacionDocumentos
        .filter(_.procedimientoDocumentoId === prodDocId)
        .result
        .headOption)

  /**
    * Borra un documento dado un procDocId
    *
    * @param prodDocId
    * @return
    */
  override def borrarDocumentoCaracterizacion(
      prodDocId: Long): Future[Boolean] =
    db.run(
        caracterizacionDocumentos
          .filter(_.procedimientoDocumentoId === prodDocId)
          .delete)
      .map(_ == 1)

  /**
    * Actualiza un archivo de una caracterizacion dado un prodDocId
    *
    * @param prodDocId
    * @param entidad
    * @return
    */
  override def actualizarDocumentoCaracetizacion(
      prodDocId: Long,
      entidad: ProcesoDocumentosEntidad): Future[Boolean] = {
    getDocumentoCaracterizacionPorId(prodDocId) flatMap {
      case Some(antiguo) =>
        val actualizado = antiguo.merge(entidad)
        db.run(
            caracterizacionDocumentos
              .filter(_.procedimientoDocumentoId === prodDocId)
              .update(actualizado))
          .map(_ == 1)
      case None => Future.successful(false)
    }
  }

  /**
    * Crea un documento asociado a una caracterizacion
    *
    * @param entidad
    */
  override def crearDocumentoCaracterizacion(
      entidad: ProcesoDocumentosEntidad): Future[Long] =
    db.run(
      caracterizacionDocumentos returning caracterizacionDocumentos.map(
        _.procedimientoDocumentoId) += entidad)
}
