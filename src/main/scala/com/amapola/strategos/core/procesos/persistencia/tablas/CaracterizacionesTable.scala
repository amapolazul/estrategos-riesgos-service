package com.amapola.strategos.core.procesos.persistencia.tablas

import com.amapola.strategos.core.procesos.persistencia.entidades.{ProcesoCaracterizacionesEntidad, ProcesosEntidad}
import com.amapola.strategos.utils.db.DatabaseConnector
import slick.lifted.ForeignKeyQuery

/**
  * Definicion de la tabla Proceso_Caracterizaciones. Este trait se encarga de mapear la entidad de base de datos
  * a una clase para su uso.
  */
private[procesos] trait CaracterizacionesTable extends ProcesosTable{

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Caracterizaciones(tag: Tag) extends Table[ProcesoCaracterizacionesEntidad](tag, "Proceso_Caracterizaciones") {
    def caracterizacionId = column[Long]("Caraceterizacion_id", O.PrimaryKey, O.AutoInc)
    def procesoId = column[Long]("Proceso_Id")
    def procedimientoNombre = column[String]("Procedimiento_Nombre")
    def procedimientoCodigo = column[String]("Procedimiento_Codigo")

    def procesoCaracterizacionFk: ForeignKeyQuery[Procesos, ProcesosEntidad] =
      foreignKey("PROCESO_CARACTERIZACION_FK", procesoId, procesos)(
        _.procesoId,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def * =
      (caracterizacionId.?,
        procesoId,
        procedimientoNombre,
        procedimientoCodigo) <> ((ProcesoCaracterizacionesEntidad.apply _).tupled, ProcesoCaracterizacionesEntidad.unapply)
  }

  val caracterizaciones = TableQuery[Caracterizaciones]
}
