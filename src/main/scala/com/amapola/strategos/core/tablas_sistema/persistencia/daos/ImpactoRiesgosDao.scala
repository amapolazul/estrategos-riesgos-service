package com.amapola.strategos.core.tablas_sistema.persistencia.daos

import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.ImpactoRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.ImpactoRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait ImpactoRiesgosDao {

  /**
    * Crea un registro en la tabla impacto_riesgos
    * @param entidad
    * @return
    */
  def crearImpactoRiesgos(entidad: ImpactoRiesgosEntidad): Future[Long]

  /**
    * Consulta un registro de la tabla impactor_riesgo por su id
    * @param id
    * @return
    */
  def traerImpactoRiesgoPorId(id: Long): Future[Option[ImpactoRiesgosEntidad]]

  /**
    * Trae todos los registros de la tabla impacto_riesgos
    * @return
    */
  def traerImpactosRiesgos(): Future[Seq[ImpactoRiesgosEntidad]]

  /**
    * Actualiza un registro de la tabla impacto_riesgos por su id
    * @param id
    * @param entidad
    * @return
    */
  def actualizarImpactoRiesgos(id: Long,
                               entidad: ImpactoRiesgosEntidad): Future[Boolean]

  /**
    * Borra un registro de la tabla impacto_riesgos
    * @param id
    * @return
    */
  def borrarImpactoRiesgos(id: Long): Future[Boolean]

}

class ImpactoRiesgosDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends ImpactoRiesgosDao
    with ImpactoRiesgosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Crea un registro en la tabla impacto_riesgos
    *
    * @param entidad
    * @return
    */
  override def crearImpactoRiesgos(
      entidad: ImpactoRiesgosEntidad): Future[Long] =
    db.run(
      impactoRiesgos returning impactoRiesgos
        .map(_.id) += entidad)

  /**
    * Consulta un registro de la tabla impactor_riesgo por su id
    *
    * @param id
    * @return
    */
  override def traerImpactoRiesgoPorId(
      id: Long): Future[Option[ImpactoRiesgosEntidad]] =
    db.run(impactoRiesgos.filter(_.id === id).result.headOption)

  /**
    * Trae todos los registros de la tabla impacto_riesgos
    *
    * @return
    */
  override def traerImpactosRiesgos(): Future[Seq[ImpactoRiesgosEntidad]] =
    db.run(impactoRiesgos.result)

  /**
    * Actualiza un registro de la tabla impacto_riesgos por su id
    *
    * @param id
    * @param entidad
    * @return
    */
  override def actualizarImpactoRiesgos(
      id: Long,
      entidad: ImpactoRiesgosEntidad): Future[Boolean] = {
    traerImpactoRiesgoPorId(id) flatMap {
      case Some(old) =>
        val actualizado = entidad.merge(old)
        db.run(impactoRiesgos.filter(_.id === id)
            .map(x => {
              (
                x.impacto,
                x.puntaje,
                x.descripcion
              )
            })
          .update((
            actualizado.impacto,
            actualizado.puntaje,
            actualizado.descripcion.getOrElse("")
          )))
          .map(_ == 1)
      case None => Future.successful(false)
    }
  }

  /**
    * Borra un registro de la tabla impacto_riesgos
    *
    * @param id
    * @return
    */
  override def borrarImpactoRiesgos(id: Long): Future[Boolean] =
    db.run(impactoRiesgos.filter(_.id === id).delete)
      .map(_ == 1)
}
