package com.amapola.strategos.core.declaracion_riesgos.servicios

import com.amapola.strategos.core.declaracion_riesgos.http.json.EfectosDeclaracionRiesgosJson
import com.amapola.strategos.core.declaracion_riesgos.persistencia.daos.EfectosDeclaracionDao

import scala.concurrent.{ExecutionContext, Future}

trait EfectosDeclaracionService {

  /**
    * Crea una declaracion riesgo a partir de una peticion http
    * @param request
    * @return
    */
  def crearEfectoDeclaracionService(
      request: EfectosDeclaracionRiesgosJson): Future[Long]

  /**
    * Actualiza un registro de control declaracion dado su id y la informacion enviada por la petion http
    * @param id
    * @param request
    * @return
    */
  def actualizarEfectoDeclaracion(
      id: Long,
      request: EfectosDeclaracionRiesgosJson): Future[Boolean]

  /**
    * Borra un registro de control declaracion dado su id
    * @param id
    * @return
    */
  def borrarEfectoDeclaracion(id: Long): Future[Boolean]

  /**
    * Lista los controles declaracion de un riesgo por el id del riesgo
    * @param riesgoId
    * @return
    */
  def listarEfectosDeclaracionPorRiesgoId(
      riesgoId: Long): Future[List[EfectosDeclaracionRiesgosJson]]

}

class EfectosDeclaracionServiceImpl(
    controlesDeclaracionDao: EfectosDeclaracionDao)(
    implicit executionContext: ExecutionContext)
    extends EfectosDeclaracionService {

  /**
    * Crea una declaracion riesgo a partir de una peticion http
    *
    * @param request
    * @return
    */
  override def crearEfectoDeclaracionService(
      request: EfectosDeclaracionRiesgosJson): Future[Long] = {
    val entity = EfectosDeclaracionRiesgosJson.toEntity(request)
    controlesDeclaracionDao.crearEfectosDeclaracion(entity)
  }

  /**
    * Actualiza un registro de control declaracion dado su id y la informacion enviada por la petion http
    *
    * @param id
    * @param request
    * @return
    */
  override def actualizarEfectoDeclaracion(
      id: Long,
      request: EfectosDeclaracionRiesgosJson): Future[Boolean] = {
    val entity = EfectosDeclaracionRiesgosJson.toEntity(request)
    controlesDeclaracionDao.actualizarEfectoDeclaracionPorId(id, entity)
  }

  /**
    * Borra un registro de control declaracion dado su id
    *
    * @param id
    * @return
    */
  override def borrarEfectoDeclaracion(id: Long): Future[Boolean] = {
    controlesDeclaracionDao.borrarEfectoDeclaracionPorId(id)
  }

  /**
    * Lista los controles declaracion de un riesgo por el id del riesgo
    *
    * @param riesgoId
    * @return
    */
  override def listarEfectosDeclaracionPorRiesgoId(
      riesgoId: Long): Future[List[EfectosDeclaracionRiesgosJson]] = {
    val result =
      controlesDeclaracionDao.traerEfectosDeclaracionPorRiesgoId(riesgoId)
    result.map(list => {
      list.map(EfectosDeclaracionRiesgosJson.fromEntity(_)).toList
    })
  }
}
