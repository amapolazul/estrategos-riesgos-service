package com.amapola.strategos.core.tablas_sistema.persistencia.daos

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.TipoRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.TipoRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait TipoRiesgosDao {

  /**
    * Crea un registro en al tabla tipo_riesgo
    * @param entidad
    * @return
    */
  def crearTipoRiesgo(entidad: TipoRiesgosEntidad): Future[Long]

  /**
    * Trae un registro de la tabla tipo_riesgos por su id
    * @param id
    * @return
    */
  def traerTipoRiesgoPorId(id: Long): Future[Option[TipoRiesgosEntidad]]

  /**
    * Devuelve todos los registros de la tabla tipo_riesgo
    * @return
    */
  def traerTiposRiesgo(): Future[Seq[TipoRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla tipo_riesgo por su id
    * @param id
    * @param entidad
    * @return
    */
  def actualizarTiposRiesgo(id: Long,
                            entidad: TipoRiesgosEntidad): Future[Boolean]

  /**
    * Borra un registro de la tabla tipo_riesgo por su id
    * @param id
    * @return
    */
  def borrarTipoRegistro(id: Long): Future[Boolean]

}

class TipoRiesgosDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends TipoRiesgosDao
    with TipoRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en al tabla tipo_riesgo
    *
    * @param entidad
    * @return
    */
  override def crearTipoRiesgo(entidad: TipoRiesgosEntidad): Future[Long] =
    db.run(tiposRiesgos returning tiposRiesgos.map(_.id) += entidad)

  /**
    * Trae un registro de la tabla tipo_riesgos por su id
    *
    * @param id
    * @return
    */
  override def traerTipoRiesgoPorId(
      id: Long): Future[Option[TipoRiesgosEntidad]] =
    db.run(tiposRiesgos.filter(_.id === id).result.headOption)

  /**
    * Devuelve todos los registros de la tabla tipo_riesgo
    *
    * @return
    */
  override def traerTiposRiesgo(): Future[Seq[TipoRiesgosEntidad]] =
    db.run(tiposRiesgos.result)

  /**
    * Actualiza un registro de la tabla tipo_riesgo por su id
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarTiposRiesgo(
      id: Long,
      entidad: TipoRiesgosEntidad): Future[Boolean] = {
    traerTipoRiesgoPorId(id) flatMap {
      case Some(old) =>
        val actualizado = entidad.merge(old)
        db.run(tiposRiesgos.filter(_.id === id).update(actualizado)).map(_ == 1)
      case None =>
        Future.successful(false)
    }
  }

  /**
    * Borra un registro de la tabla tipo_riesgo por su id
    *
    * @param id
    * @return
    */
  override def borrarTipoRegistro(id: Long): Future[Boolean] =
    db.run(tiposRiesgos.filter(_.id === id).delete).map(_ == 1)
}
