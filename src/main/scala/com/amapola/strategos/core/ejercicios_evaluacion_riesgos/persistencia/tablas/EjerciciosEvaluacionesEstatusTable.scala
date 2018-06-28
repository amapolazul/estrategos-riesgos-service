package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.tablas

import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.entidades.EjerciciosEvaluacionEstatusEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

/**
  * Tabla que modela lo estados de un ejercicio de evaluación de riesgo
  */
private[ejercicios_evaluacion_riesgos] trait EjerciciosEvaluacionesEstatusTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class EjerciciosEvaluacionesEstatus(tag: Tag)
      extends Table[EjerciciosEvaluacionEstatusEntidad](
        tag,
        "ejercicios_evaluacion_estatus") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def estatus = column[String]("estatus")

    def * =
      (
        id,
        estatus
      ) <> ((EjerciciosEvaluacionEstatusEntidad.apply _).tupled, EjerciciosEvaluacionEstatusEntidad.unapply)
  }

  val ejerciciosEstatus = TableQuery[EjerciciosEvaluacionesEstatus]
}
