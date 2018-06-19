package com.amapola.strategos.core.procesos.servicios

import com.amapola.strategos.core.procesos.http.json._
import com.amapola.strategos.core.procesos.persistencia.daos.{
  CaracterizacionDao,
  DocumentosCaracterizacionDao,
  ProcesosDao,
  ProductosServiciosDao
}
import com.amapola.strategos.core.procesos.persistencia.entidades.{
  ProcesoCaracterizacionesEntidad,
  ProcesoDocumentosEntidad,
  ProductosServiciosEntidad
}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Clase que expone los servicios asociados a los procesos, productos/servicios y caracterizaciones
  * con los nombres de los archivos asociados
  * @param caracterizacionDao
  * @param procesosDao
  * @param productosServiciosDao
  * @param documentosCaracterizacionDao
  * @param executionContext
  */
class ProcesosServiciosImpl(
    caracterizacionDao: CaracterizacionDao,
    procesosDao: ProcesosDao,
    productosServiciosDao: ProductosServiciosDao,
    documentosCaracterizacionDao: DocumentosCaracterizacionDao)(
    implicit executionContext: ExecutionContext)
    extends ProcesosServiciosService {

  /**
    * Crea el proceso con sus dependencias desde el request enviado por el servicio
    * @param request
    * @return
    */
  override def crearProcesos(
      request: ProcesoProcedimientoJson): Future[Long] = {

    val procesoEntity = Proceso.toEntity(request.proceso)
    val prodcutosServicios = request.productoServicios
    val caracterizaciones = request.caracterizaciones

    for {
      procesoId <- procesosDao.guardarProceso(procesoEntity)
      _ <- crearListaProductosServicios(prodcutosServicios, procesoId)
      _ <- crearListaCaracterizacionesArchivos(caracterizaciones, procesoId)
    } yield procesoId

  }

  /**
    * Crea la lista de caracterizaciones y sus archivos asociados a la base de datos.
    * Una vez guardada la caracterizacion se guardan los archivos asociados a la caracterizacion
    * @param list
    * @param procesoId
    * @return
    */
  private def crearListaCaracterizacionesArchivos(list: List[Caracterizacion],
                                                  procesoId: Long) = {

    val caraceristicasArchivos = list.map(x => {
      val caractEntidad = Caracterizacion.toEntity(x).copy(proceso_Id = Some(procesoId))

      caracterizacionDao
        .crearCaracterizacion(caractEntidad)
        .flatMap(caracterizacionId => {
          val archivos = x.documentosCaracterizacion
          val archivosEntidad = archivos.map(arch => {
            val archivo = DocumentoCaracterizacion.toEntity(
              arch.copy(caraceterizacion_id = Some(caracterizacionId)))
            documentosCaracterizacionDao.crearDocumentoCaracterizacion(archivo)
          })

          Future.sequence(archivosEntidad)
        })
    })

    Future.sequence(caraceristicasArchivos)
  }

  private def crearListaCaracterizacionesArchivosJson(
      caract: Seq[ProcesoCaracterizacionesEntidad]) = {
    val caracterizaciones: Seq[Future[Caracterizacion]] = caract.map(x => {
      documentosCaracterizacionDao
        .getDocumentosPorCaracterizacion(x.caraceterizacion_id.getOrElse(0l))
        .map(y => {
          Caracterizacion.fromEntity(x, y.toList)
        })
    })

    Future.sequence(caracterizaciones).map(_.toList)
  }

  /**
    * Convierte la lista de productos servicios en una lista de futuros los cuales guardan
    * los registros correspondientes
    * @param list
    * @param procesoId
    * @return
    */
  private def crearListaProductosServicios(
      list: List[ProductoServicio],
      procesoId: Long): Future[List[Long]] = {
    val futureList = list.map(x => {

      val prodServicio =
        ProductoServicio.toEntity(x.copy(proceso_Id = Some(procesoId)))
      productosServiciosDao.guardarProductoServicio(prodServicio)
    })

    Future.sequence(futureList)
  }

  /**
    * Devuelve los procesos asociados a un proceso padre dado su id
    * @param padreId
    * @return
    */
  override def traerProcesosPorIdPadre(padreId: Long): Future[Seq[Proceso]] = {
    procesosDao
      .getProcesosPorIdPadre(padreId)
      .map(list => {
        list.map(proc => {
          Proceso.fromEntity(proc)
        })
      })
  }

  /**
    * Consulta la información completa de los procesos por su id
    * @param id
    * @return
    */
  override def traerProcesoPorId(
      id: Long): Future[Option[ProcesoProcedimientoJson]] = {
    procesosDao
      .getProcesoPorId(id)
      .flatMap(proceso => {
        proceso match {
          case Some(pr) =>
            for {
              productoServicios <- productosServiciosDao.getProductoServiciosPorProcesoId(
                pr.proceso_Id.getOrElse(0l))
              caract <- caracterizacionDao.darCaracterizacionesPorProcesoId(
                pr.proceso_Id.getOrElse(0l))
              caractArchivos <- crearListaCaracterizacionesArchivosJson(caract)
            } yield {
              Some(
                ProcesoProcedimientoJson(
                  proceso = Proceso.fromEntity(pr),
                  productoServicios =
                    productoServicios.map(ProductoServicio.fromEntity(_)).toList,
                  caracterizaciones = caractArchivos
                ))
            }
          case None =>
            Future.successful(None)
        }
      })
  }
}
