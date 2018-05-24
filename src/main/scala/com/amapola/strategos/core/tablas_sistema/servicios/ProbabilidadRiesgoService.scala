package com.amapola.strategos.core.tablas_sistema.servicios

import com.amapola.strategos.core.tablas_sistema.http.json.ProbabilidadRiesgosJson
import com.amapola.strategos.core.tablas_sistema.persistencia.daos.ProbabilidadRiesgosDao

import scala.concurrent.{ExecutionContext, Future}

trait ProbabilidadRiesgoService {

  /**
    * Retorna un registro de la tabla probabilidad_riesgo por el id enviado por parametro
    * @param id
    * @return
    */
  def traerProbabilidadRiesgosPorId(
      id: Long): Future[Option[ProbabilidadRiesgosJson]]

  /**
    * Crea un registro en la tabla probabilidad_riesgos dados los datos enviados por parametro
    * @param entidad
    * @return Id del registro generado
    */
  def crearProbabilidadRiesgos(entidad: ProbabilidadRiesgosJson): Future[Long]

  /**
    * Devuelve la lista de todos los registros de la tabla probabilidad_riesgos
    * @return|
    */
  def traerProbabilidadRiesgos(): Future[List[ProbabilidadRiesgosJson]]

  /**
    * Actualiza un registro de la tabla probabilidad_riesgos dado un id y la información a actualizar
    * @param id
    * @param entidad
    * @return
    */
  def actualizarProbabilidadRiesgos(
      id: Long,
      entidad: ProbabilidadRiesgosJson): Future[Boolean]

  /**
    * Borra un registro de la tabla probabilidad_riesgo dado el id
    * @param id
    * @return
    */
  def borrarProbabilidadRiesgos(id: Long): Future[Boolean]

}

class ProbabilidadRiesgoServiceImpl(
    probabilidadRiesgosDao: ProbabilidadRiesgosDao)(
    implicit executionContext: ExecutionContext)
    extends ProbabilidadRiesgoService {

  /**
    * Retorna un registro de la tabla probabilidad_riesgo por el id enviado por parametro
    *
    * @param id
    * @return
    */
  override def traerProbabilidadRiesgosPorId(
      id: Long): Future[Option[ProbabilidadRiesgosJson]] = {
    probabilidadRiesgosDao
      .traerProabilidadRiesgoPorId(id)
      .map(_.map(ProbabilidadRiesgosJson.fromEntity(_)))
  }

  /**
    * Crea un registro en la tabla probabilidad_riesgos dados los datos enviados por parametro
    *
    * @param entidad
    * @return Id del registro generado
    */
  override def crearProbabilidadRiesgos(
      entidad: ProbabilidadRiesgosJson): Future[Long] = {
    val entidadDB = ProbabilidadRiesgosJson.toEntity(entidad)
    probabilidadRiesgosDao.crearProbabilidadRiesgos(entidadDB)
  }

  /**
    * Devuelve la lista de todos los registros de la tabla probabilidad_riesgos
    *
    * @return |
    */
  override def traerProbabilidadRiesgos()
    : Future[List[ProbabilidadRiesgosJson]] = {
    probabilidadRiesgosDao
      .traerProbabilidadesRiesgos()
      .map(_.map(ProbabilidadRiesgosJson.fromEntity(_)).toList),
  }

  /**
    * Actualiza un registro de la tabla probabilidad_riesgos dado un id y la información a actualizar
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarProbabilidadRiesgos(
      id: Long,
      entidad: ProbabilidadRiesgosJson): Future[Boolean] = {
    val entidadDB = ProbabilidadRiesgosJson.toEntity(entidad)
    probabilidadRiesgosDao.actualizarProbabilidadRiesgo(id, entidadDB)
  }

  /**
    * Borra un registro de la tabla probabilidad_riesgo dado el id
    *
    * @param id
    * @return
    */
  override def borrarProbabilidadRiesgos(id: Long): Future[Boolean] = {
    probabilidadRiesgosDao.borrarProbabilidadRiesgo(id)
  }
}
