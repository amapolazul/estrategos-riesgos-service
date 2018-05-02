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

  class ProcesoDocumentos(tag: Tag) extends Table[ProcesoDocumentosEntidad](tag, "Proceso_Documentos") {
    def procedimientoDocumentoId = column[Long]("Procedimiento_Documento_Id", O.PrimaryKey, O.AutoInc)
    def caracterizacionId = column[Long]("Caraceterizacion_id")
    def procDocumentoNombre = column[String]("Procedimiento_Documento_Nombre")
    def procDocumentoDesc = column[String]("Procedimiento_Documento_Descripcion")
    def procDocumentoCodigo = column[String]("Procedimiento_Documento_Codigo")
    def procDocumentoArchivo = column[String]("Procedimiento_Documento_Arch")

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
