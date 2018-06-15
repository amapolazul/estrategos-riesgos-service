package com.amapola.strategos.core.responsables.persistencia.daos

import com.amapola.strategos.core.responsables.persistencia.entidades.ResponsablesEntidad
import com.amapola.strategos.core.responsables.persistencia.tablas.ResponsablesTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait ResponsablesDao {

  /**
    * Retorna todos los registros de responsables
    * @return
    */
  def getResponsables(): Future[Seq[ResponsablesEntidad]]
}

class ResponsablesDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends ResponsablesDao
    with ResponsablesTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Retorna todos los registros de responsables
    *
    * @return
    */
  override def getResponsables(): Future[Seq[ResponsablesEntidad]] = {
    db.run(responsables.result)
  }
}
