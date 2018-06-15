package com.amapola.strategos.core.responsables.persistencia

package object entidades {

  case class ResponsablesEntidad(
    id : Option[Long],
    email:  String
  )

}
