package com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.EstatusRiesgosEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * Clase que mapea la tabla estatus riesgos de la base de datos
  */
private[declaracion_riesgos] trait DeclaracionRiesgosEstatusTable {
  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class DeclaracionRiesgosEstatus(tag: Tag)
      extends Table[EstatusRiesgosEntidad](tag, "estuatus_riesgos") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def estatus_riesgo_nombre = column[String]("estatus_riesgo_nombre")

    def * =
      (
        id.?,
        estatus_riesgo_nombre
      ) <> ((EstatusRiesgosEntidad.apply _).tupled, EstatusRiesgosEntidad.unapply)
  }

  val estatusRiesgo = TableQuery[DeclaracionRiesgosEstatus]
}
