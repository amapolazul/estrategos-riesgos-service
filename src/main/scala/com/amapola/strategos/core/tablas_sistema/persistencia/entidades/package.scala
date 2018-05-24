package com.amapola.strategos.core.tablas_sistema.persistencia

package object entidades {

  case class CausasRiesgosEntidad (
    id : Option[Long] = None,
    causa_riesgo: String,
    descripcion: String
  ) {
    def merge(porActualizar: CausasRiesgosEntidad) = {
      porActualizar.copy(
        causa_riesgo = this.causa_riesgo,
        descripcion = this.descripcion
      )
    }
  }

  case class ImpactoRiesgosEntidad (
    id : Option[Long] = None,
    impacto: String,
    puntaje: String,
    descripcion: String,
  ) {
    def merge(porActualizar: ImpactoRiesgosEntidad) = {
      porActualizar.copy(
        impacto = this.impacto,
        puntaje = this.puntaje,
        descripcion = this.descripcion
      )
    }
  }

  case class ProbabilidadRiesgosEntidad (
    id : Option[Long] = None,
    probabilidad: String,
    puntaje: String,
    descripcion: String,
  ) {
    def merge(porActualizar: ProbabilidadRiesgosEntidad) = {
      porActualizar.copy(
        probabilidad = this.probabilidad,
        puntaje = this.puntaje,
        descripcion = this.descripcion
      )
    }
  }

  case class TipoRiesgosEntidad (
    id : Option[Long] = None,
    tipo_riesgo: String,
  ) {
    def merge(porActualizar: TipoRiesgosEntidad) = {
      porActualizar.copy(
        tipo_riesgo = this.tipo_riesgo
      )
    }
  }
}
