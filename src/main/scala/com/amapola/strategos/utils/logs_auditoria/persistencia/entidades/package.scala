package com.amapola.strategos.utils.logs_auditoria.persistencia

package object entidades {

  case class LogsAuditoriaEntidad(
      id: Option[Long] = None,
      tipo_log: String,
      clase_origen: String,
      mensaje: String,
      fecha_creacion: Long
  )
}
