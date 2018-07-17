package com.amapola.strategos.core.declaracion_riesgos.persistencia.daos

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.CausasDeclaracionRiesgosEntidad
import com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas.CausasDeclaracionRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.Future

trait CausasDeclaracionDao {

  /**
    * Crea un registro en la tabla causas_declaracion_riesgo
    * @param entidad
    * @return
    */
  def crearCausasDeclaracionDao(
      entidad: CausasDeclaracionRiesgosEntidad): Future[Long]

  /**
    * Consulta un registro de la tabla causas_declaracion_riesgo por su id
    * @param id
    * @return
    */
  def consultarCausaDeclaracionPorId(
      id: Long): Future[Option[CausasDeclaracionRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla causas_declaracion_riesgo dado un id y la informacion nueva del registro
    * @param id
    * @param entidad
    * @return
    */
  def actualizarCausaDeclaracionPorId(
      id: Long,
      entidad: CausasDeclaracionRiesgosEntidad): Future[Boolean]

  /**
    * Retorna todos los registros de la tabla causas_declaracion_riesgo asociados a una declaracion_riesgo
    * @param declaracionRiesgoId
    * @return
    */
  def traerCausasDeclaracionPorRiesgoId(
      declaracionRiesgoId: Long): Future[Seq[CausasDeclaracionRiesgosEntidad]]

  /**
    * Borra un registro de la tabla causas_declaracion_riesgo dado un id
    * @param id
    * @return
    */
  def borrarCausaDeclaracionPorId(id: Long): Future[Boolean]

}

class CausasDeclaracionDaoImpl(val databaseConnector: DatabaseConnector)
    extends CausasDeclaracionDao
    with CausasDeclaracionRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la tabla causas_declaracion_riesgo
    *
    * @param entidad
    * @return
    */
  override def crearDeclaracionDao(
      entidad: CausasDeclaracionRiesgosEntidad): Future[Long] = {
    db.run(
      causasDeclaracionRiesgos returning causasDeclaracionRiesgos
        .map(_.id) += entidad)
  }

  /**
    * Consulta un registro de la tabla causas_declaracion_riesgo por su id
    *
    * @param id
    * @return
    */
  override def consultarCausaDeclaracionPorId(
      id: Long): Future[Option[CausasDeclaracionRiesgosEntidad]] = {
    db.run(causasDeclaracionRiesgos.filter(_.id === id).result.headOption)
  }

  /**
    * Actualiza un registro de la tabla causas_declaracion_riesgo dado un id y la informacion nueva del registro
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarCausaDeclaracionPorId(
      id: Long,
      entidad: CausasDeclaracionRiesgosEntidad): Future[Boolean] = {
    consultarCausaDeclaracionPorId(id).flatMap(result => {
      result match {
        case Some(causaDecl) =>
          val merge = entidad.merge(causaDecl)
          db.run(causasDeclaracionRiesgos.update(merge)).map(_ == 1)
        case None => Future.successful(false)
      }
    })
  }

  /**
    * Retorna todos los registros de la tabla causas_declaracion_riesgo asociados a una declaracion_riesgo
    *
    * @param declaracionRiesgoId
    * @return
    */
  override def traerCausasDeclaracionPorRiesgoId(declaracionRiesgoId: Long)
    : Future[Seq[CausasDeclaracionRiesgosEntidad]] = {
    db.run(
      causasDeclaracionRiesgos
        .filter(_.declaracion_riesgo_id === declaracionRiesgoId)
        .result)
  }

  /**
    * Borra un registro de la tabla causas_declaracion_riesgo dado un id
    *
    * @param id
    * @return
    */
  override def borrarCausaDeclaracionPorId(id: Long): Future[Boolean] = {
    db.run(causasDeclaracionRiesgos.filter(_.id === id).delete).map(_ == 1)
  }
}
