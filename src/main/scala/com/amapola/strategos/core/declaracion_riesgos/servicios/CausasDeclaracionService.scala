package com.amapola.strategos.core.declaracion_riesgos.servicios

import com.amapola.strategos.core.declaracion_riesgos.http.json.CausasDeclaracionRiesgosJson
import com.amapola.strategos.core.declaracion_riesgos.persistencia.daos.CausasDeclaracionDao

import scala.concurrent.{ExecutionContext, Future}

trait CausasDeclaracionService {

  /**
    * Crea una declaracion riesgo a partir de una peticion http
    * @param request
    * @return
    */
  def crearCausaDeclaracionService(
      request: CausasDeclaracionRiesgosJson): Future[Long]

  /**
    * Actualiza un registro de control declaracion dado su id y la informacion enviada por la petion http
    * @param id
    * @param request
    * @return
    */
  def actualizarCausaDeclaracion(
      id: Long,
      request: CausasDeclaracionRiesgosJson): Future[Boolean]

  /**
    * Borra un registro de control declaracion dado su id
    * @param id
    * @return
    */
  def borrarCausaDeclaracion(id: Long): Future[Boolean]

  /**
    * Lista los controles declaracion de un riesgo por el id del riesgo
    * @param riesgoId
    * @return
    */
  def listarCausasDeclaracionPorRiesgoId(
      riesgoId: Long): Future[List[CausasDeclaracionRiesgosJson]]

}

class CausasDeclaracionServiceImpl(causasDeclaracionDao: CausasDeclaracionDao)(
    implicit executionContext: ExecutionContext)
    extends CausasDeclaracionService {

  /**
    * Crea una declaracion riesgo a partir de una peticion http
    *
    * @param request
    * @return
    */
  override def crearCausaDeclaracionService(
      request: CausasDeclaracionRiesgosJson): Future[Long] = {
    val entity = CausasDeclaracionRiesgosJson.toEntity(request)
    causasDeclaracionDao.crearCausasDeclaracion(entity)
  }

  /**
    * Actualiza un registro de control declaracion dado su id y la informacion enviada por la petion http
    *
    * @param id
    * @param request
    * @return
    */
  override def actualizarCausaDeclaracion(
      id: Long,
      request: CausasDeclaracionRiesgosJson): Future[Boolean] = {
    val entity = CausasDeclaracionRiesgosJson.toEntity(request)
    causasDeclaracionDao.actualizarCausaDeclaracionPorId(id, entity)
  }

  /**
    * Borra un registro de control declaracion dado su id
    *
    * @param id
    * @return
    */
  override def borrarCausaDeclaracion(id: Long): Future[Boolean] = {
    causasDeclaracionDao.borrarCausaDeclaracionPorId(id)
  }

  /**
    * Lista los controles declaracion de un riesgo por el id del riesgo
    *
    * @param riesgoId
    * @return
    */
  override def listarCausasDeclaracionPorRiesgoId(
      riesgoId: Long): Future[List[CausasDeclaracionRiesgosJson]] = {
    val result =
      causasDeclaracionDao.traerCausasDeclaracionPorRiesgoId(riesgoId)
    result.map(list => {
      list.map(CausasDeclaracionRiesgosJson.fromEntity(_)).toList
    })
  }
}
