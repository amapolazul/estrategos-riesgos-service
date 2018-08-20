package com.amapola.strategos.utils.logs_auditoria.persistencia.enums

/**
  * Define los tipos de los mensajes que se guardan en los eventos de log
  */
object TipoMensajeLogs extends Enumeration {
  val info = Value("INFORMATIVO")
  val error = Value("ERROR")
}