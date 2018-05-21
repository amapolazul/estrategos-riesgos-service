package com.amapola.strategos.core.tablas_sistema.persistencia.tablas

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.{ImpactoRiesgosEntidad, ProbabilidadRiesgosEntidad}
import com.amapola.strategos.utils.db.DatabaseConnector

private[tablas_sistema] trait ProbabilidadRiesgosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class ProbabilidadRiesgos(tag: Tag)
    extends Table[ProbabilidadRiesgosEntidad](tag, "probabilidad_riesgos") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def probabilidad = column[String]("probabilidad")
    def puntaje = column[String]("puntaje")
    def descripcion = column[String]("descripcion")

    def * =
      (id.?, probabilidad, puntaje, descripcion) <> ((ProbabilidadRiesgosEntidad.apply _).tupled, ProbabilidadRiesgosEntidad.unapply)
  }

  val probabilidadRiesgos = TableQuery[ProbabilidadRiesgos]

}
