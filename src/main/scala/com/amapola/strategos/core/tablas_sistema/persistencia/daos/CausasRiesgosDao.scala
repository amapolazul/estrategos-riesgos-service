package com.amapola.strategos.core.tablas_sistema.persistencia.daos

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.CausasRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.CausasRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait CausasRiesgosDao {

  /**
    * Crea un registro en la tabla causas_riesgos
    * @param entidad
    * @return
    */
  def crearCausasRiesgo(entidad: CausasRiesgosEntidad): Future[Long]

  /**
    * Consulta un registro de la tabla causas_riesgos por su id
    * @param id
    * @return
    */
  def traerCausasRiesgoPorId(id: Long): Future[Option[CausasRiesgosEntidad]]

  /**
    * Consulta todos los registros de la tabla causas_riesgos
    * @return
    */
  def traerTodasCausasRiesgo(): Future[Seq[CausasRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla causas_riesgos por su id
    * @param id
    * @param entidadActualizar
    * @return
    */
  def actualizarCausaRiesgo(
      id: Long,
      entidadActualizar: CausasRiesgosEntidad): Future[Boolean]

  /**
    * Borra un registro de la tabla causas_riesgos por su id
    * @param id
    * @return
    */
  def borrarCausaRiesgo(id: Long): Future[Boolean]

}

class CausasRiesgosDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends CausasRiesgosDao
    with CausasRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la tabla causas_riesgos
    *
    * @param entidad
    * @return
    */
  override def crearCausasRiesgo(entidad: CausasRiesgosEntidad): Future[Long] =
    db.run(
      causasRiesgos returning causasRiesgos
        .map(_.id) += entidad)

  /**
    * Consulta un registro de la tabla causas_riesgos por su id
    *
    * @param id
    * @return
    */
  override def traerCausasRiesgoPorId(
      id: Long): Future[Option[CausasRiesgosEntidad]] =
    db.run(causasRiesgos.filter(_.id === id).result.headOption)

  /**
    * Consulta todos los registros de la tabla causas_riesgos
    *
    * @return
    */
  override def traerTodasCausasRiesgo(): Future[Seq[CausasRiesgosEntidad]] =
    db.run(causasRiesgos.result)

  /**
    * Actualiza un registro de la tabla causas_riesgos por su id
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarCausaRiesgo(
      id: Long,
      entidad: CausasRiesgosEntidad): Future[Boolean] = {
    traerCausasRiesgoPorId(id).flatMap {
      case Some(causasOld) =>
        val actualizado = entidad.merge(causasOld)
        db.run(causasRiesgos.filter(_.id === id).map(x => {
          (
            x.causa_riesgo,
            x.descripcion
          )
        }).update((
            actualizado.causa_riesgo,
            actualizado.descripcion.getOrElse("")
          ))).map(_ == 1)
      case None => Future.successful(false)
    }
  }

  /**
    * Borra un registro de la tabla causas_riesgos por su id
    *
    * @param id
    * @return
    */
  override def borrarCausaRiesgo(id: Long): Future[Boolean] =
    db.run(causasRiesgos.filter(_.id === id).delete).map(_ == 1)
}
