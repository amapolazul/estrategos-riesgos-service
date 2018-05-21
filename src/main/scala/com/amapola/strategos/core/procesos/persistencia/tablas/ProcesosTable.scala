package com.amapola.strategos.core.procesos.persistencia.tablas

import com.amapola.strategos.core.procesos.persistencia.entidades.ProcesosEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * Definicion de la tabla Procesos. Este trait se encarga de mapear la entidad de base de datos
  * a una clase para su uso
  */
private[procesos] trait ProcesosTable {
  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Procesos(tag: Tag) extends Table[ProcesosEntidad](tag, "Procesos") {
    def procesoId = column[Long]("Proceso_Id", O.PrimaryKey, O.AutoInc)
    def procesoPadreId = column[Long]("Proceso_Padre_Id")
    def procesoNombre = column[String]("Proceso_Nombre")
    def procesoCodigo = column[String]("Proceso_Codigo")
    def procesoTipo = column[Long]("Proceso_Tipo")
    def procesoResponsable = column[Long]("Proceso_Responsable_Id")
    def procesoDocumento = column[String]("Proceso_Documento")

    def * =
      (procesoId.?,
       procesoPadreId.?,
       procesoNombre,
       procesoCodigo,
       procesoTipo,
       procesoResponsable,
       procesoDocumento) <> ((ProcesosEntidad.apply _).tupled, ProcesosEntidad.unapply)
  }

  protected val procesos = TableQuery[Procesos]
}
