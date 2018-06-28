package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.daos

import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.entidades.EjerciciosEvaluacionEstatusEntidad
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.tablas.EjerciciosEvaluacionesEstatusTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

trait EjerciciosEvaluacionEstatusDao {

  /**
    * Consulta todos los estados de ejercicios evaluacion
    * @return
    */
  def traerEjerciciosEstatus(): Future[Seq[EjerciciosEvaluacionEstatusEntidad]]
}

class EjerciciosEvaluacionEstatusDaoImpl(
    val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends EjerciciosEvaluacionEstatusDao
    with EjerciciosEvaluacionesEstatusTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Consulta todos los estados de ejercicios evaluacion
    *
    * @return
    */
  override def traerEjerciciosEstatus()
    : Future[Seq[EjerciciosEvaluacionEstatusEntidad]] =
    db.run(ejerciciosEstatus.result)

}
