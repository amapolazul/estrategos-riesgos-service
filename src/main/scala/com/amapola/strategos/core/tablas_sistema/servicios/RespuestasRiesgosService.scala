package com.amapola.strategos.core.tablas_sistema.servicios

import com.amapola.strategos.core.tablas_sistema.http.json.RespuestasRiesgoJson
import com.amapola.strategos.core.tablas_sistema.persistencia.daos.RespuestaRiesgosDao

import scala.concurrent.{ExecutionContext, Future}

trait RespuestasRiesgosService {

  /**
    * Consulta un registro en la base de datos mediante un id enviado por parametro
    * @param id
    * @return
    */
  def traerRespuestasRiesgoPorId(
      id: Long): Future[Option[RespuestasRiesgoJson]]

  /**
    * Crea un registro en la base de datos con la entidad enviada por parametro
    * @param entidad
    * @return
    */
  def crearRespuestasRiesgo(entidad: RespuestasRiesgoJson): Future[Long]

  /**
    * Actualiza un registro de la base de datos un id enviado por parametro y la nueva informacion
    * @param id
    * @param entidad
    * @return Booleano
    */
  def actualizarRespuestasRiesgo(
      id: Long,
      entidad: RespuestasRiesgoJson): Future[Boolean]

  /**
    * Borra un registro de la tabla y retorna el estado de la operación
    * @param id
    * @return
    */
  def borrarRespuestasRiesgo(id: Long): Future[Boolean]

  /***
    * Trae todos los registros de la tabla y los retorna en una lista
    * @return
    */
  def traerRespuestasRiesgo(): Future[List[RespuestasRiesgoJson]]

}

class RespuestasRiesgosServiceImpl(
    respuestasRiesgosDao: RespuestaRiesgosDao)(
    implicit executionContext: ExecutionContext)
    extends RespuestasRiesgosService {

  /**
    * Consulta un registro en la base de datos mediante un id enviado por parametro
    *
    * @param id
    * @return
    */
  override def traerRespuestasRiesgoPorId(
      id: Long): Future[Option[RespuestasRiesgoJson]] = {
    respuestasRiesgosDao
      .traerRespuestaRiesgoPorId(id)
      .map(optCausas => {
        optCausas.map(RespuestasRiesgoJson.fromEntity(_))
      })
  }

  /**
    * Crea un registro en la base de datos con la entidad enviada por parametro
    *
    * @param entidad
    * @return
    */
  override def crearRespuestasRiesgo(
      entidad: RespuestasRiesgoJson): Future[Long] = {
    val entidadDB = RespuestasRiesgoJson.toEntity(entidad)
    respuestasRiesgosDao.crearRespuestaRiesgo(entidadDB)
  }

  /**
    * Actualiza un registro de la base de datos un id enviado por parametro y la nueva informacion
    *
    * @param id
    * @param entidad
    * @return Booleano
    */
  override def actualizarRespuestasRiesgo(
      id: Long,
      entidad: RespuestasRiesgoJson): Future[Boolean] = {
    val entidadDB = RespuestasRiesgoJson.toEntity(entidad)
    respuestasRiesgosDao.actualizarRespuestaRiesgo(id, entidadDB)
  }

  /**
    * Borra un registro de la tabla y retorna el estado de la operación
    *
    * @param id
    * @return
    */
  override def borrarRespuestasRiesgo(id: Long): Future[Boolean] = {
    respuestasRiesgosDao.borrarRespuestaRiesgo(id)
  }

  /** *
    * Trae todos los registros de la tabla y los retorna en una lista
    *
    * @return
    */
  override def traerRespuestasRiesgo()
    : Future[List[RespuestasRiesgoJson]] = {
    respuestasRiesgosDao
      .traerTodasRespuestaRiesgo()
      .map(_.map(RespuestasRiesgoJson.fromEntity(_)).toList)
  }
}
