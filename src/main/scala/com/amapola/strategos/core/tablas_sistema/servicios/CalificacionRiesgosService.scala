package com.amapola.strategos.core.tablas_sistema.servicios

import com.amapola.strategos.core.tablas_sistema.http.json.CalificacionRiesgosJson
import com.amapola.strategos.core.tablas_sistema.persistencia.daos.CalificacionesRiesgosDao

import scala.concurrent.{ExecutionContext, Future}

trait CalificacionRiesgosService {

  /**
    * Consulta un registro en la base de datos mediante un id enviado por parametro
    * @param id
    * @return
    */
  def traerCalificacionRiesgoPorId(
      id: Long): Future[Option[CalificacionRiesgosJson]]

  /**
    * Crea un registro en la base de datos con la entidad enviada por parametro
    * @param entidad
    * @return
    */
  def crearCalificacionRiesgo(entidad: CalificacionRiesgosJson): Future[Long]

  /**
    * Actualiza un registro de la base de datos un id enviado por parametro y la nueva informacion
    * @param id
    * @param entidad
    * @return Booleano
    */
  def actualizarCalificacionRiesgo(
      id: Long,
      entidad: CalificacionRiesgosJson): Future[Boolean]

  /**
    * Borra un registro de la tabla y retorna el estado de la operación
    * @param id
    * @return
    */
  def borrarCalificacionRiesgo(id: Long): Future[Boolean]

  /***
    * Trae todos los registros de la tabla y los retorna en una lista
    * @return
    */
  def traerCalificacionRiesgo(): Future[List[CalificacionRiesgosJson]]

  /**
    * Consulta la calificacion de un riesgo dada la severidad
    * @param severidad
    * @return
    */
  def consultarCalifiacionRiesgoPorSeveridad(
      severidad: Long): Future[Option[CalificacionRiesgosJson]]

}

class CalificacionRiesgosServiceImpl(
    calificacionRiesgosDao: CalificacionesRiesgosDao)(
    implicit executionContext: ExecutionContext)
    extends CalificacionRiesgosService {

  /**
    * Consulta un registro en la base de datos mediante un id enviado por parametro
    *
    * @param id
    * @return
    */
  override def traerCalificacionRiesgoPorId(
      id: Long): Future[Option[CalificacionRiesgosJson]] = {
    calificacionRiesgosDao
      .traerCalificacionRiesgoPorId(id)
      .map(optCausas => {
        optCausas.map(CalificacionRiesgosJson.fromEntity(_))
      })
  }

  /**
    * Crea un registro en la base de datos con la entidad enviada por parametro
    *
    * @param entidad
    * @return
    */
  override def crearCalificacionRiesgo(
      entidad: CalificacionRiesgosJson): Future[Long] = {
    val entidadDB = CalificacionRiesgosJson.toEntity(entidad)
    calificacionRiesgosDao.crearCalificacionRiesgo(entidadDB)
  }

  /**
    * Actualiza un registro de la base de datos un id enviado por parametro y la nueva informacion
    *
    * @param id
    * @param entidad
    * @return Booleano
    */
  override def actualizarCalificacionRiesgo(
      id: Long,
      entidad: CalificacionRiesgosJson): Future[Boolean] = {
    val entidadDB = CalificacionRiesgosJson.toEntity(entidad)
    calificacionRiesgosDao.actualizarCalificacionRiesgo(id, entidadDB)
  }

  /**
    * Borra un registro de la tabla y retorna el estado de la operación
    *
    * @param id
    * @return
    */
  override def borrarCalificacionRiesgo(id: Long): Future[Boolean] = {
    calificacionRiesgosDao.borrarCalificacionRiesgo(id)
  }

  /** *
    * Trae todos los registros de la tabla y los retorna en una lista
    *
    * @return
    */
  override def traerCalificacionRiesgo()
    : Future[List[CalificacionRiesgosJson]] = {
    calificacionRiesgosDao
      .traerTodasCalificacionRiesgo()
      .map(_.map(CalificacionRiesgosJson.fromEntity(_)).toList)
  }

  /**
    * Consulta la calificacion de un riesgo dada la severidad
    *
    * @param severidad
    * @return
    */
  override def consultarCalifiacionRiesgoPorSeveridad(
      severidad: Long): Future[Option[CalificacionRiesgosJson]] = {
    calificacionRiesgosDao
      .consultarCalifiacionRiesgoPorSeveridad(severidad)
      .map(_.map(CalificacionRiesgosJson.fromEntity(_)))
  }
}
