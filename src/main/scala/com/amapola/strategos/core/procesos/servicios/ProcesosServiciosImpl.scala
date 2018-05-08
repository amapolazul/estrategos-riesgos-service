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
      request: ProcesoProcedimientoRequest): Future[Long] = {

    val procesoEntity = Proceso.asProcesoEntitidad(request.proceso)
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
      val caractEntidad = ProcesoCaracterizacionesEntidad(
        proceso_Id = procesoId,
        procedimiento_Nombre = x.procedimiento,
        procedimiento_Codigo = x.codigoProcedimiento
      )

      caracterizacionDao
        .crearCaracterizacion(caractEntidad)
        .flatMap(caracterizacionId => {
          val archivos = x.documentosCaracterizacion
          val archivosEntidad = archivos.map(arch => {
            val archivo = ProcesoDocumentosEntidad(
              caraceterizacion_id = caracterizacionId,
              procedimiento_Documento_Nombre = arch.nombreDocumento,
              procedimiento_Documento_Descripcion = arch.descripcion,
              procedimiento_Documento_Codigo = arch.codigoDocumento,
              procedimiento_Documento_Arch = arch.anexo
            )

            documentosCaracterizacionDao.crearDocumentoCaracterizacion(archivo)
          })

          Future.sequence(archivosEntidad)
        })
    })

    Future.sequence(caraceristicasArchivos)
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
      val prodServicio = ProductosServiciosEntidad(
        proceso_Id = procesoId,
        producto_Servicio_nombre = x.productoServicio,
        producto_Caracteristicas = x.caracteristicas
      )
      productosServiciosDao.guardarProductoServicio(prodServicio)
    })

    Future.sequence(futureList)
  }
}
