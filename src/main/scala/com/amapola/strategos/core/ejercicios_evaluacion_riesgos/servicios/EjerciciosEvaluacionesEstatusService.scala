package com.amapola.strategos.core.ejercicios_evaluacion_riesgos.servicios

import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.http.json.EjerciciosEvaluacionEstatusJson
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.daos.EjerciciosEvaluacionEstatusDao

import scala.concurrent.{ExecutionContext, Future}

trait EjerciciosEvaluacionesEstatusService {

  /**
    * Trae todos los estados disponibles para los ejercicios de evaluación
    * @return
    */
  def traerEjerciciosEstatus(): Future[Seq[EjerciciosEvaluacionEstatusJson]]

}

case class EjerciciosEvaluacionesEstatusServiceImpl(
    ejerciciosEstatusDao: EjerciciosEvaluacionEstatusDao)(
    implicit executionContext: ExecutionContext)
    extends EjerciciosEvaluacionesEstatusService {

  /**
    * Trae todos los estados disponibles para los ejercicios de evaluación
    * @return
    */
  override def traerEjerciciosEstatus()
    : Future[Seq[EjerciciosEvaluacionEstatusJson]] = {
    ejerciciosEstatusDao
      .traerEjerciciosEstatus()
      .map(result => {
        result.map(EjerciciosEvaluacionEstatusJson.fromEntity(_))
      })
  }
}
