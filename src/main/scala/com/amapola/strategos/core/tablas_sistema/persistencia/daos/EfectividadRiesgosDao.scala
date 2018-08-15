package com.amapola.strategos.core.tablas_sistema.persistencia.daos

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.EfectividadRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.EfectividadRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait EfectividadRiesgosDao {

  /**
    * Crea un registro en la tabla efectividad_riesgos
    * @param entidad
    * @return
    */
  def crearEfectividadRiesgo(entidad: EfectividadRiesgosEntidad): Future[Long]

  /**
    * Consulta un registro de la tabla efectividad_riesgos por su id
    * @param id
    * @return
    */
  def traerEfectividadRiesgoPorId(id: Long): Future[Option[EfectividadRiesgosEntidad]]

  /**
    * Consulta todos los registros de la tabla efectividad_riesgos
    * @return
    */
  def traerTodasEfectividadRiesgo(): Future[Seq[EfectividadRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla efectividad_riesgos por su id
    * @param id
    * @param entidadActualizar
    * @return
    */
  def actualizarEfectividadRiesgo(
                             id: Long,
                             entidadActualizar: EfectividadRiesgosEntidad): Future[Boolean]

  /**
    * Borra un registro de la tabla efectividad_riesgos por su id
    * @param id
    * @return
    */
  def borrarEfectividadRiesgo(id: Long): Future[Boolean]
}

class EfectividadRiesgosDaoImpl (val databaseConnector: DatabaseConnector)(
  implicit executionContext: ExecutionContext)
  extends EfectividadRiesgosDao
    with EfectividadRiesgosTable {
  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la tabla efectividad_riesg
    *
    * @param entidad
    * @return
    */
  override def crearEfectividadRiesgo(entidad: EfectividadRiesgosEntidad): Future[Long] =
    db.run(
      efectividadRiesgos returning efectividadRiesgos
        .map(_.id) += entidad)

  /**
    * Consulta un registro de la tabla efectividad_riesgo por su id
    *
    * @param id
    * @return
    */
  override def traerEfectividadRiesgoPorId(
                                       id: Long): Future[Option[EfectividadRiesgosEntidad]] =
    db.run(efectividadRiesgos.filter(_.id === id).result.headOption)

  /**
    * Consulta todos los registros de la tabla efectividad_riesg
    *
    * @return
    */
  override def traerTodasEfectividadRiesgo(): Future[Seq[EfectividadRiesgosEntidad]] =
    db.run(efectividadRiesgos.result)

  /**
    * Actualiza un registro de la tabla efectividad_riesgo por su id
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarEfectividadRiesgo(
                                      id: Long,
                                      entidad: EfectividadRiesgosEntidad): Future[Boolean] = {
    traerEfectividadRiesgoPorId(id).flatMap {
      case Some(efectividad) =>
        val actualizado = entidad.merge(efectividad)
        db.run(efectividadRiesgos.filter(_.id === id).
          map(x => {
            (
              x.efectividad_nombre,
              x.puntaje,
              x.descripcion
            )
          })
          update((
            actualizado.efectividad_nombre,
            actualizado.puntaje,
            actualizado.descripcion.getOrElse("")
          )))
          .map(_ == 1)
      case None => Future.successful(false)
    }
  }

  /**
    * Borra un registro de la tabla efectividad_riesgo por su id
    *
    * @param id
    * @return
    */
  override def borrarEfectividadRiesgo(id: Long): Future[Boolean] =
    db.run(efectividadRiesgos.filter(_.id === id).delete).map(_ == 1)
}
