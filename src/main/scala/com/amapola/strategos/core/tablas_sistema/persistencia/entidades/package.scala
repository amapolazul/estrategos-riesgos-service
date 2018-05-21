package com.amapola.strategos.core.tablas_sistema.persistencia

package object entidades {

  case class CausasRiesgosEntidad (
    id : Option[Long],
    causa_riesgo: String,
    descripcion: String
  )

  case class ImpactoRiesgosEntidad (
    id : Option[Long],
    impacto: String,
    puntaje: String,
    descripcion: String,
  )

  case class ProbabilidadRiesgosEntidad (
    id : Option[Long],
    probabilidad: String,
    puntaje: String,
    descripcion: String,
  )

  case class TipoRiesgosEntidad (
    id : Option[Long],
    tipo_riesgo: String,
  )
}
