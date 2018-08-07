package com.amapola.strategos.core.tablas_sistema.persistencia.tablas

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.ImpactoRiesgosEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * Clase que modela la tabla impacto_riesgos de la base de datos
  */
private[core] trait ImpactoRiesgosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class ImpactoRiesgos(tag: Tag)
      extends Table[ImpactoRiesgosEntidad](tag, "impacto_riesgos") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def impacto = column[String]("impacto")
    def puntaje = column[Long]("puntaje")
    def descripcion = column[String]("descripcion")

    def * =
      (id.?, impacto, puntaje, descripcion.?) <> ((ImpactoRiesgosEntidad.apply _).tupled, ImpactoRiesgosEntidad.unapply)
  }

  val impactoRiesgos = TableQuery[ImpactoRiesgos]

}
