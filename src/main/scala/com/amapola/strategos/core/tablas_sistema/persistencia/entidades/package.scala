package com.amapola.strategos.core.tablas_sistema.persistencia

package object entidades {

  case class CausasRiesgosEntidad(
      id: Option[Long] = None,
      causa_riesgo: String,
      descripcion: Option[String]
  ) {
    def merge(porActualizar: CausasRiesgosEntidad) = {
      porActualizar.copy(
        causa_riesgo = this.causa_riesgo,
        descripcion = this.descripcion
      )
    }
  }

  case class ImpactoRiesgosEntidad(
      id: Option[Long] = None,
      impacto: String,
      puntaje: Long,
      descripcion: Option[String],
  ) {
    def merge(porActualizar: ImpactoRiesgosEntidad) = {
      porActualizar.copy(
        impacto = this.impacto,
        puntaje = this.puntaje,
        descripcion = this.descripcion
      )
    }
  }

  case class ProbabilidadRiesgosEntidad(
      id: Option[Long] = None,
      probabilidad: String,
      puntaje: Long,
      descripcion: Option[String],
  ) {
    def merge(porActualizar: ProbabilidadRiesgosEntidad) = {
      porActualizar.copy(
        probabilidad = this.probabilidad,
        puntaje = this.puntaje,
        descripcion = this.descripcion
      )
    }
  }

  case class TipoRiesgosEntidad(
      id: Option[Long] = None,
      tipo_riesgo: String,
  ) {
    def merge(porActualizar: TipoRiesgosEntidad) = {
      porActualizar.copy(
        tipo_riesgo = this.tipo_riesgo
      )
    }
  }

  case class CalificacionRiesgosEntidad(
      id: Option[Long] = None,
      nombre_calificacion_riesgo: String,
      rango_minimo: Long,
      rango_maximo: Long,
      color: String,
      accion_tomar: String
  ) {
    def merge(porActualizar: CalificacionRiesgosEntidad) = {
      porActualizar.copy(
        nombre_calificacion_riesgo = this.nombre_calificacion_riesgo,
        rango_minimo = this.rango_minimo,
        rango_maximo = this.rango_maximo,
        color = this.color,
        accion_tomar = this.accion_tomar
      )
    }
  }

  case class RespuestasRiesgosEntidad(
      id: Option[Long] = None,
      respuestaRiesgoNombre: String,
      descripcion: Option[String]
  ) {
    def merge(porActualizar: RespuestasRiesgosEntidad) = {
      porActualizar.copy(
        respuestaRiesgoNombre = this.respuestaRiesgoNombre,
        descripcion = this.descripcion
      )
    }
  }

  case class EfectividadRiesgosEntidad(
      id: Option[Long] = None,
      efectividad_nombre: String,
      puntaje: Int,
      descripcion: Option[String]
  ) {
    def merge(porActualizar: EfectividadRiesgosEntidad) = {
      porActualizar.copy(
        efectividad_nombre = this.efectividad_nombre,
        puntaje = this.puntaje,
        descripcion = this.descripcion
      )
    }
  }
}
