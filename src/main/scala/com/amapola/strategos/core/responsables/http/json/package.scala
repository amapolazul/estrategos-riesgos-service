package com.amapola.strategos.core.responsables.http

import com.amapola.strategos.core.responsables.persistencia.entidades.ResponsablesEntidad

package object json {

  case class ResponsablesJson(
      responsable_id: Long,
      usuario_id: Option[Long],
      nombre: String,
      cargo: String,
      ubicacion: Option[String],
      email: Option[String],
      notas: Option[String],
      children_count: Option[Long],
      tipo: Long,
      grupo: Long,
      organizacion_id: Option[Long]
  )

  case object ResponsablesJson {
    def fromEntity(entidad: ResponsablesEntidad): ResponsablesJson = {
      ResponsablesJson(
        responsable_id = entidad.responsable_id,
        usuario_id = entidad.usuario_id,
        nombre = entidad.nombre,
        cargo = entidad.cargo,
        ubicacion = entidad.ubicacion,
        email = entidad.email,
        notas = entidad.notas,
        children_count = entidad.children_count,
        tipo = entidad.tipo,
        grupo = entidad.grupo,
        organizacion_id = entidad.organizacion_id
      )
    }

    def toEntity(json: ResponsablesJson): ResponsablesEntidad = {
      ResponsablesEntidad(
        responsable_id = json.responsable_id,
        usuario_id = json.usuario_id,
        nombre = json.nombre,
        cargo = json.cargo,
        ubicacion = json.ubicacion,
        email = json.email,
        notas = json.notas,
        children_count = json.children_count,
        tipo = json.tipo,
        grupo = json.grupo,
        organizacion_id = json.organizacion_id
      )
    }
  }

}
