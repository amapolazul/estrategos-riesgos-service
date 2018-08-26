package com.amapola.strategos.utils.logs_auditoria.servicios

import java.util.Date

import com.amapola.strategos.utils.logs_auditoria.persistencia.daos.LogsAuditoriaDao
import com.amapola.strategos.utils.logs_auditoria.persistencia.entidades.LogsAuditoriaEntidad
import com.amapola.strategos.utils.logs_auditoria.persistencia.enums.TipoMensajeLogs

trait LogsAuditoriaService {

  /**
    * Guarda un mensaje informativo dentro de la base de datos de logs
    * @param mensaje
    * @param claseOrigen
    */
  def info(mensaje: String, claseOrigen: String) : Unit

  /**
    * Guarda un mensaje de error dentro de la base de datos de logs
    * @param mensaje
    * @param claseOrigen
    */
  def error(mensaje: String, claseOrigen: String, throwable: Throwable) : Unit

}

class LogsAuditoriaServiceImpl(logsAuditoriaDao: LogsAuditoriaDao) extends LogsAuditoriaService {
  /**
    * Guarda un mensaje informativo dentro de la base de datos de logs
    *
    * @param mensaje
    * @param claseOrigen
    */
  override def info(mensaje: String, claseOrigen: String): Unit = {
    val entidad = LogsAuditoriaEntidad(
      tipo_log = TipoMensajeLogs.info.toString,
      clase_origen = claseOrigen,
      mensaje = mensaje,
      fecha_creacion = new Date().getTime
    )

    logsAuditoriaDao.log(entidad)
  }

  /**
    * Guarda un mensaje de error dentro de la base de datos de logs
    *
    * @param mensaje
    * @param claseOrigen
    */
  override def error(mensaje: String, claseOrigen: String, throwable: Throwable): Unit = {
    val entidad = LogsAuditoriaEntidad(
      tipo_log = TipoMensajeLogs.error.toString,
      clase_origen = claseOrigen,
      mensaje = s"$mensaje ${throwable.getMessage}",
      fecha_creacion = new Date().getTime
    )

    logsAuditoriaDao.log(entidad)
  }
}
