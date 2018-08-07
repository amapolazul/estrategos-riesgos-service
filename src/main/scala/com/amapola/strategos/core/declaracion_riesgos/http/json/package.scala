package com.amapola.strategos.core.declaracion_riesgos.http

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades._

package object json {

  case class DeclaracionRiesgosJson(
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
      fecha_actualizacion: Long,
      fecha_ejercicio: Option[Long] = None,
      calificacion_riesgo: Option[String] = None

  ) {
    require(proceso_id > 0, "El campo proceso_id debe ser mayor a 0")
    require(ejercicio_riesgo_id > 0,
            "El campo ejercicio_riesgo_id debe ser mayor a 0")
    require(tipo_riesgo_id > 0, "El campo tipo_riesgo_id debe ser mayor a 0")
    require(respuesta_riesgo_id > 0,
            "El campo respuesta_riesgo_id debe ser mayor a 0")
    require(estatus_riesgo_id > 0,
            "El campo estatus_riesgo_id debe ser mayor a 0")
    require(!factor_riesgo.isEmpty, "El campo factor_riesgo no puede ser vacio")
    require(!efectividad_controles.isEmpty, "El campo efectividad_controles no puede ser vacio")
    require(!probabilidad.isEmpty, "El campo probabilidad no puede ser vacio")
    require(!impacto.isEmpty, "El campo impacto no puede ser vacio")
    require(!severidad.isEmpty, "El campo severidad no puede ser vacio")
    require(fecha_creacion > 0, "El campo fecha_creacion debe ser mayor a 0")
    require(fecha_actualizacion > 0,
            "El campo fecha_creacion debe ser mayor a 0")

  }

  object DeclaracionRiesgosJson {
    def fromEntity(entity: DeclaracionRiesgosEntidad) = {
      DeclaracionRiesgosJson(
        id = entity.id,
        proceso_id = entity.proceso_id,
        ejercicio_riesgo_id = entity.ejercicio_riesgo_id,
        tipo_riesgo_id = entity.tipo_riesgo_id,
        respuesta_riesgo_id = entity.respuesta_riesgo_id,
        estatus_riesgo_id = entity.estatus_riesgo_id,
        factor_riesgo = entity.factor_riesgo,
        descripcion = entity.descripcion,
        efectividad_controles = entity.efectividad_controles,
        probabilidad = entity.probabilidad,
        historico = entity.historico,
        impacto = entity.impacto,
        severidad = entity.severidad,
        riesgo_residual = entity.riesgo_residual,
        fecha_creacion = entity.fecha_creacion,
        fecha_actualizacion = entity.fecha_actualizacion
      )
    }

    def toEntity(json: DeclaracionRiesgosJson) = {
      DeclaracionRiesgosEntidad(
        id = json.id,
        proceso_id = json.proceso_id,
        ejercicio_riesgo_id = json.ejercicio_riesgo_id,
        tipo_riesgo_id = json.tipo_riesgo_id,
        respuesta_riesgo_id = json.respuesta_riesgo_id,
        estatus_riesgo_id = json.estatus_riesgo_id,
        factor_riesgo = json.factor_riesgo,
        descripcion = json.descripcion,
        efectividad_controles = json.efectividad_controles,
        probabilidad = json.probabilidad,
        historico = json.historico,
        impacto = json.impacto,
        severidad = json.severidad,
        riesgo_residual = json.riesgo_residual,
        fecha_creacion = json.fecha_creacion,
        fecha_actualizacion = json.fecha_actualizacion
      )
    }
  }

  case class EstatusRiesgosJson(
      id: Option[Long] = None,
      estatus_riesgo_nombre: String
  ) {
    require(!estatus_riesgo_nombre.isEmpty,
            "El campo estatus_riesgo_nombre no puede ser vacio")
  }

  object EstatusRiesgosJson {
    def fromEntity(entity: EstatusRiesgosEntidad) = {
      EstatusRiesgosJson(
        id = entity.id,
        estatus_riesgo_nombre = entity.estatus_riesgo_nombre
      )
    }

    def toEntity(json: EstatusRiesgosJson) = {
      EstatusRiesgosEntidad(
        id = json.id,
        estatus_riesgo_nombre = json.estatus_riesgo_nombre
      )
    }
  }

  case class CausasDeclaracionRiesgosJson(
      id: Option[Long] = None,
      probabilidad_riesgo_id: Long,
      declaracion_riesgo_id: Option[Long],
      causa: String,
      descripcion: String
  ) {
    require(probabilidad_riesgo_id > 0,
            "El campo probabilidad_riesgo_id debe ser mayor a 0")
    require(!causa.isEmpty, "El campo causa no puede ser vacio")
    require(!descripcion.isEmpty, "El descripcion causa no puede ser vacio")
  }

  object CausasDeclaracionRiesgosJson {
    def fromEntity(entity: CausasDeclaracionRiesgosEntidad) = {
      CausasDeclaracionRiesgosJson(
        id = entity.id,
        probabilidad_riesgo_id = entity.probabilidad_riesgo_id,
        declaracion_riesgo_id = entity.declaracion_riesgo_id,
        causa = entity.causa,
        descripcion = entity.descripcion
      )
    }

    def toEntity(json: CausasDeclaracionRiesgosJson) = {
      CausasDeclaracionRiesgosEntidad(
        id = json.id,
        probabilidad_riesgo_id = json.probabilidad_riesgo_id,
        declaracion_riesgo_id = json.declaracion_riesgo_id,
        causa = json.causa,
        descripcion = json.descripcion
      )
    }
  }

  case class EfectosDeclaracionRiesgosJson(
      id: Option[Long] = None,
      impacto_riesgos_id: Long,
      declaracion_riesgo_id: Option[Long],
      impacto: String,
      descripcion: String
  ) {
    require(impacto_riesgos_id > 0,
            "El campo declaracion_riesgo_id debe ser mayor a 0")
    require(!impacto.isEmpty, "El campo impacto no puede ser vacio")
    require(!descripcion.isEmpty, "El campo descripcion no puede ser vacio")
  }

  object EfectosDeclaracionRiesgosJson {
    def fromEntity(entity: EfectosDeclaracionRiesgosEntidad) = {
      EfectosDeclaracionRiesgosJson(
        id = entity.id,
        impacto_riesgos_id = entity.impacto_riesgos_id,
        declaracion_riesgo_id = entity.declaracion_riesgo_id,
        impacto = entity.impacto,
        descripcion = entity.descripcion
      )
    }

    def toEntity(json: EfectosDeclaracionRiesgosJson) = {
      EfectosDeclaracionRiesgosEntidad(
        id = json.id,
        impacto_riesgos_id = json.impacto_riesgos_id,
        declaracion_riesgo_id = json.declaracion_riesgo_id,
        impacto = json.impacto,
        descripcion = json.descripcion
      )
    }
  }

  case class ControlesDeclaracionRiesgosJson(
      id: Option[Long] = None,
      efectividad_riesgos_id: Long,
      declaracion_riesgo_id: Option[Long],
      control: String,
      descripcion: String
  ) {
    require(efectividad_riesgos_id > 0,
            "El campo efectividad_riesgos_id no puede ser vacio")
    require(!control.isEmpty, "El campo control no puede ser vacio")
    require(!descripcion.isEmpty, "El campo descripcion no puede ser vacio")
  }

  object ControlesDeclaracionRiesgosJson {
    def fromEntity(entity: ControlesDeclaracionRiesgosEntidad) = {
      ControlesDeclaracionRiesgosJson(
        id = entity.id,
        efectividad_riesgos_id = entity.efectividad_riesgos_id,
        declaracion_riesgo_id = entity.declaracion_riesgo_id,
        control = entity.control,
        descripcion = entity.descripcion
      )
    }
    def toEntity(json: ControlesDeclaracionRiesgosJson) = {
      ControlesDeclaracionRiesgosEntidad(
        id = json.id,
        efectividad_riesgos_id = json.efectividad_riesgos_id,
        declaracion_riesgo_id = json.declaracion_riesgo_id,
        control = json.control,
        descripcion = json.descripcion
      )
    }
  }

  case class DeclaracionRiesgosRequestJson(
      declaracionRiesgo: DeclaracionRiesgosJson,
      causasDeclaracionRiesgo: List[CausasDeclaracionRiesgosJson],
      efectosDeclaracionRiesgo: List[EfectosDeclaracionRiesgosJson],
      controlesDeclaracionRiesgo: List[ControlesDeclaracionRiesgosJson],
  ) {
    require(causasDeclaracionRiesgo.size >= 1, "Debe existir por lo menos una causa")
    require(controlesDeclaracionRiesgo.size >= 1, "Debe existir por lo menos un control")
    require(efectosDeclaracionRiesgo.size >= 1, "Debe existir por lo menos un efecto")
  }
}
