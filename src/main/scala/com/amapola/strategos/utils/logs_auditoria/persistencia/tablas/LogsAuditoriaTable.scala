package com.amapola.strategos.utils.logs_auditoria.persistencia.tablas

import com.amapola.strategos.utils.db.DatabaseConnector
import com.amapola.strategos.utils.logs_auditoria.persistencia.entidades.LogsAuditoriaEntidad

/**
  * Clase que mapea a la tabla encargada de guardar en base de datos los eventos
  * que ocurren dentro de el módulo de riesgos
  */
trait LogsAuditoriaTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class LogsAuditoria(tag: Tag)
      extends Table[LogsAuditoriaEntidad](tag, "riesgos_logs_auditoria") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def tipo_log = column[String]("tipo_log")
    def clase_origen = column[String]("clase_origen")
    def mensaje = column[String]("mensaje")
    def fecha_creacion = column[Long]("fecha_creacion")

    def * =
      (id.?, tipo_log, clase_origen, mensaje, fecha_creacion) <> ((LogsAuditoriaEntidad.apply _).tupled, LogsAuditoriaEntidad.unapply)
  }

  val logsAuditoria = TableQuery[LogsAuditoria]

}
