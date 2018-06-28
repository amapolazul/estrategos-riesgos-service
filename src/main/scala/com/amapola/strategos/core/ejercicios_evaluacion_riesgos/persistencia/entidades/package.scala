package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia

package object entidades {

  case class EjerciciosEvaluacionEstatusEntidad (
      id: Long,
      estatus: String
  )

  case class EjerciciosEvaluacionRiesgosEntidad(
      id: Option[Long],
      proceso_id: Long,
      estatus_id: Long,
      descripcion: String,
      fecha_creacion_ejercicio: Long
  ) {

    def merge(porActualizar: EjerciciosEvaluacionRiesgosEntidad) = {
      porActualizar.copy(
        proceso_id = this.proceso_id,
        estatus_id = this.estatus_id,
        descripcion = this.descripcion,
        fecha_creacion_ejercicio = this.fecha_creacion_ejercicio
      )
    }
  }

}
