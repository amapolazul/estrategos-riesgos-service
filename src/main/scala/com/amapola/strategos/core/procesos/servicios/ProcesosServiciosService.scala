package com.amapola.strategos.core.procesos.servicios

import com.amapola.strategos.core.procesos.http.json.{Proceso, ProcesoProcedimientoJson}

import scala.concurrent.Future

/**
  * Trait que expone las funciones encargadas de interactuar con los procesos
  * y sus dependencias
  */
trait ProcesosServiciosService {

  /**
    * Crea el proceso con sus dependencias desde el request enviado por el servicio
    * @param request
    * @return
    */
  def crearProcesos(request: ProcesoProcedimientoJson) : Future[Long]

  /**
    * Devuelve los procesos asociados a un proceso padre dado su id
    * @param padreId
    * @return
    */
  def traerProcesosPorIdPadre(padreId: Long) : Future[Seq[Proceso]]

  /**
    * Consulta la información completa de los procesos por su id
    * @param id
    * @return
    */
  def traerProcesoPorId(id: Long) : Future[Option[ProcesoProcedimientoJson]]

  /**
    * Consulta todos los procesos y sub procesos
    * @return
    */
  def traerProcesos() : Future[Seq[Proceso]]

  /**
    * Actualiza un proceso completo dado la entidad con la nueva información y su Id
    * @param id
    * @param request
    */
  def actualizarProceso(id:Long, request: ProcesoProcedimientoJson) : Future[Boolean]

}


