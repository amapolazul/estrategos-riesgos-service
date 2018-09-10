package com.amapola.strategos.core.tablas_sistema.persistencia.tablas

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.CausasRiesgosEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * clase que modela la tabla de causas_riesgos en la base de datos
  */
trait CausasRiesgosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class CausasRiesgos(tag: Tag)
      extends Table[CausasRiesgosEntidad](tag, "causas_riesgos") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def causa_riesgo = column[String]("causa_riesgo")
    def descripcion = column[String]("descripcion")

    def * =
      (id.?, causa_riesgo, descripcion.?) <> ((CausasRiesgosEntidad.apply _).tupled, CausasRiesgosEntidad.unapply)
  }

  val causasRiesgos = TableQuery[CausasRiesgos]
}
