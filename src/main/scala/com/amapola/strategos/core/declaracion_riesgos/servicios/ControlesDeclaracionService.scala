package com.amapola.strategos.core.declaracion_riesgos.servicios

import com.amapola.strategos.core.declaracion_riesgos.http.json.ControlesDeclaracionRiesgosJson
import com.amapola.strategos.core.declaracion_riesgos.persistencia.daos.ControlesDeclaracionDao

import scala.concurrent.{ExecutionContext, Future}

trait ControlesDeclaracionService {

  /**
    * Crea una declaracion riesgo a partir de una peticion http
    * @param request
    * @return
    */
  def crearControlDeclaracionService(
      request: ControlesDeclaracionRiesgosJson): Future[Long]

  /**
    * Actualiza un registro de control declaracion dado su id y la informacion enviada por la petion http
    * @param id
    * @param request
    * @return
    */
  def actualizarControlDeclaracion(
      id: Long,
      request: ControlesDeclaracionRiesgosJson): Future[Boolean]

  /**
    * Borra un registro de control declaracion dado su id
    * @param id
    * @return
    */
  def borrarControlDeclaracion(id: Long): Future[Boolean]

  /**
    * Borra los registros de controles de riesgo de una declaracion de riesgo
    * @param riesgoId
    * @return
    */
  def borrarControlesDeclaracionPorRiesgoId(riesgoId: Long): Future[Boolean]

  /**
    * Lista los controles declaracion de un riesgo por el id del riesgo
    * @param riesgoId
    * @return
    */
  def listarControlesDeclaracionPorRiesgoId(
      riesgoId: Long): Future[List[ControlesDeclaracionRiesgosJson]]

}

class ControlesDeclaracionServiceImpl(
    controlesDeclaracionDao: ControlesDeclaracionDao)(
    implicit executionContext: ExecutionContext)
    extends ControlesDeclaracionService {

  /**
    * Crea una declaracion riesgo a partir de una peticion http
    *
    * @param request
    * @return
    */
  override def crearControlDeclaracionService(
      request: ControlesDeclaracionRiesgosJson): Future[Long] = {
    val entity = ControlesDeclaracionRiesgosJson.toEntity(request)
    controlesDeclaracionDao.crearControlesDeclaracion(entity)
  }

  /**
    * Actualiza un registro de control declaracion dado su id y la informacion enviada por la petion http
    *
    * @param id
    * @param request
    * @return
    */
  override def actualizarControlDeclaracion(
      id: Long,
      request: ControlesDeclaracionRiesgosJson): Future[Boolean] = {
    val entity = ControlesDeclaracionRiesgosJson.toEntity(request)
    controlesDeclaracionDao.actualizarControlDeclaracionPorId(id, entity)
  }

  /**
    * Borra un registro de control declaracion dado su id
    *
    * @param id
    * @return
    */
  override def borrarControlDeclaracion(id: Long): Future[Boolean] = {
    controlesDeclaracionDao.borrarControlDeclaracionPorId(id)
  }

  /**
    * Lista los controles declaracion de un riesgo por el id del riesgo
    *
    * @param riesgoId
    * @return
    */
  override def listarControlesDeclaracionPorRiesgoId(
      riesgoId: Long): Future[List[ControlesDeclaracionRiesgosJson]] = {
    val result =
      controlesDeclaracionDao.traerControlesDeclaracionPorRiesgoId(riesgoId)
    result.map(list => {
      list.map(ControlesDeclaracionRiesgosJson.fromEntity(_)).toList
    })
  }

  /**
    * Borra los registros de controles de riesgo de una declaracion de riesgo
    *
    * @param riesgoId
    * @return
    */
  override def borrarControlesDeclaracionPorRiesgoId(
      riesgoId: Long): Future[Boolean] = {
    controlesDeclaracionDao.borrarControlesDeclaracionRiesgoPorRiesgoId(
      riesgoId)
  }
}
