package com.amapola.strategos.core.tablas_sistema.http

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.{CausasRiesgosEntidad, ImpactoRiesgosEntidad, ProbabilidadRiesgosEntidad, TipoRiesgosEntidad}

package object json {

  case class CausasRiesgosJson(
      id: Option[Long] = None,
      causa_riesgo: String,
      descripcion: String
  ) {
    require(!descripcion.isEmpty, "El campo descripcion no puede ser vacio")
    require(!causa_riesgo.isEmpty, "El campo causa_riesgo no puede ser vacio")
  }

  object CausasRiesgosJson {
    def toEntity(entidad: CausasRiesgosJson): CausasRiesgosEntidad = {
      CausasRiesgosEntidad(
        id = entidad.id,
        causa_riesgo = entidad.causa_riesgo,
        descripcion = entidad.descripcion
      )
    }

    def fromEntity(entity: CausasRiesgosEntidad) = {
      CausasRiesgosJson(
        id = entity.id,
        causa_riesgo = entity.causa_riesgo,
        descripcion = entity.descripcion
      )
    }
  }

  case class ImpactoRiesgosJson(
      id: Option[Long] = None,
      impacto: String,
      puntaje: String,
      descripcion: String,
  ) {

    require(!impacto.isEmpty, "El campo impacto no puede ser vacio")
    require(!puntaje.isEmpty, "El campo puntaje no puede ser vacio")
    require(!descripcion.isEmpty, "El campo descripcion no puede ser vacio")
  }

  object ImpactoRiesgosJson {
    def toEntity(entidad: ImpactoRiesgosJson): ImpactoRiesgosEntidad = {
      ImpactoRiesgosEntidad(
        id = entidad.id,
        impacto = entidad.impacto,
        puntaje = entidad.puntaje,
        descripcion = entidad.descripcion
      )
    }

    def fromEntity(entidad: ImpactoRiesgosEntidad) = {
      ImpactoRiesgosJson(
        id = entidad.id,
        impacto = entidad.impacto,
        puntaje = entidad.puntaje,
        descripcion = entidad.descripcion
      )
    }
  }

  case class ProbabilidadRiesgosJson(
      id: Option[Long] = None,
      probabilidad: String,
      puntaje: String,
      descripcion: String,
  ) {

    require(!probabilidad.isEmpty, "El campo probabilidad es requerido")
    require(!puntaje.isEmpty, "El campo puntaje es requerido")
    require(!descripcion.isEmpty, "El campo descripcion es requerido")
  }

  object ProbabilidadRiesgosJson {
    def toEntity(entidad: ProbabilidadRiesgosJson): ProbabilidadRiesgosEntidad = {
      ProbabilidadRiesgosEntidad(
        id = entidad.id,
        probabilidad = entidad.probabilidad,
        puntaje = entidad.puntaje,
        descripcion = entidad.descripcion
      )
    }

    def fromEntity(entidad: ProbabilidadRiesgosEntidad) = {
      ProbabilidadRiesgosJson(
        id = entidad.id,
        probabilidad = entidad.probabilidad,
        puntaje = entidad.puntaje,
        descripcion = entidad.descripcion
      )
    }
  }

  case class TipoRiesgosJson(
      id: Option[Long] = None,
      tipo_riesgo: String,
  ) {

    require(!tipo_riesgo.isEmpty, "El campo tipo_riesgo es requerido")
  }

  object TipoRiesgosJson {
    def toEntity(entidad: TipoRiesgosJson): TipoRiesgosEntidad = {
      TipoRiesgosEntidad(
        id = entidad.id,
        tipo_riesgo = entidad.tipo_riesgo
      )
    }

    def fromEntity(entidad: TipoRiesgosEntidad) = {
      TipoRiesgosJson(
        id = entidad.id,
        tipo_riesgo = entidad.tipo_riesgo
      )
    }
  }
}
