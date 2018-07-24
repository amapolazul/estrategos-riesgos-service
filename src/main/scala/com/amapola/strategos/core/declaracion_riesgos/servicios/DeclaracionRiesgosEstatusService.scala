package com.amapola.strategos.core.declaracion_riesgos.servicios

import com.amapola.strategos.core.declaracion_riesgos.http.json.EstatusRiesgosJson
import com.amapola.strategos.core.declaracion_riesgos.persistencia.daos.DeclaracionRiesgosEstatusDao

import scala.concurrent.{ExecutionContext, Future}

trait DeclaracionRiesgosEstatusService {

  /**
    * Trae todos los estados disponibles para los ejercicios de evaluación
    * @return
    */
  def traerDeclaracionRiesgosEstatus(): Future[List[EstatusRiesgosJson]]
}

class DeclaracionRiesgosEstatusServiceImpl(
    declaracionResgosEstatusDao: DeclaracionRiesgosEstatusDao)(
    implicit executionContext: ExecutionContext)
    extends DeclaracionRiesgosEstatusService {

  /**
    * Trae todos los estados disponibles para los ejercicios de evaluación
    * @return
    */
  override def traerDeclaracionRiesgosEstatus()
    : Future[List[EstatusRiesgosJson]] = {
    declaracionResgosEstatusDao
      .traerDeclaracionRiesgosEstatus()
      .map(result => {
        result.map(EstatusRiesgosJson.fromEntity(_)).toList
      })
  }
}
