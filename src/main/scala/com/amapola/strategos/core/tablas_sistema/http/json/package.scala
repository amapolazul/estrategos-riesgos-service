package com.amapola.strategos.core.tablas_sistema.http

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades._

package object json {

  case class CausasRiesgosJson(
      id: Option[Long] = None,
      causa_riesgo: String,
      descripcion: Option[String]
  ) {
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
      puntaje: Long,
      descripcion: Option[String],
  ) {

    require(!impacto.isEmpty, "El campo impacto no puede ser vacio")
    require(puntaje > 0, "El puntaje deb")
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
      puntaje: Long,
      descripcion: Option[String],
  ) {

    require(!probabilidad.isEmpty, "El campo probabilidad es requerido")
    require(puntaje > 0, "El puntaje debe ser mayor a 0")
  }

  object ProbabilidadRiesgosJson {
    def toEntity(
        entidad: ProbabilidadRiesgosJson): ProbabilidadRiesgosEntidad = {
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

  case class CalificacionRiesgosJson(
      id: Option[Long] = None,
      nombre_calificacion_riesgo: String,
      rango_minimo: Long,
      rango_maximo: Long,
      color: String,
      accion_tomar: String
  ) {

    require(!nombre_calificacion_riesgo.isEmpty, "El nombre no puede ser vacio")
    require(rango_minimo < rango_maximo,
            "El rango minimo debe ser menor al rango máximo")
    require(!color.isEmpty, "Debe seleccionar un color")
    require(!accion_tomar.isEmpty, "El campo accion a tomar no puede ser vacio")
  }

  object CalificacionRiesgosJson {
    def toEntity(json: CalificacionRiesgosJson): CalificacionRiesgosEntidad = {
      CalificacionRiesgosEntidad(
        id = json.id,
        nombre_calificacion_riesgo = json.nombre_calificacion_riesgo,
        rango_minimo = json.rango_minimo,
        rango_maximo = json.rango_maximo,
        color = json.color,
        accion_tomar = json.accion_tomar
      )
    }

    def fromEntity(
        entidad: CalificacionRiesgosEntidad): CalificacionRiesgosJson = {
      CalificacionRiesgosJson(
        id = entidad.id,
        nombre_calificacion_riesgo = entidad.nombre_calificacion_riesgo,
        rango_minimo = entidad.rango_minimo,
        rango_maximo = entidad.rango_maximo,
        color = entidad.color,
        accion_tomar = entidad.accion_tomar
      )
    }
  }

  case class RespuestasRiesgoJson(
      id: Option[Long] = None,
      respuestaRiesgoNombre: String,
      descripcion: Option[String]
  ) {
    require(!respuestaRiesgoNombre.isEmpty,
            "El campo respuesta riesgo no puede ser vacio")

  }

  object RespuestasRiesgoJson {
    def toEntity(json: RespuestasRiesgoJson): RespuestasRiesgosEntidad = {
      RespuestasRiesgosEntidad(
        id = json.id,
        respuestaRiesgoNombre = json.respuestaRiesgoNombre,
        descripcion = json.descripcion
      )
    }

    def fromEntity(entidad: RespuestasRiesgosEntidad): RespuestasRiesgoJson = {
      RespuestasRiesgoJson(
        id = entidad.id,
        respuestaRiesgoNombre = entidad.respuestaRiesgoNombre,
        descripcion = entidad.descripcion
      )
    }
  }

  case class EfectividadRiesgosJson(
      id: Option[Long] = None,
      efectividad_nombre: String,
      puntaje: Int = 0,
      descripcion: Option[String]
  ) {
    require(!efectividad_nombre.isEmpty,
            "El campo nombre efectivad no puede ser vacio")
    require(puntaje >= 0, "El campo puntaje debe ser mayor o igual a cero")
  }

  object EfectividadRiesgosJson {
    def toEntity(json: EfectividadRiesgosJson): EfectividadRiesgosEntidad = {
      EfectividadRiesgosEntidad(
        id = json.id,
        efectividad_nombre = json.efectividad_nombre,
        puntaje = json.puntaje,
        descripcion = json.descripcion
      )
    }

    def fromEntity(
        entidad: EfectividadRiesgosEntidad): EfectividadRiesgosJson = {
      EfectividadRiesgosJson(
        id = entidad.id,
        efectividad_nombre = entidad.efectividad_nombre,
        puntaje = entidad.puntaje,
        descripcion = entidad.descripcion
      )
    }
  }
}
