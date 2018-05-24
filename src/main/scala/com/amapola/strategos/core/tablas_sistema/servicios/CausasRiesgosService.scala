package com.amapola.strategos.core.tablas_sistema.servicios

import com.amapola.strategos.core.tablas_sistema.http.json.CausasRiesgosJson
import com.amapola.strategos.core.tablas_sistema.persistencia.daos.CausasRiesgosDao

import scala.concurrent.{ExecutionContext, Future}

trait CausasRiesgosService {

  /**
    * Consulta un registro en la base de datos mediante un id enviado por parametro
    * @param id
    * @return
    */
  def traerCausasRiesgoPorId(id: Long): Future[Option[CausasRiesgosJson]]

  /**
    * Crea un registro en la base de datos con la entidad enviada por parametro
    * @param entidad
    * @return
    */
  def crearCausasRiesgo(entidad: CausasRiesgosJson): Future[Long]

  /**
    * Actualiza un registro de la base de datos un id enviado por parametro y la nueva informacion
    * @param id
    * @param entidad
    * @return Booleano
    */
  def actualizarCausasRiesgo(id: Long,
                             entidad: CausasRiesgosJson): Future[Boolean]

  /**
    * Borra un registro de la tabla y retorna el estado de la operación
    * @param id
    * @return
    */
  def borrarCausasRiesgo(id: Long): Future[Boolean]

  /***
    * Trae todos los registros de la tabla y los retorna en una lista
    * @return
    */
  def traerCausasRiesgo(): Future[List[CausasRiesgosJson]]

}

class CausasRiesgosServiceImpl(causasRiesgosDao: CausasRiesgosDao)(
    implicit executionContext: ExecutionContext)
    extends CausasRiesgosService {

  /**
    * Consulta un registro en la base de datos mediante un id enviado por parametro
    *
    * @param id
    * @return
    */
  override def traerCausasRiesgoPorId(
      id: Long): Future[Option[CausasRiesgosJson]] = {
    causasRiesgosDao
      .traerCausasRiesgoPorId(id)
      .map(optCausas => {
        optCausas.map(CausasRiesgosJson.fromEntity(_))
      })
  }

  /**
    * Crea un registro en la base de datos con la entidad enviada por parametro
    *
    * @param entidad
    * @return
    */
  override def crearCausasRiesgo(entidad: CausasRiesgosJson): Future[Long] = {
    val entidadDB = CausasRiesgosJson.toEntity(entidad)
    causasRiesgosDao.crearCausasRiesgo(entidadDB)
  }

  /**
    * Actualiza un registro de la base de datos un id enviado por parametro y la nueva informacion
    *
    * @param id
    * @param entidad
    * @return Booleano
    */
  override def actualizarCausasRiesgo(
      id: Long,
      entidad: CausasRiesgosJson): Future[Boolean] = {
    val entidadDB = CausasRiesgosJson.toEntity(entidad)
    causasRiesgosDao.actualizarCausaRiesgo(id, entidadDB)
  }

  /**
    * Borra un registro de la tabla y retorna el estado de la operación
    *
    * @param id
    * @return
    */
  override def borrarCausasRiesgo(id: Long): Future[Boolean] = {
    causasRiesgosDao.borrarCausaRiesgo(id)
  }

  /** *
    * Trae todos los registros de la tabla y los retorna en una lista
    *
    * @return
    */
  override def traerCausasRiesgo(): Future[List[CausasRiesgosJson]] = {
    causasRiesgosDao
      .traerTodasCausasRiesgo()
      .map(_.map(CausasRiesgosJson.fromEntity(_)).toList)
  }
}
