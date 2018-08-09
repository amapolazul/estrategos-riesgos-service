package com.amapola.strategos.core.responsables.persistencia

package object entidades {

  case class ResponsablesEntidad(
      responsable_id : Long,
      usuario_id : Option[Long],
      nombre : String,
      cargo : String,
      ubicacion : Option[String],
      email : Option[String],
      notas : Option[String],
      children_count : Option[Long],
      tipo : Long,
      grupo : Long,
      organizacion_id : Option[Long]
  )
}
