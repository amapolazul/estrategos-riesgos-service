package com.amapola.strategos.core.tablas_sistema.servicios

import com.amapola.strategos.core.tablas_sistema.http.json.EfectividadRiesgosJson
import com.amapola.strategos.core.tablas_sistema.persistencia.daos.EfectividadRiesgosDao

import scala.concurrent.{ExecutionContext, Future}

trait EfectividadRiesgosService {

  /**
    * Consulta un registro en la base de datos mediante un id enviado por parametro
    * @param id
    * @return
    */
  def traerEfectividadRiesgoPorId(
      id: Long): Future[Option[EfectividadRiesgosJson]]

  /**
    * Crea un registro en la base de datos con la entidad enviada por parametro
    * @param entidad
    * @return
    */
  def crearEfectividadRiesgo(entidad: EfectividadRiesgosJson): Future[Long]

  /**
    * Actualiza un registro de la base de datos un id enviado por parametro y la nueva informacion
    * @param id
    * @param entidad
    * @return Booleano
    */
  def actualizarEfectividadRiesgo(
      id: Long,
      entidad: EfectividadRiesgosJson): Future[Boolean]

  /**
    * Borra un registro de la tabla y retorna el estado de la operación
    * @param id
    * @return
    */
  def borrarEfectividadRiesgo(id: Long): Future[Boolean]

  /***
    * Trae todos los registros de la tabla y los retorna en una lista
    * @return
    */
  def traerEfectividadRiesgo(): Future[List[EfectividadRiesgosJson]]

}

class EfectividadRiesgosServiceImpl(
    efectividadRiesgosDao: EfectividadRiesgosDao)(
    implicit executionContext: ExecutionContext)
    extends EfectividadRiesgosService {

  /**
    * Consulta un registro en la base de datos mediante un id enviado por parametro
    *
    * @param id
    * @return
    */
  override def traerEfectividadRiesgoPorId(
      id: Long): Future[Option[EfectividadRiesgosJson]] = {
    efectividadRiesgosDao
      .traerEfectividadRiesgoPorId(id)
      .map(optCausas => {
        optCausas.map(EfectividadRiesgosJson.fromEntity(_))
      })
  }

  /**
    * Crea un registro en la base de datos con la entidad enviada por parametro
    *
    * @param entidad
    * @return
    */
  override def crearEfectividadRiesgo(
      entidad: EfectividadRiesgosJson): Future[Long] = {
    val entidadDB = EfectividadRiesgosJson.toEntity(entidad)
    efectividadRiesgosDao.crearEfectividadRiesgo(entidadDB)
  }

  /**
    * Actualiza un registro de la base de datos un id enviado por parametro y la nueva informacion
    *
    * @param id
    * @param entidad
    * @return Booleano
    */
  override def actualizarEfectividadRiesgo(
      id: Long,
      entidad: EfectividadRiesgosJson): Future[Boolean] = {
    val entidadDB = EfectividadRiesgosJson.toEntity(entidad)
    efectividadRiesgosDao.actualizarEfectividadRiesgo(id, entidadDB)
  }

  /**
    * Borra un registro de la tabla y retorna el estado de la operación
    *
    * @param id
    * @return
    */
  override def borrarEfectividadRiesgo(id: Long): Future[Boolean] = {
    efectividadRiesgosDao.borrarEfectividadRiesgo(id)
  }

  /** *
    * Trae todos los registros de la tabla y los retorna en una lista
    *
    * @return
    */
  override def traerEfectividadRiesgo()
    : Future[List[EfectividadRiesgosJson]] = {
    efectividadRiesgosDao
      .traerTodasEfectividadRiesgo()
      .map(_.map(EfectividadRiesgosJson.fromEntity(_)).toList)
  }
}
