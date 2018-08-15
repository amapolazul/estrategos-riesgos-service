package com.amapola.strategos.core.tablas_sistema.persistencia.tablas

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.TipoRiesgosEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * Clase que modela la tabla tipos_riesgos de la base de datos
  */
private[core] trait TipoRiesgosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class TiposRiesgos(tag: Tag)
      extends Table[TipoRiesgosEntidad](tag, "tipo_riesgos") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def tipo_riesgo = column[String]("tipo_riesgo")

    def * =
      (id.?, tipo_riesgo) <> ((TipoRiesgosEntidad.apply _).tupled, TipoRiesgosEntidad.unapply)
  }

  val tiposRiesgos = TableQuery[TiposRiesgos]

}
