package com.amapola.strategos.core.declaracion_riesgos.persistencia.daos

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.EfectosDeclaracionRiesgosEntidad
import com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas.EfectosDeclaracionRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait EfectosDeclaracionDao {

  /**
    * Crea un registro en la tabla causas_declaracion_riesgo
    * @param entidad
    * @return
    */
  def crearEfectosDeclaracion(
      entidad: EfectosDeclaracionRiesgosEntidad): Future[Long]

  /**
    * Consulta un registro de la tabla causas_declaracion_riesgo por su id
    * @param id
    * @return
    */
  def consultarEfectoDeclaracionPorId(
      id: Long): Future[Option[EfectosDeclaracionRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla causas_declaracion_riesgo dado un id y la informacion nueva del registro
    * @param id
    * @param entidad
    * @return
    */
  def actualizarEfectoDeclaracionPorId(
      id: Long,
      entidad: EfectosDeclaracionRiesgosEntidad): Future[Boolean]

  /**
    * Retorna todos los registros de la tabla causas_declaracion_riesgo asociados a una declaracion_riesgo
    * @param declaracionRiesgoId
    * @return
    */
  def traerEfectosDeclaracionPorRiesgoId(
      declaracionRiesgoId: Long): Future[Seq[EfectosDeclaracionRiesgosEntidad]]

  /**
    * Borra un registro de la tabla causas_declaracion_riesgo dado un id
    * @param id
    * @return
    */
  def borrarEfectoDeclaracionPorId(id: Long): Future[Boolean]

}

class EfectosDeclaracionDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends EfectosDeclaracionDao
    with EfectosDeclaracionRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la tabla causas_declaracion_riesgo
    *
    * @param entidad
    * @return
    */
  override def crearEfectosDeclaracion(
      entidad: EfectosDeclaracionRiesgosEntidad): Future[Long] = {
    db.run(
      efectosDeclaracionRiesgos returning efectosDeclaracionRiesgos
        .map(_.id) += entidad)
  }

  /**
    * Consulta un registro de la tabla causas_declaracion_riesgo por su id
    *
    * @param id
    * @return
    */
  override def consultarEfectoDeclaracionPorId(
      id: Long): Future[Option[EfectosDeclaracionRiesgosEntidad]] = {
    db.run(efectosDeclaracionRiesgos.filter(_.id === id).result.headOption)
  }

  /**
    * Actualiza un registro de la tabla causas_declaracion_riesgo dado un id y la informacion nueva del registro
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarEfectoDeclaracionPorId(
      id: Long,
      entidad: EfectosDeclaracionRiesgosEntidad): Future[Boolean] = {
    consultarEfectoDeclaracionPorId(id).flatMap(result => {
      result match {
        case Some(causaDecl) =>
          val merge = entidad.merge(causaDecl)
          db.run(efectosDeclaracionRiesgos.update(merge)).map(_ == 1)
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
  override def traerEfectosDeclaracionPorRiesgoId(declaracionRiesgoId: Long)
    : Future[Seq[EfectosDeclaracionRiesgosEntidad]] = {
    db.run(
      efectosDeclaracionRiesgos
        .filter(_.declaracion_riesgo_id === declaracionRiesgoId)
        .result)
  }

  /**
    * Borra un registro de la tabla causas_declaracion_riesgo dado un id
    *
    * @param id
    * @return
    */
  override def borrarEfectoDeclaracionPorId(id: Long): Future[Boolean] = {
    db.run(efectosDeclaracionRiesgos.filter(_.id === id).delete).map(_ == 1)
  }
}
