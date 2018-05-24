package com.amapola.strategos.core.tablas_sistema.persistencia.daos

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.ProbabilidadRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.ProbabilidadRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait ProbabilidadRiesgosDao {

  /**
    * Crea un registro en la tabla probabilidad_riesgo
    * @param entidad
    * @return
    */
  def crearProbabilidadRiesgos(
      entidad: ProbabilidadRiesgosEntidad): Future[Long]

  /**
    * Trae un registro de la tabla probabilidad_riesgos por su id
    * @param id
    * @return
    */
  def traerProabilidadRiesgoPorId(
      id: Long): Future[Option[ProbabilidadRiesgosEntidad]]

  /**
    * Trae todos los registros de la tabla probabilidad_riesgos
    * @return
    */
  def traerProbabilidadesRiesgos(): Future[Seq[ProbabilidadRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla probabilidad_riesgos por su id
    * @param id
    * @param entidad
    * @return
    */
  def actualizarProbabilidadRiesgo(
      id: Long,
      entidad: ProbabilidadRiesgosEntidad): Future[Boolean]

  /**
    * Borra un registro de la tabla probabilidad_riesgo por su id
    * @param id
    * @return
    */
  def borrarProbabilidadRiesgo(id: Long): Future[Boolean]

}

class ProbabilidadRiesgosDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends ProbabilidadRiesgosDao
    with ProbabilidadRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la tabla probabilidad_riesgo
    *
    * @param entidad
    * @return
    */
  override def crearProbabilidadRiesgos(
      entidad: ProbabilidadRiesgosEntidad): Future[Long] =
    db.run(
      probabilidadRiesgos returning probabilidadRiesgos.map(_.id) += entidad)

  /**
    * Trae un registro de la tabla probabilidad_riesgos por su id
    *
    * @param id
    * @return
    */
  override def traerProabilidadRiesgoPorId(
      id: Long): Future[Option[ProbabilidadRiesgosEntidad]] =
    db.run(probabilidadRiesgos.filter(_.id === id).result.headOption)

  /**
    * Trae todos los registros de la tabla probabilidad_riesgos
    *
    * @return
    */
  override def traerProbabilidadesRiesgos()
    : Future[Seq[ProbabilidadRiesgosEntidad]] =
    db.run(probabilidadRiesgos.result)

  /**
    * Actualiza un registro de la tabla probabilidad_riesgos por su id
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarProbabilidadRiesgo(
      id: Long,
      entidad: ProbabilidadRiesgosEntidad): Future[Boolean] = {
    traerProabilidadRiesgoPorId(id) flatMap {
      case Some(old) =>
        val actualizado = entidad.merge(old)
        db.run(probabilidadRiesgos.filter(_.id === id).update(actualizado))
          .map(_ == 1)
      case None =>
        Future.successful(false)
    }
  }

  /**
    * Borra un registro de la tabla probabilidad_riesgo por su id
    *
    * @param id
    * @return
    */
  override def borrarProbabilidadRiesgo(id: Long): Future[Boolean] =
    db.run(probabilidadRiesgos.filter(_.id === id).delete).map(_ == 1)
}
