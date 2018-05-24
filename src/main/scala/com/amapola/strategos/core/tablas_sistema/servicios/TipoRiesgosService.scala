package com.amapola.strategos.core.tablas_sistema.servicios

import com.amapola.strategos.core.tablas_sistema.http.json.TipoRiesgosJson
import com.amapola.strategos.core.tablas_sistema.persistencia.daos.TipoRiesgosDao

import scala.concurrent.{ExecutionContext, Future}

trait TipoRiesgosService {

  /**
    * Retorna un registro de la tabla probabilidad_riesgo por el id enviado por parametro
    * @param id
    * @return
    */
  def traerTipoRiesgosPorId(id: Long): Future[Option[TipoRiesgosJson]]

  /**
    * Crea un registro en la tabla probabilidad_riesgos dados los datos enviados por parametro
    * @param entidad
    * @return Id del registro generado
    */
  def crearTipoRiesgos(entidad: TipoRiesgosJson): Future[Long]

  /**
    * Devuelve la lista de todos los registros de la tabla probabilidad_riesgos
    * @return|
    */
  def traerTipoRiesgos(): Future[List[TipoRiesgosJson]]

  /**
    * Actualiza un registro de la tabla probabilidad_riesgos dado un id y la información a actualizar
    * @param id
    * @param entidad
    * @return
    */
  def actualizarTipoRiesgos(id: Long, entidad: TipoRiesgosJson): Future[Boolean]

  /**
    * Borra un registro de la tabla probabilidad_riesgo dado el id
    * @param id
    * @return
    */
  def borrarTipoRiesgos(id: Long): Future[Boolean]

}

class TipoRiesgoServiceImpl(TipoRiesgosDao: TipoRiesgosDao)(
    implicit executionContext: ExecutionContext)
    extends TipoRiesgosService {

  /**
    * Retorna un registro de la tabla probabilidad_riesgo por el id enviado por parametro
    *
    * @param id
    * @return
    */
  override def traerTipoRiesgosPorId(
      id: Long): Future[Option[TipoRiesgosJson]] = {
    TipoRiesgosDao
      .traerTipoRiesgoPorId(id)
      .map(_.map(TipoRiesgosJson.fromEntity(_)))
  }

  /**
    * Crea un registro en la tabla probabilidad_riesgos dados los datos enviados por parametro
    *
    * @param entidad
    * @return Id del registro generado
    */
  override def crearTipoRiesgos(entidad: TipoRiesgosJson): Future[Long] = {
    val entidadDB = TipoRiesgosJson.toEntity(entidad)
    TipoRiesgosDao.crearTipoRiesgo(entidadDB)
  }

  /**
    * Devuelve la lista de todos los registros de la tabla probabilidad_riesgos
    *
    * @return |
    */
  override def traerTipoRiesgos(): Future[List[TipoRiesgosJson]] = {
    TipoRiesgosDao
      .traerTiposRiesgo()
      .map(_.map(TipoRiesgosJson.fromEntity(_)).toList)
  }

  /**
    * Actualiza un registro de la tabla probabilidad_riesgos dado un id y la información a actualizar
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarTipoRiesgos(
      id: Long,
      entidad: TipoRiesgosJson): Future[Boolean] = {
    val entidadDB = TipoRiesgosJson.toEntity(entidad)
    TipoRiesgosDao.actualizarTiposRiesgo(id, entidadDB)
  }

  /**
    * Borra un registro de la tabla probabilidad_riesgo dado el id
    *
    * @param id
    * @return
    */
  override def borrarTipoRiesgos(id: Long): Future[Boolean] = {
    TipoRiesgosDao.borrarTipoRegistro(id)
  }
}
