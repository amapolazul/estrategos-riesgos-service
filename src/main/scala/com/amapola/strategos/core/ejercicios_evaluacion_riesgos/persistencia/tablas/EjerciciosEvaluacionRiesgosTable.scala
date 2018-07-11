package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.tablas

import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.entidades.{
  EjerciciosEvaluacionEstatusEntidad,
  EjerciciosEvaluacionRiesgosEntidad
}
import com.amapola.strategos.core.procesos.persistencia.entidades.ProcesosEntidad
import com.amapola.strategos.core.procesos.persistencia.tablas.ProcesosTable
import com.amapola.strategos.utils.db.DatabaseConnector
import slick.lifted.ForeignKeyQuery

/**
  * Modela la tabla de ejercicios de evaluacion por proceso
  */
private[core] trait EjerciciosEvaluacionRiesgosTable
    extends ProcesosTable
    with EjerciciosEvaluacionesEstatusTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class EjerciciosEvaluacionRiesgos(tag: Tag)
      extends Table[EjerciciosEvaluacionRiesgosEntidad](
        tag,
        "ejercicios_evaluacion_riesgos") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def proceso_id = column[Long]("proceso_id")
    def estatus_id = column[Long]("estatus_id")
    def descripcion = column[String]("descripcion")
    def fecha_creacion_ejercicio = column[Long]("fecha_creacion_ejercicio")

    def procesoEjerciciosEvaluacionfK
      : ForeignKeyQuery[Procesos, ProcesosEntidad] =
      foreignKey("PROCESO_EVALUACION_RIESGO_FK", proceso_id, procesos)(
        _.procesoId,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def procesoCaracterizacionFk
      : ForeignKeyQuery[EjerciciosEvaluacionesEstatus,
                        EjerciciosEvaluacionEstatusEntidad] =
      foreignKey("EVALUACION_RIESGO_STATUS_FK", estatus_id, ejerciciosEstatus)(
        _.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def * =
      (
        id.?,
        proceso_id,
        estatus_id,
        descripcion,
        fecha_creacion_ejercicio,
      ) <> ((EjerciciosEvaluacionRiesgosEntidad.apply _).tupled, EjerciciosEvaluacionRiesgosEntidad.unapply)

  }

  val ejerciciosEvaluacionRiesgos = TableQuery[EjerciciosEvaluacionRiesgos]
}
