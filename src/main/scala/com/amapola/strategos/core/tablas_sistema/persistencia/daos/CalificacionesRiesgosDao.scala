package com.amapola.strategos.core.tablas_sistema.persistencia.daos

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.CalificacionRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.CalificacionRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait CalificacionesRiesgosDao {

  /**
    * Crea un registro en la tabla calificaciones_riesgo
    * @param entidad
    * @return
    */
  def crearCalificacionRiesgo(entidad: CalificacionRiesgosEntidad): Future[Long]

  /**
    * Consulta un registro de la tabla calificaciones_riesgo por su id
    * @param id
    * @return
    */
  def traerCalificacionRiesgoPorId(
      id: Long): Future[Option[CalificacionRiesgosEntidad]]

  /**
    * Consulta todos los registros de la tabla calificaciones_riesgo
    * @return
    */
  def traerTodasCalificacionRiesgo(): Future[Seq[CalificacionRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla calificaciones_riesgo por su id
    * @param id
    * @param entidadActualizar
    * @return
    */
  def actualizarCalificacionRiesgo(
      id: Long,
      entidadActualizar: CalificacionRiesgosEntidad): Future[Boolean]

  /**
    * Borra un registro de la tabla calificaciones_riesgo por su id
    * @param id
    * @return
    */
  def borrarCalificacionRiesgo(id: Long): Future[Boolean]

  /**
    * Consulta la calificacion de un riesgo dada la severidad
    * @param severidad
    * @return
    */
  def consultarCalifiacionRiesgoPorSeveridad(
      severidad: Long): Future[Option[CalificacionRiesgosEntidad]]

}

class CalificacionesRiesgosDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends CalificacionesRiesgosDao
    with CalificacionRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la tabla causas_riesgos
    *
    * @param entidad
    * @return
    */
  override def crearCalificacionRiesgo(
      entidad: CalificacionRiesgosEntidad): Future[Long] =
    db.run(
      calificacionRiesgos returning calificacionRiesgos
        .map(_.id) += entidad)

  /**
    * Consulta un registro de la tabla causas_riesgos por su id
    *
    * @param id
    * @return
    */
  override def traerCalificacionRiesgoPorId(
      id: Long): Future[Option[CalificacionRiesgosEntidad]] =
    db.run(calificacionRiesgos.filter(_.id === id).result.headOption)

  /**
    * Consulta todos los registros de la tabla causas_riesgos
    *
    * @return
    */
  override def traerTodasCalificacionRiesgo()
    : Future[Seq[CalificacionRiesgosEntidad]] =
    db.run(calificacionRiesgos.result)

  /**
    * Actualiza un registro de la tabla causas_riesgos por su id
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarCalificacionRiesgo(
      id: Long,
      entidad: CalificacionRiesgosEntidad): Future[Boolean] = {
    traerCalificacionRiesgoPorId(id).flatMap {
      case Some(calificacionOld) =>
        val actualizado = entidad.merge(calificacionOld)
        db.run(calificacionRiesgos.filter(_.id === id).map(x => {
          (
            x.nombre_calificacion_riesgo,
            x.color,
            x.rango_minimo,
            x.rango_maximo,
            x.accion_tomar
          )
        }).update((actualizado.nombre_calificacion_riesgo,
          actualizado.color,
          actualizado.rango_minimo,
          actualizado.rango_maximo,
          actualizado.accion_tomar))
        )
          .map(_ == 1)
      case None => Future.successful(false)
    }
  }

  /**
    * Borra un registro de la tabla causas_riesgos por su id
    *
    * @param id
    * @return
    */
  override def borrarCalificacionRiesgo(id: Long): Future[Boolean] =
    db.run(calificacionRiesgos.filter(_.id === id).delete).map(_ == 1)

  /**
    * Consulta la calificacion de un riesgo dada la severidad
    * @param severidad
    * @return
    */
  override def consultarCalifiacionRiesgoPorSeveridad(
      severidad: Long): Future[Option[CalificacionRiesgosEntidad]] = {

    db.run(
      calificacionRiesgos
        .filter(_.rango_maximo >= severidad)
        .filter(_.rango_minimo <= severidad)
        .result
        .headOption)
  }
}
