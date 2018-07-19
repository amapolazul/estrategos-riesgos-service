package com.amapola.strategos.core.declaracion_riesgos.persistencia.daos

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.ControlesDeclaracionRiesgosEntidad
import com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas.ControlesDeclaracionRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait ControlesDeclaracionDao {

  /**
    * Crea un registro en la tabla causas_declaracion_riesgo
    * @param entidad
    * @return
    */
  def crearControlesDeclaracion(
      entidad: ControlesDeclaracionRiesgosEntidad): Future[Long]

  /**
    * Consulta un registro de la tabla causas_declaracion_riesgo por su id
    * @param id
    * @return
    */
  def consultarControlDeclaracionPorId(
      id: Long): Future[Option[ControlesDeclaracionRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla causas_declaracion_riesgo dado un id y la informacion nueva del registro
    * @param id
    * @param entidad
    * @return
    */
  def actualizarControlDeclaracionPorId(
      id: Long,
      entidad: ControlesDeclaracionRiesgosEntidad): Future[Boolean]

  /**
    * Retorna todos los registros de la tabla causas_declaracion_riesgo asociados a una declaracion_riesgo
    * @param declaracionRiesgoId
    * @return
    */
  def traerControlesDeclaracionPorRiesgoId(declaracionRiesgoId: Long)
    : Future[Seq[ControlesDeclaracionRiesgosEntidad]]

  /**
    * Borra un registro de la tabla causas_declaracion_riesgo dado un id
    * @param id
    * @return
    */
  def borrarControlDeclaracionPorId(id: Long): Future[Boolean]

}

class ControlesDeclaracionDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends ControlesDeclaracionDao
    with ControlesDeclaracionRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la tabla causas_declaracion_riesgo
    *
    * @param entidad
    * @return
    */
  override def crearControlesDeclaracion(
      entidad: ControlesDeclaracionRiesgosEntidad): Future[Long] = {
    db.run(
      controlDeclaracionRiesgos returning controlDeclaracionRiesgos
        .map(_.id) += entidad)
  }

  /**
    * Consulta un registro de la tabla causas_declaracion_riesgo por su id
    *
    * @param id
    * @return
    */
  override def consultarControlDeclaracionPorId(
      id: Long): Future[Option[ControlesDeclaracionRiesgosEntidad]] = {
    db.run(controlDeclaracionRiesgos.filter(_.id === id).result.headOption)
  }

  /**
    * Actualiza un registro de la tabla causas_declaracion_riesgo dado un id y la informacion nueva del registro
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarControlDeclaracionPorId(
      id: Long,
      entidad: ControlesDeclaracionRiesgosEntidad): Future[Boolean] = {
    consultarControlDeclaracionPorId(id).flatMap(result => {
      result match {
        case Some(causaDecl) =>
          val merge = entidad.merge(causaDecl)
          db.run(controlDeclaracionRiesgos.update(merge)).map(_ == 1)
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
  override def traerControlesDeclaracionPorRiesgoId(declaracionRiesgoId: Long)
    : Future[Seq[ControlesDeclaracionRiesgosEntidad]] = {
    db.run(
      controlDeclaracionRiesgos
        .filter(_.declaracion_riesgo_id === declaracionRiesgoId)
        .result)
  }

  /**
    * Borra un registro de la tabla causas_declaracion_riesgo dado un id
    *
    * @param id
    * @return
    */
  override def borrarControlDeclaracionPorId(id: Long): Future[Boolean] = {
    db.run(controlDeclaracionRiesgos.filter(_.id === id).delete).map(_ == 1)
  }
}
