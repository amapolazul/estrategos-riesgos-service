package com.amapola.strategos.core.procesos.persistencia.tablas

import com.amapola.strategos.core.procesos.persistencia.entidades.{ProcesoCaracterizacionesEntidad, ProcesoDocumentosEntidad}
import com.amapola.strategos.utils.db.DatabaseConnector
import slick.lifted.ForeignKeyQuery

/**
  * Definicion de la tabla Proceso_Documentos. Este trait se encarga de mapear la entidad de base de datos
  * a una clase para su uso
  */
private[procesos] trait ArchivosCaracterizacionesTable extends CaracterizacionesTable{

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class ProcesoDocumentos(tag: Tag) extends Table[ProcesoDocumentosEntidad](tag, "proceso_documentos") {
    def procedimientoDocumentoId = column[Long]("procedimiento_documento_id", O.PrimaryKey, O.AutoInc)
    def caracterizacionId = column[Long]("caraceterizacion_id")
    def procDocumentoNombre = column[String]("procedimiento_documento_nombre")
    def procDocumentoDesc = column[String]("procedimiento_documento_descripcion")
    def procDocumentoCodigo = column[String]("procedimiento_documento_codigo")
    def procDocumentoArchivo = column[String]("procedimiento_documento_arch")

    def procesoProductoFk: ForeignKeyQuery[Caracterizaciones, ProcesoCaracterizacionesEntidad] =
      foreignKey("DOCUMENTO_CARACTERIZACION_FK", caracterizacionId, caracterizaciones)(
        _.procesoId,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def * =
      (procedimientoDocumentoId.?,
        caracterizacionId,
        procDocumentoNombre,
        procDocumentoDesc,
        procDocumentoCodigo,
        procDocumentoArchivo) <> ((ProcesoDocumentosEntidad.apply _).tupled, ProcesoDocumentosEntidad.unapply)
  }

  val caracterizacionDocumentos = TableQuery[ProcesoDocumentos]
}
