package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.daos

import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.entidades.EjerciciosEvaluacionRiesgosEntidad
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.tablas.EjerciciosEvaluacionRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait EjerciciosEvaluacionRiesgosDao {

  /**
    * Crea un registro en la table ejercicio_evaluacion_riesgos
    * @param entidad
    * @return
    */
  def crearEjerciciosEvaluacion(
      entidad: EjerciciosEvaluacionRiesgosEntidad): Future[Long]

  /**
    * Borra un registro de la tabla ejercicio_evaluacion_riesgos dado su id
    * @param id
    * @return
    */
  def borrarEjercicioEvaluacionPorId(id: Long): Future[Boolean]

  /**
    * Actualiza un registro dado su id y la entidad con la información a actualizar
    * @param id
    * @param entidad
    * @return
    */
  def actualizarEjercicioEvaluacion(
      id: Long,
      entidad: EjerciciosEvaluacionRiesgosEntidad): Future[Boolean]

  /**
    * Consulta todos los ejercicios evaluacion asociados a un proceso dado el id del proceso
    * @return
    */
  def traerEjerciciosEvaluacionPorProceso(
      procesoId: Long): Future[Seq[EjerciciosEvaluacionRiesgosEntidad]]

  /**
    * Trae un registro de la tabla ejercicio_evaluacion_riesgos dado un id
    * @param id
    * @return
    */
  def traerEjercicioEvaluacionPorId(
      id: Long): Future[Option[EjerciciosEvaluacionRiesgosEntidad]]
}

class EjerciciosEvaluacionRiesgosDaoImpl(
    val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends EjerciciosEvaluacionRiesgosDao
    with EjerciciosEvaluacionRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la table ejercicio_evaluacion_riesgos
    *
    * @param entidad
    * @return
    */
  override def crearEjerciciosEvaluacion(
      entidad: EjerciciosEvaluacionRiesgosEntidad): Future[Long] =
    db.run(
      ejerciciosEvaluacionRiesgos returning ejerciciosEvaluacionRiesgos.map(
        _.id) += entidad)

  /**
    * Borra un registro de la tabla ejercicio_evaluacion_riesgos dado su id
    *
    * @param id
    * @return
    */
  override def borrarEjercicioEvaluacionPorId(id: Long): Future[Boolean] =
    db.run(ejerciciosEvaluacionRiesgos.filter(_.id === id).delete).map(_ == 1)

  /**
    * Actualiza un registro dado su id y la entidad con la información a actualizar
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarEjercicioEvaluacion(
      id: Long,
      entidad: EjerciciosEvaluacionRiesgosEntidad): Future[Boolean] = {

    traerEjercicioEvaluacionPorId(id).flatMap {
      case Some(eval) =>
        val updated = entidad.merge(eval)
        db.run(
            ejerciciosEvaluacionRiesgos
              .filter(_.id === id)
              .map(x => {
                (x.proceso_id,
                 x.estatus_id,
                 x.descripcion,
                 x.fecha_creacion_ejercicio)
              })
              .update((updated.proceso_id,
                      updated.estatus_id,
                      updated.descripcion,
                      updated.fecha_creacion_ejercicio)))
          .map(_ == 1)
      case None => Future.successful(false)
    }

  }

  /**
    * Consulta todos los ejercicios evaluacion asociados a un proceso dado el id del proceso
    *
    * @return
    */
  override def traerEjerciciosEvaluacionPorProceso(
      procesoId: Long): Future[Seq[EjerciciosEvaluacionRiesgosEntidad]] = {
    db.run(
      ejerciciosEvaluacionRiesgos.filter(_.proceso_id === procesoId).result)
  }

  /**
    * Trae un registro de la tabla ejercicio_evaluacion_riesgos dado un id
    *
    * @param id
    * @return
    */
  override def traerEjercicioEvaluacionPorId(
      id: Long): Future[Option[EjerciciosEvaluacionRiesgosEntidad]] = {
    db.run(ejerciciosEvaluacionRiesgos.filter(_.id === id).result.headOption)
  }
}
