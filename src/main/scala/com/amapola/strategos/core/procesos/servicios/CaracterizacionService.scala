package com.amapola.strategos.core.procesos.servicios

import com.amapola.strategos.core.procesos.persistencia.daos.{
  CaracterizacionDao,
  DocumentosCaracterizacionDao
}
import com.amapola.strategos.core.procesos.persistencia.entidades.ProcesoCaracterizacionesEntidad
import com.amapola.strategos.infraestructura.AdministradorArchivosServiceImpl

import scala.concurrent.{ExecutionContext, Future}

trait CaracterizacionService {

  /**
    * Retorna todas las caracterizaciones
    * @return
    */
  def darCaracterizaciones(): Future[Seq[ProcesoCaracterizacionesEntidad]]

  /**
    * Retorna las caracterizaciones por su Id
    * @param caracterizacionId
    * @return
    */
  def darCaracterizacionPorId(
      caracterizacionId: Long): Future[Option[ProcesoCaracterizacionesEntidad]]

  /**
    * Retorna las caracterizaciones de un proceso por su procesoId
    * @param procesoId
    * @return
    */
  def darCaracterizacionesPorProcesoId(
      procesoId: Long): Future[Seq[ProcesoCaracterizacionesEntidad]]

  /**
    * Agrega un registro a la tabla proceso caracterizacion
    * @param entidad
    * @return
    */
  def crearCaracterizacion(
      entidad: ProcesoCaracterizacionesEntidad): Future[Long]

  /**
    * Actualiza un registro de caracterizacion
    * @param entidad
    * @return
    */
  def actualizarCaracterizacion(
      caracterizacionId: Long,
      entidad: ProcesoCaracterizacionesEntidad): Future[Boolean]

  /**
    * Borrar una caracterizacion por su id
    * @param caraceterizacionId
    * @return
    */
  def borrarCaracterizacion(caraceterizacionId: Long): Future[Boolean]

  /**
    * Borra todas las caracterizaciones de un proceso
    * @param procesoId
    * @return
    */
  def borrarCaracterizacionesPorProcesoId(procesoId: Long): Future[Boolean]

}

class CaracterizacionServiceImpl(
    caracterizacionDao: CaracterizacionDao,
    documentosCaracterizacionDao: DocumentosCaracterizacionDao)(
    implicit executionContext: ExecutionContext)
    extends CaracterizacionService {

  /**
    * Retorna todas las caracterizaciones
    *
    * @return
    */
  override def darCaracterizaciones()
    : Future[Seq[ProcesoCaracterizacionesEntidad]] = {
    caracterizacionDao.darCaracterizaciones()
  }

  /**
    * Retorna las caracterizaciones por su Id
    *
    * @param caracterizacionId
    * @return
    */
  override def darCaracterizacionPorId(caracterizacionId: Long)
    : Future[Option[ProcesoCaracterizacionesEntidad]] = {
    caracterizacionDao.darCaracterizacionPorId(caracterizacionId)
  }

  /**
    * Retorna las caracterizaciones de un proceso por su procesoId
    *
    * @param procesoId
    * @return
    */
  override def darCaracterizacionesPorProcesoId(
      procesoId: Long): Future[Seq[ProcesoCaracterizacionesEntidad]] = {
    caracterizacionDao.darCaracterizacionesPorProcesoId(procesoId)
  }

  /**
    * Agrega un registro a la tabla proceso caracterizacion
    *
    * @param entidad
    * @return
    */
  override def crearCaracterizacion(
      entidad: ProcesoCaracterizacionesEntidad): Future[Long] = {
    caracterizacionDao.crearCaracterizacion(entidad)
  }

  /**
    * Actualiza un registro de caracterizacion
    *
    * @param entidad
    * @return
    */
  override def actualizarCaracterizacion(
      caracterizacionId: Long,
      entidad: ProcesoCaracterizacionesEntidad): Future[Boolean] = {
    caracterizacionDao.actualizarCaracterizacion(caracterizacionId, entidad)
  }

  /**
    * Borrar una caracterizacion por su id
    *
    * @param caraceterizacionId
    * @return
    */
  override def borrarCaracterizacion(
      caraceterizacionId: Long): Future[Boolean] = {

    caracterizacionDao
      .darCaracterizacionPorId(caraceterizacionId)
      .flatMap(result => {
        result match {
          case Some(_) =>
            for {
              archivosBorrados <- borrarArchivosCaracterizacion(
                caraceterizacionId)
              caracterizacionBorrada <- caracterizacionDao
                .borrarCaracterizacionPorId(caraceterizacionId)
            } yield archivosBorrados && caracterizacionBorrada

          case None =>
            Future.successful(false)
        }
      })

  }

  /**
    * Borra todos los archivos de una caracterizacion
    * @param caraceterizacionId
    * @return
    */
  private def borrarArchivosCaracterizacion(
      caraceterizacionId: Long): Future[Boolean] = {
    documentosCaracterizacionDao
      .getDocumentosPorCaracterizacion(caraceterizacionId)
      .flatMap(result => {
        val list = result map (arch => {
          if (AdministradorArchivosServiceImpl.borrarArchivo(
                arch.procedimiento_Documento_Arch)) {
            documentosCaracterizacionDao.borrarDocumentoCaracterizacion(
              arch.procedimiento_Documento_Id.getOrElse(0))
          } else {
            Future.failed(new Exception(
              s"Error Borrando el archivo ${arch.procedimiento_Documento_Arch} con id ${arch.procedimiento_Documento_Id}"))
          }
        })
        if (list.isEmpty) Future.successful(true)
        else Future.sequence(list).map(x => x.reduce(_ && _))
      })
  }

  /**
    * Borra todas las caracterizaciones de un proceso
    * @param procesoId
    * @return
    */
  override def borrarCaracterizacionesPorProcesoId(
      procesoId: Long): Future[Boolean] = {
    caracterizacionDao
      .darCaracterizacionesPorProcesoId(procesoId)
      .flatMap(caracterizaciones => {
        val borrarCaracterizaciones = caracterizaciones.map(caracterizacion => {
          borrarArchivosCaracterizacion(
            caracterizacion.caraceterizacion_id.getOrElse(0l))
        })

        if (borrarCaracterizaciones.isEmpty) Future.successful(true)
        else Future.sequence(borrarCaracterizaciones).map(_.reduce(_ && _))

      })
  }
}
