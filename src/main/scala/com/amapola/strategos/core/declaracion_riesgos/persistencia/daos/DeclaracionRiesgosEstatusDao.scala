package com.amapola.strategos.core.declaracion_riesgos.persistencia.daos

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.EstatusRiesgosEntidad
import com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas.DeclaracionRiesgosEstatusTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait DeclaracionRiesgosEstatusDao {

  /**
    * Consulta todos los estados de declaracion_riesgo_estatus
    * @return
    */
  def traerDeclaracionRiesgosEstatus(): Future[Seq[EstatusRiesgosEntidad]]
}

class DeclaracionRiesgosEstatusDaoImpl(
    val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends DeclaracionRiesgosEstatusDao
    with DeclaracionRiesgosEstatusTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Consulta todos los estados de declaracion_riesgo_estatus
    *
    * @return
    */
  override def traerDeclaracionRiesgosEstatus()
    : Future[Seq[EstatusRiesgosEntidad]] =
    db.run(estatusRiesgo.result)

}
