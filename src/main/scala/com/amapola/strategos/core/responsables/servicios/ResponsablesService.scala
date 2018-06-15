package com.amapola.strategos.core.responsables.servicios

import com.amapola.strategos.core.responsables.http.json.ResponsablesJson
import com.amapola.strategos.core.responsables.persistencia.daos.ResponsablesDao

import scala.concurrent.{ExecutionContext, Future}

trait ResponsablesService {
  /**
    * Retorna la lista completa de responsables
    * @return
    */
  def traerListResponsables() : Future[List[ResponsablesJson]]
}

class ResponsablesServiceImpl(responsablesDao: ResponsablesDao)(
  implicit executionContext: ExecutionContext) extends ResponsablesService {
  /**
    * Retorna la lista completa de responsables
    *
    * @return
    */
  override def traerListResponsables(): Future[List[ResponsablesJson]] = {
    responsablesDao.getResponsables().map(x => {
      x.map(ResponsablesJson.fromEntity(_)).toList
    })
  }
}
