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
}
