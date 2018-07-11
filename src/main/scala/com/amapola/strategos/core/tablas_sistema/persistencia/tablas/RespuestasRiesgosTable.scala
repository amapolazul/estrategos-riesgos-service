package com.amapola.strategos.core.tablas_sistema.persistencia.tablas

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.RespuestasRiesgosEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * Clase que mapea a la tabla respuestas_riesgos
  */
private[core] trait RespuestasRiesgosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class RespuestasRiesgos(tag: Tag)
      extends Table[RespuestasRiesgosEntidad](tag, "respuestas_riesgos") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def respuestaRiesgoNombre = column[String]("respuesta_riesgo_nombre")
    def descripcion = column[String]("descripcion")

    override def * =
      (id.?, respuestaRiesgoNombre, descripcion) <> ((RespuestasRiesgosEntidad.apply _).tupled, RespuestasRiesgosEntidad.unapply)
  }

  val respuestasRiesgos = TableQuery[RespuestasRiesgos]

}
