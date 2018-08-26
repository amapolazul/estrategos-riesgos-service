package com.amapola.strategos.core.procesos.persistencia.daos

import com.amapola.strategos.core.procesos.persistencia.entidades.ProcesosEntidad
import com.amapola.strategos.core.procesos.persistencia.tablas.ProcesosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

sealed trait ProcesosDao {

  /**
    * Obtiene toda la lista de procesos
    * @return
    */
  def getProcesos(): Future[Seq[ProcesosEntidad]]

  /**
    * Obtiene un proceso por Id
    * @param procesoId
    * @return
    */
  def getProcesoPorId(procesoId: Long): Future[Option[ProcesosEntidad]]

  /**
    * Obtiene todos los procesos cuyo procesoId padre coincida con el enviado
    * por parametro
    * @param procesoIdPadre
    * @return
    */
  def getProcesosPorIdPadre(procesoIdPadre: Long): Future[Seq[ProcesosEntidad]]

  /**
    * Guarda el proceso retornando el Id resultante
    * @param procesosEntidad
    * @return
    */
  def guardarProceso(procesosEntidad: ProcesosEntidad): Future[Long]

  /**
    * Actualiza el proceso dado un procesoId y una entidad que contiene los nuevos datos
    * @param idProceso
    * @param entidad
    * @return
    */
  def actualizarProceso(idProceso: Long,
                        entidad: ProcesosEntidad): Future[Boolean]

  /**
    * Borra el proceso dado un procesoId. Esta funcion debería borrar todos los datos
    * relacionados al proceso como productos y caracterizaciones
    *
    * @param idProceso
    * @return
    */
  def borrarProceso(idProceso: Long): Future[Boolean]
}

class ProcesosDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends ProcesosDao
    with ProcesosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Obtiene toda la lista de procesos
    *
    * @return
    */
  override def getProcesos(): Future[Seq[ProcesosEntidad]] =
    db.run(procesos.result)

  /**
    * Obtiene un proceso por Id
    *
    * @param procesoId
    * @return
    */
  override def getProcesoPorId(
      procesoId: Long): Future[Option[ProcesosEntidad]] =
    db.run(procesos.filter(_.procesoId === procesoId).result.headOption)

  /**
    * Obtiene todos los procesos cuyo procesoId padre coincida con el enviado
    * por parametro
    *
    * @param procesoIdPadre
    * @return
    */
  override def getProcesosPorIdPadre(
      procesoIdPadre: Long): Future[Seq[ProcesosEntidad]] =
    db.run(procesos.filter(_.procesoPadreId === procesoIdPadre).result)

  /**
    * Guarda el proceso retornando el Id resultante
    * @param procesosEntidad
    * @return
    */
  override def guardarProceso(procesosEntidad: ProcesosEntidad): Future[Long] =
    db.run(procesos returning procesos.map(_.procesoId) += procesosEntidad)

  /**
    * Actualiza el proceso dado un procesoId y una entidad que contiene los nuevos datos
    *
    * @param idProceso
    * @param entidad
    * @return
    */
  override def actualizarProceso(idProceso: Long,
                                 entidad: ProcesosEntidad): Future[Boolean] = {
    getProcesoPorId(idProceso) flatMap {
      case Some(procesoAnterior) =>
        val actualizado = entidad.merge(procesoAnterior)
        db.run(
            procesos
              .filter(_.procesoId === idProceso)
              .map(x => {
                (
                  x.procesoNombre,
                  x.procesoCodigo,
                  x.procesoDescripcion,
                  x.procesoTipo,
                  x.procesoResponsable,
                  x.procesoDocumento
                )
              })
              .update(
                (
                  actualizado.proceso_Nombre,
                  actualizado.proceso_Codigo.getOrElse(""),
                  actualizado.proceso_Descripcion.getOrElse(""),
                  actualizado.proceso_Tipo.getOrElse(0l),
                  actualizado.proceso_Responsable_Id.getOrElse(""),
                  actualizado.proceso_Documento.getOrElse("")
                )))
          .map(_ == 1)
      case None => Future.successful(false)
    }
  }

  /**
    * Borra el proceso dado un procesoId. Esta funcion debería borrar todos los datos
    * relacionados al proceso como productos y caracterizaciones
    *
    * @param idProceso
    * @return
    */
  override def borrarProceso(idProceso: Long): Future[Boolean] = {
    db.run(procesos.filter(_.procesoId === idProceso).delete).map(_ == 1)
  }
}
