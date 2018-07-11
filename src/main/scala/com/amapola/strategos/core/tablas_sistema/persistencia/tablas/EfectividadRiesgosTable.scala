package com.amapola.strategos.core.tablas_sistema.persistencia.tablas

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.EfectividadRiesgosEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * Entidad que mapea la tabla efectividad_riesgos
  */
private[core] trait EfectividadRiesgosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class EfectividadRiesgos(tag: Tag)
      extends Table[EfectividadRiesgosEntidad](tag, "efectividad_riesgos") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def efectividad_nombre = column[String]("efectividad_nombre")
    def puntaje = column[Int]("puntaje")
    def descripcion = column[String]("descripcion")

    override def * =
      (id.?, efectividad_nombre, puntaje, descripcion) <> ((EfectividadRiesgosEntidad.apply _).tupled, EfectividadRiesgosEntidad.unapply)
  }

  val efectividadRiesgos = TableQuery[EfectividadRiesgos]

}
