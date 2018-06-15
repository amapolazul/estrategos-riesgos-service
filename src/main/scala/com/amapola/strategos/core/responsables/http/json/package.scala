package com.amapola.strategos.core.responsables.http

import com.amapola.strategos.core.responsables.persistencia.entidades.ResponsablesEntidad

package object json {

  case class ResponsablesJson(
      id: Option[Long],
      email: String
  )

  case object ResponsablesJson {
    def fromEntity(entidad: ResponsablesEntidad): ResponsablesJson = {
      ResponsablesJson(
        id = entidad.id,
        email = entidad.email
      )
    }

    def toEntity(json: ResponsablesJson): ResponsablesEntidad = {
      ResponsablesEntidad(
        id = json.id,
        email = json.email
      )
    }
  }

}
