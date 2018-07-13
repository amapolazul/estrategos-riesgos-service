package com.amapola.strategos.core.tablas_sistema.persistencia.daos

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.RespuestasRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.RespuestasRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait RespuestaRiesgosDao {

  /**
    * Crea un registro en la tabla respuesta_riesgo
    * @param entidad
    * @return
    */
  def crearRespuestaRiesgo(entidad: RespuestasRiesgosEntidad): Future[Long]

  /**
    * Consulta un registro de la tabla respuesta_riesgo por su id
    * @param id
    * @return
    */
  def traerRespuestaRiesgoPorId(
      id: Long): Future[Option[RespuestasRiesgosEntidad]]

  /**
    * Consulta todos los registros de la tabla respuesta_riesgo
    * @return
    */
  def traerTodasRespuestaRiesgo(): Future[Seq[RespuestasRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla respuesta_riesgo por su id
    * @param id
    * @param entidadActualizar
    * @return
    */
  def actualizarRespuestaRiesgo(
      id: Long,
      entidadActualizar: RespuestasRiesgosEntidad): Future[Boolean]

  /**
    * Borra un registro de la tabla respuesta_riesgo por su id
    * @param id
    * @return
    */
  def borrarRespuestaRiesgo(id: Long): Future[Boolean]

}

class RespuestaRiesgosDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends RespuestaRiesgosDao
    with RespuestasRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la tabla efectividad_riesg
    *
    * @param entidad
    * @return
    */
  override def crearRespuestaRiesgo(
      entidad: RespuestasRiesgosEntidad): Future[Long] =
    db.run(
      respuestasRiesgos returning respuestasRiesgos
        .map(_.id) += entidad)

  /**
    * Consulta un registro de la tabla efectividad_riesgo por su id
    *
    * @param id
    * @return
    */
  override def traerRespuestaRiesgoPorId(
      id: Long): Future[Option[RespuestasRiesgosEntidad]] =
    db.run(respuestasRiesgos.filter(_.id === id).result.headOption)

  /**
    * Consulta todos los registros de la tabla efectividad_riesg
    *
    * @return
    */
  override def traerTodasRespuestaRiesgo()
    : Future[Seq[RespuestasRiesgosEntidad]] =
    db.run(respuestasRiesgos.result)

  /**
    * Actualiza un registro de la tabla efectividad_riesgo por su id
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarRespuestaRiesgo(
      id: Long,
      entidad: RespuestasRiesgosEntidad): Future[Boolean] = {
    traerRespuestaRiesgoPorId(id).flatMap {
      case Some(respuestasOld) =>
        val actualizado = entidad.merge(respuestasOld)
        db.run(respuestasRiesgos.filter(_.id === id).update(actualizado))
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
  override def borrarRespuestaRiesgo(id: Long): Future[Boolean] =
    db.run(respuestasRiesgos.filter(_.id === id).delete).map(_ == 1)

}
