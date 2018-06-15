package com.amapola.strategos.core.tablas_sistema.persistencia.tablas

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.CalificacionRiesgosEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * Entidad que modela la tabla califiacion_riesgos en la base de datos
  */
private[tablas_sistema] trait CalificacionRiesgosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class CalificacionRiesgos(tag: Tag)
      extends Table[CalificacionRiesgosEntidad](tag, "calificacion_riesgos") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def nombre_calificacion_riesgo =
      column[String]("nombre_calificacion_riesgo")
    def rango_minimo = column[Long]("rango_minimo")
    def rango_maximo = column[Long]("rango_maximo")
    def color = column[String]("color")
    def accion_tomar = column[String]("accion_tomar")

    def * =
      (id.?,
       nombre_calificacion_riesgo,
       rango_minimo,
       rango_maximo,
       color,
       accion_tomar) <> ((CalificacionRiesgosEntidad.apply _).tupled, CalificacionRiesgosEntidad.unapply)
  }

  val calificacionRiesgos = TableQuery[CalificacionRiesgos]

}
