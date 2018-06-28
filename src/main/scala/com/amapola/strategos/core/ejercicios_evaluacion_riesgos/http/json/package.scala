package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.http

import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.entidades.{
  EjerciciosEvaluacionEstatusEntidad,
  EjerciciosEvaluacionRiesgosEntidad
}

package object json {

  case class EjerciciosEvaluacionEstatusJson(
      id: Long,
      estatus: String
  )

  object EjerciciosEvaluacionEstatusJson {
    def fromEntity(entidad: EjerciciosEvaluacionEstatusEntidad) = {
      EjerciciosEvaluacionEstatusJson(
        id = entidad.id,
        estatus = entidad.estatus
      )
    }

    def toEntity(json: EjerciciosEvaluacionEstatusJson)
      : EjerciciosEvaluacionEstatusEntidad = {
      EjerciciosEvaluacionEstatusEntidad(
        id = json.id,
        estatus = json.estatus
      )
    }
  }

  case class EjerciciosEvaluacionRiesgosJson(
      id: Option[Long],
      proceso_id: Long,
      estatus_id: Long,
      descripcion: String,
      fecha_creacion_ejercicio: Long
  )

  object EjerciciosEvaluacionRiesgosJson {
    def fromEntity(entidad: EjerciciosEvaluacionRiesgosEntidad) = {
      EjerciciosEvaluacionRiesgosJson(
        entidad.id,
        entidad.proceso_id,
        entidad.estatus_id,
        entidad.descripcion,
        entidad.fecha_creacion_ejercicio
      )
    }

    def toEntity(json: EjerciciosEvaluacionRiesgosJson) = {
      EjerciciosEvaluacionRiesgosEntidad(
        id = json.id,
        proceso_id = json.proceso_id,
        estatus_id = json.estatus_id,
        descripcion = json.descripcion,
        fecha_creacion_ejercicio = json.fecha_creacion_ejercicio
      )
    }
  }
}
