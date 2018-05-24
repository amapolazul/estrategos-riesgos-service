package com.amapola.strategos.core.tablas_sistema.servicios

import com.amapola.strategos.core.tablas_sistema.http.json.ImpactoRiesgosJson
import com.amapola.strategos.core.tablas_sistema.persistencia.daos.ImpactoRiesgosDao

import scala.concurrent.{ExecutionContext, Future}

trait ImpactoRiesgosService {

  /**
    * Retorna un registro de la tabla impacto_riesgo por el id enviado por parametro
    * @param id
    * @return
    */
  def traerImpactoRiesgoPorId(id: Long): Future[Option[ImpactoRiesgosJson]]

  /**
    * Crea un registro en la tabla impacto_riesgos dados los datos enviados por parametro
    * @param entidad
    * @return Id del registro generado
    */
  def crearImpactoRiesgos(entidad: ImpactoRiesgosJson): Future[Long]

  /**
    * Devuelve la lista de todos los registros de la tabla impacto_riesgos
    * @return
    */
  def traerImpactoRiesgos(): Future[List[ImpactoRiesgosJson]]

  /**
    * Actualiza un registro de la tabla impacto_riesgos dado un id y la información a actualizar
    * @param id
    * @param entidad
    * @return
    */
  def actualizarImpactoRiesgo(id: Long,
                              entidad: ImpactoRiesgosJson): Future[Boolean]

  /**
    * Borra un registro de la tabla impacto_riesgo dado el id
    * @param id
    * @return
    */
  def borrarImpactoRiesgo(id: Long): Future[Boolean]

}

class ImpactoRiesgosServiceImpl(impactoRiesgosDao: ImpactoRiesgosDao)(
  implicit executionContext: ExecutionContext)
    extends ImpactoRiesgosService {

  /**
    * Retorna un registro de la tabla impacto_riesgo por el id enviado por parametro
    *
    * @param id
    * @return
    */
  override def traerImpactoRiesgoPorId(
      id: Long): Future[Option[ImpactoRiesgosJson]] = {
    impactoRiesgosDao
      .traerImpactoRiesgoPorId(id)
      .map(_.map(ImpactoRiesgosJson.fromEntity(_)))
  }

  /**
    * Crea un registro en la tabla impacto_riesgos dados los datos enviados por parametro
    *
    * @param entidad
    * @return Id del registro generado
    */
  override def crearImpactoRiesgos(
      entidad: ImpactoRiesgosJson): Future[Long] = {
    val entidadDB = ImpactoRiesgosJson.toEntity(entidad)
    impactoRiesgosDao.crearImpactoRiesgos(entidadDB)
  }

  /**
    * Devuelve la lista de todos los registros de la tabla impacto_riesgos
    *
    * @return
    */
  override def traerImpactoRiesgos(): Future[List[ImpactoRiesgosJson]] = {
    impactoRiesgosDao
      .traerImpactosRiesgos()
      .map(_.map(ImpactoRiesgosJson.fromEntity(_)).toList)
  }

  /**
    * Actualiza un registro de la tabla impacto_riesgos dado un id y la información a actualizar
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarImpactoRiesgo(
      id: Long,
      entidad: ImpactoRiesgosJson): Future[Boolean] = {
    val entidadDB = ImpactoRiesgosJson.toEntity(entidad)
    impactoRiesgosDao.actualizarImpactoRiesgos(id, entidadDB)
  }

  /**
    * Borra un registro de la tabla impacto_riesgo dado el id
    *
    * @param id
    * @return
    */
  override def borrarImpactoRiesgo(id: Long): Future[Boolean] = {
    impactoRiesgosDao.borrarImpactoRiesgos(id)
  }
}
