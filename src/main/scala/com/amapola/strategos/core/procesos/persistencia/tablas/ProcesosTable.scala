package com.amapola.strategos.core.procesos.persistencia.tablas

import com.amapola.strategos.core.procesos.persistencia.entidades.ProcesosEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * Definicion de la tabla Procesos. Este trait se encarga de mapear la entidad de base de datos
  * a una clase para su uso
  */
trait ProcesosTable {
  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Procesos(tag: Tag) extends Table[ProcesosEntidad](tag, "procesos") {
    def procesoId = column[Long]("proceso_id", O.PrimaryKey, O.AutoInc)
    def procesoPadreId = column[Long]("proceso_padre_id")
    def procesoNombre = column[String]("proceso_nombre")
    def procesoDescripcion = column[String]("proceso_descripcion")
    def procesoCodigo = column[String]("proceso_codigo")
    def procesoTipo = column[Long]("proceso_tipo")
    def procesoResponsable = column[Long]("proceso_responsable_id")
    def procesoDocumento = column[String]("proceso_documento")

    def * =
      (procesoId.?,
       procesoPadreId.?,
       procesoNombre,
       procesoCodigo.?,
       procesoDescripcion.?,
       procesoTipo.?,
       procesoResponsable.?,
       procesoDocumento) <> ((ProcesosEntidad.apply _).tupled, ProcesosEntidad.unapply)
  }

  protected val procesos = TableQuery[Procesos]
}
