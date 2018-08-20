package com.amapola.strategos.utils.logs_auditoria.persistencia.daos

import com.amapola.strategos.utils.db.DatabaseConnector
import com.amapola.strategos.utils.logs_auditoria.persistencia.entidades.LogsAuditoriaEntidad
import com.amapola.strategos.utils.logs_auditoria.persistencia.tablas.LogsAuditoriaTable
import org.apache.logging.log4j.scala.Logging
import org.slf4j.Logger

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

trait LogsAuditoriaDao {

  /**
    * Guarda mensajes informativos
    * @return
    */
  def log(entidad: LogsAuditoriaEntidad): Unit

}

class LogsAuditoriaDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends LogsAuditoriaDao
    with LogsAuditoriaTable with Logging {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Guarda mensajes informativos
    *
    * @return
    */
  override def log(entidad: LogsAuditoriaEntidad): Unit = {
    db.run(logsAuditoria returning logsAuditoria.map(_.id) += entidad) onComplete {
      case Success(_) => logger.info(s"${entidad.toString} guardado correctamente")
      case Failure(ex) => logger.error(s"${entidad.toString} ha fallado.", ex)
    }
  }
}
