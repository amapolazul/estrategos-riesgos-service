package com.amapola.strategos.core.declaracion_riesgos.persistencia

package object entidades {

  case class DeclaracionRiesgosEntidad(
      id: Option[Long] = None,
      proceso_id: Long,
      ejercicio_riesgo_id: Long,
      tipo_riesgo_id: Long,
      respuesta_riesgo_id: Long,
      estatus_riesgo_id: Long,
      factor_riesgo: String,
      descripcion: Option[String],
      efectividad_controles: String,
      probabilidad: String,
      historico: Boolean,
      impacto: String,
      severidad: String,
      riesgo_residual: String,
      fecha_creacion: Long,
      fecha_actualizacion: Long
  ) {

    def merge(porActualizar: DeclaracionRiesgosEntidad) = {
      porActualizar.copy(
        proceso_id = this.proceso_id,
        ejercicio_riesgo_id = this.ejercicio_riesgo_id,
        tipo_riesgo_id = this.tipo_riesgo_id,
        respuesta_riesgo_id = this.respuesta_riesgo_id,
        estatus_riesgo_id = this.estatus_riesgo_id,
        factor_riesgo = this.factor_riesgo,
        descripcion = this.descripcion,
        efectividad_controles = this.efectividad_controles,
        probabilidad = this.probabilidad,
        historico = this.historico,
        impacto = this.impacto,
        severidad = this.severidad,
        riesgo_residual = this.riesgo_residual,
        fecha_creacion = this.fecha_creacion,
        fecha_actualizacion = this.fecha_actualizacion
      )
    }
  }

  case class EstatusRiesgosEntidad(
      id: Option[Long] = None,
      estatus_riesgo_nombre: String
  )

  case class CausasDeclaracionRiesgosEntidad(
      id: Option[Long] = None,
      probabilidad_riesgo_id: Long,
      declaracion_riesgo_id: Option[Long],
      causa: Long,
      descripcion: Option[String]
  ) {

    def merge(porActualizar: CausasDeclaracionRiesgosEntidad) = {
      porActualizar.copy(
        probabilidad_riesgo_id = this.probabilidad_riesgo_id,
        declaracion_riesgo_id = this.declaracion_riesgo_id,
        causa = this.causa,
        descripcion = this.descripcion
      )
    }
  }

  case class EfectosDeclaracionRiesgosEntidad(
      id: Option[Long] = None,
      impacto_riesgos_id: Long,
      declaracion_riesgo_id: Option[Long],
      impacto: String,
      descripcion: Option[String]
  ) {

    def merge(porActualizar: EfectosDeclaracionRiesgosEntidad) = {
      porActualizar.copy(
        impacto_riesgos_id = this.impacto_riesgos_id,
        declaracion_riesgo_id = this.declaracion_riesgo_id,
        impacto = this.impacto,
        descripcion = this.descripcion
      )
    }
  }

  case class ControlesDeclaracionRiesgosEntidad(
      id: Option[Long] = None,
      efectividad_riesgos_id: Long,
      declaracion_riesgo_id: Option[Long],
      control: String,
      descripcion: Option[String]
  ) {
    def merge(porActualizar: ControlesDeclaracionRiesgosEntidad) = {
      porActualizar.copy(
        efectividad_riesgos_id = this.efectividad_riesgos_id,
        declaracion_riesgo_id = this.declaracion_riesgo_id,
        control = this.control,
        descripcion = this.descripcion
      )
    }
  }
}
