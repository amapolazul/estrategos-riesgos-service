package com.amapola.strategos.core.procesos.persistencia.daos

import com.amapola.strategos.core.procesos.persistencia.entidades.ProcesoCaracterizacionesEntidad
import com.amapola.strategos.core.procesos.persistencia.tablas.CaracterizacionesTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait CaracterizacionDao {

  /**
    * Retorna todas las caracterizaciones
    * @return
    */
  def darCaracterizaciones(): Future[Seq[ProcesoCaracterizacionesEntidad]]

  /**
    * Retorna las caracterizaciones por su Id
    * @param caracterizacionId
    * @return
    */
  def darCaracterizacionPorId(
      caracterizacionId: Long): Future[Option[ProcesoCaracterizacionesEntidad]]

  /**
    * Retorna las caracterizaciones de un proceso por su procesoId
    * @param procesoId
    * @return
    */
  def darCaracterizacionesPorProcesoId(
      procesoId: Long): Future[Seq[ProcesoCaracterizacionesEntidad]]

  /**
    * Agrega un registro a la tabla proceso caracterizacion
    * @param entidad
    * @return
    */
  def crearCaracterizacion(
      entidad: ProcesoCaracterizacionesEntidad): Future[Long]

  /**
    * Actualiza un registro de caracterizacion
    * @param entidad
    * @return
    */
  def actualizarCaracterizacion(
      caracterizacionId: Long,
      entidad: ProcesoCaracterizacionesEntidad): Future[Boolean]

  /**
    * Borra un registro de caracterizacion por su identificador
    * @param caracterizacionId
    * @return
    */
  def borrarCaracterizacionPorId(caracterizacionId: Long): Future[Boolean]
}

class CaracterizacionDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends CaracterizacionDao
    with CaracterizacionesTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Retorna todas las caracterizaciones
    *
    * @return
    */
  override def darCaracterizaciones()
    : Future[Seq[ProcesoCaracterizacionesEntidad]] =
    db.run(caracterizaciones.result)

  /**
    * Retorna las caracterizaciones por su Id
    *
    * @param caracterizacionId
    * @return
    */
  override def darCaracterizacionPorId(caracterizacionId: Long)
    : Future[Option[ProcesoCaracterizacionesEntidad]] =
    db.run(
      caracterizaciones
        .filter(_.caracterizacionId === caracterizacionId)
        .result
        .headOption)

  /**
    * Retorna las caracterizaciones de un proceso por su procesoId
    *
    * @param procesoId
    * @return
    */
  override def darCaracterizacionesPorProcesoId(
      procesoId: Long): Future[Seq[ProcesoCaracterizacionesEntidad]] =
    db.run(
      caracterizaciones
        .filter(_.procesoId === procesoId)
        .result)

  /**
    * Agrega un registro a la tabla proceso caracterizacion
    *
    * @param entidad
    * @return
    */
  override def crearCaracterizacion(
      entidad: ProcesoCaracterizacionesEntidad): Future[Long] =
    db.run(
      caracterizaciones returning caracterizaciones
        .map(_.caracterizacionId) += entidad)

  /**
    * Actualiza un registro de caracterizacion
    *
    * @param entidad
    * @return
    */
  override def actualizarCaracterizacion(
      caracterizacionId: Long,
      entidad: ProcesoCaracterizacionesEntidad): Future[Boolean] = {
    darCaracterizacionPorId(caracterizacionId).flatMap {
      case Some(caracterizacionViejo) =>
        val actualizado = entidad.merge(caracterizacionViejo)
        db.run(
            caracterizaciones
              .filter(_.caracterizacionId === caracterizacionId)
              .update(actualizado))
          .map(_ == 1)
      case None => Future.successful(false)
    }
  }

  /**
    * Borra un registro de caracterizacion por su identificador
    *
    * @param caracterizacionId
    * @return
    */
  override def borrarCaracterizacionPorId(
      caracterizacionId: Long): Future[Boolean] =
    db.run(
        caracterizaciones
          .filter(_.caracterizacionId === caracterizacionId)
          .delete)
      .map(_ == 1)
}
