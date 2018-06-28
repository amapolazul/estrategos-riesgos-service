package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.servicios

import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.http.json.EjerciciosEvaluacionRiesgosJson
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.daos.EjerciciosEvaluacionRiesgosDao

import scala.concurrent.{ExecutionContext, Future}

trait EjerciciosEvaluacionesRiesgosService {

  /**
    * Crea un registro en la table ejercicio_evaluacion_riesgos
    * @param entidad
    * @return
    */
  def crearEjerciciosEvaluacion(
      entidad: EjerciciosEvaluacionRiesgosJson): Future[Long]

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
      entidad: EjerciciosEvaluacionRiesgosJson): Future[Boolean]

  /**
    * Consulta todos los ejercicios evaluacion asociados a un proceso dado el id del proceso
    * @return
    */
  def traerEjerciciosEvaluacionPorProceso(
      procesoId: Long): Future[Seq[EjerciciosEvaluacionRiesgosJson]]

  /**
    * Trae un registro de la tabla ejercicio_evaluacion_riesgos dado un id
    * @param id
    * @return
    */
  def traerEjercicioEvaluacionPorId(
      id: Long): Future[Option[EjerciciosEvaluacionRiesgosJson]]

}

class EjerciciosEvaluacionesRiesgosServiceImpl(
    ejerciciosEvaluacionRiesgosDao: EjerciciosEvaluacionRiesgosDao)(
    implicit executionContext: ExecutionContext)
    extends EjerciciosEvaluacionesRiesgosService {

  /**
    * Crea un registro en la table ejercicio_evaluacion_riesgos
    *
    * @param json
    * @return
    */
  override def crearEjerciciosEvaluacion(
      json: EjerciciosEvaluacionRiesgosJson): Future[Long] = {
    val entidad = EjerciciosEvaluacionRiesgosJson.toEntity(json)
    ejerciciosEvaluacionRiesgosDao.crearEjerciciosEvaluacion(entidad)
  }

  /**
    * Borra un registro de la tabla ejercicio_evaluacion_riesgos dado su id
    *
    * @param id
    * @return
    */
  override def borrarEjercicioEvaluacionPorId(id: Long): Future[Boolean] = {
    ejerciciosEvaluacionRiesgosDao.borrarEjercicioEvaluacionPorId(id)
  }

  /**
    * Actualiza un registro dado su id y la entidad con la información a actualizar
    *
    * @param id
    * @param json
    * @return
    */
  override def actualizarEjercicioEvaluacion(
      id: Long,
      json: EjerciciosEvaluacionRiesgosJson): Future[Boolean] = {
    val entidad = EjerciciosEvaluacionRiesgosJson.toEntity(json)
    ejerciciosEvaluacionRiesgosDao.actualizarEjercicioEvaluacion(id, entidad)
  }

  /**
    * Consulta todos los ejercicios evaluacion asociados a un proceso dado el id del proceso
    *
    * @return
    */
  override def traerEjerciciosEvaluacionPorProceso(
      procesoId: Long): Future[Seq[EjerciciosEvaluacionRiesgosJson]] = {
    ejerciciosEvaluacionRiesgosDao
      .traerEjerciciosEvaluacionPorProceso(procesoId)
      .map(result => {
        result.map(EjerciciosEvaluacionRiesgosJson.fromEntity(_))
      })
  }

  /**
    * Trae un registro de la tabla ejercicio_evaluacion_riesgos dado un id
    *
    * @param id
    * @return
    */
  override def traerEjercicioEvaluacionPorId(
      id: Long): Future[Option[EjerciciosEvaluacionRiesgosJson]] = {
    ejerciciosEvaluacionRiesgosDao
      .traerEjercicioEvaluacionPorId(id)
      .map(result => {
        result.map(EjerciciosEvaluacionRiesgosJson.fromEntity(_))
      })
  }
}
