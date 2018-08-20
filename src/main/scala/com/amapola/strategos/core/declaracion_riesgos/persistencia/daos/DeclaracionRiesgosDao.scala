package com.amapola.strategos.core.declaracion_riesgos.persistencia.daos

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.DeclaracionRiesgosEntidad
import com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas.DeclaracionRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait DeclaracionRiesgosDao {

  /**
    * Crea un registro en la tabla declaracion_riesgo
    * @param entidad
    * @return
    */
  def crearDeclaracionRiesgo(entidad: DeclaracionRiesgosEntidad): Future[Long]

  /**
    * Actualiza un registro de la tabla declaracion_riesgo dado un id y la información a actualizar
    * @param id
    * @param entidad
    * @return
    */
  def actualizarDeclaracionRiesgo(
      id: Long,
      entidad: DeclaracionRiesgosEntidad): Future[Boolean]

  /**
    * Elimina un registro de la tabla declaracion_riesgo por su id
    * @param id
    * @return
    */
  def eliminarDeclaracionRiesgo(id: Long): Future[Boolean]

  /**
    * Trae todas los registros declaracion_riesgo cuyo estado sea pendiente
    * @param procesoId
    * @return
    */
  def traerDeclaracionesRiesgoPendientesPorProcesoId(
      procesoId: Long): Future[Seq[DeclaracionRiesgosEntidad]]

  /**
    * Trae todas las declaracion_riesgo de un ejercicio_evaluacion
    * @param ejercicioId
    * @return
    */
  def traerDeclaracionesRiesgoPorEjercicioEvaluacionId(
      ejercicioId: Long): Future[Seq[DeclaracionRiesgosEntidad]]

  /**
    * Traer una declaracion_riesgo por su id
    * @param id
    * @return
    */
  def traerDeclaracionRiesgoPorId(
      id: Long): Future[Option[DeclaracionRiesgosEntidad]]

}

class DeclaracionRiesgosDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends DeclaracionRiesgosDao
    with DeclaracionRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Traer una declaracion_riesgo por su id
    * @param id
    * @return
    */
  override def traerDeclaracionRiesgoPorId(
      id: Long): Future[Option[DeclaracionRiesgosEntidad]] = {
    db.run(declaracionesRiesgos.filter(_.id === id).result.headOption)
  }

  /**
    * Crea un registro en la tabla declaracion_riesgo
    *
    * @param entidad
    * @return
    */
  override def crearDeclaracionRiesgo(
      entidad: DeclaracionRiesgosEntidad): Future[Long] = {
    db.run(
      declaracionesRiesgos returning declaracionesRiesgos.map(_.id) += entidad)
  }

  /**
    * Actualiza un registro de la tabla declaracion_riesgo dado un id y la información a actualizar
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarDeclaracionRiesgo(
      id: Long,
      entidad: DeclaracionRiesgosEntidad): Future[Boolean] = {
    traerDeclaracionRiesgoPorId(id).flatMap(declRiesgo => {
      declRiesgo match {
        case Some(toUpdate) =>
          val merged = entidad.merge(toUpdate)
          db.run(
              declaracionesRiesgos
                .filter(_.id === id)
                .map(x => {
                  (
                    x.tipo_riesgo_id,
                    x.respuesta_riesgo_id,
                    x.estatus_riesgo_id,
                    x.factor_riesgo,
                    x.descripcion,
                    x.efectividad_controles,
                    x.probabilidad,
                    x.historico,
                    x.impacto,
                    x.severidad,
                    x.riesgo_residual,
                    x.fecha_actualizacion
                  )
                })
                .update(
                  (merged.tipo_riesgo_id,
                   merged.respuesta_riesgo_id,
                   merged.estatus_riesgo_id,
                   merged.factor_riesgo,
                   merged.descripcion.getOrElse(""),
                   merged.efectividad_controles,
                   merged.probabilidad,
                   merged.historico,
                   merged.impacto,
                   merged.severidad,
                   merged.riesgo_residual,
                   merged.fecha_actualizacion)))
            .map(_ == 1)

        case None => Future.successful(false)
      }
    })
  }

  /**
    * Elimina un registro de la tabla declaracion_riesgo por su id
    *
    * @param id
    * @return
    */
  override def eliminarDeclaracionRiesgo(id: Long): Future[Boolean] = {
    db.run(declaracionesRiesgos.filter(_.id === id).delete).map(_ == 1)
  }

  /**
    * Trae todas los registros declaracion_riesgo cuyo estado sea pendiente
    *
    * @param procesoId
    * @return
    */
  override def traerDeclaracionesRiesgoPendientesPorProcesoId(
      procesoId: Long): Future[Seq[DeclaracionRiesgosEntidad]] = {
    db.run(
      declaracionesRiesgos
        .filter(_.proceso_id === procesoId)
        .filter(_.tipo_riesgo_id === 1l)
        .result)
  }

  /**
    * Trae todas las declaracion_riesgo de un ejercicio_evaluacion
    *
    * @param ejercicioId
    * @return
    */
  override def traerDeclaracionesRiesgoPorEjercicioEvaluacionId(
      ejercicioId: Long): Future[Seq[DeclaracionRiesgosEntidad]] = {
    db.run(
      declaracionesRiesgos.filter(_.ejercicio_riesgo_id === ejercicioId).result)
  }
}
