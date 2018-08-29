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
  * @param caracterizacionService
  * @param procesosDao
  * @param productosServiciosDao
  * @param documentosCaracterizacionDao
  * @param executionContext
  */
class ProcesosServiciosImpl(
    caracterizacionService: CaracterizacionService,
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
      _ <- crearActualizarListaProductosServicios(prodcutosServicios, procesoId)
      _ <- crearActualizarListaCaracterizacionesArchivos(caracterizaciones,
                                                         procesoId)
    } yield procesoId

  }

  /**
    * Actualiza un proceso completo dado la entidad con la nueva información y su Id
    * @param id
    * @param request
    */
  def actualizarProceso(id: Long, request: ProcesoProcedimientoJson) = {
    val procesoEntity = Proceso.toEntity(request.proceso)
    val prodcutosServicios = request.productoServicios
    val caracterizaciones = request.caracterizaciones

    for {
      procesoActualizado <- procesosDao.actualizarProceso(id, procesoEntity)
      productosActualizados <- crearActualizarListaProductosServicios(
        prodcutosServicios,
        id)
      caracterizacionesActualizadas <- crearActualizarListaCaracterizacionesArchivos(
        caracterizaciones,
        id)
    } yield
      procesoActualizado && !productosActualizados.exists(_ == 0) && !caracterizacionesActualizadas.flatten
        .exists(_ == 0)
  }

  /**
    * Crea la lista de caracterizaciones y sus archivos asociados a la base de datos.
    * Una vez guardada la caracterizacion se guardan los archivos asociados a la caracterizacion
    * @param list
    * @param procesoId
    * @return
    */
  private def crearActualizarListaCaracterizacionesArchivos(
      list: List[Caracterizacion],
      procesoId: Long) = {

    val caraceristicasArchivos = list.map(x => {

      x.caraceterizacion_id match {
        case Some(caractId) =>
          val caractEntidad = Caracterizacion.toEntity(x)
          caracterizacionService
            .actualizarCaracterizacion(caractId, caractEntidad)
            .flatMap(upd => {
              val archivos = x.documentosCaracterizacion
              val archivosEntidad = archivos.map(arch => {
                arch.procedimiento_Documento_Id match {
                  case Some(docId) =>
                    val archivo = DocumentoCaracterizacion.toEntity(arch)
                    documentosCaracterizacionDao
                      .actualizarDocumentoCaracetizacion(docId, archivo)
                      .map(if (_) 1l else 0l)
                  case None =>
                    val archivo = DocumentoCaracterizacion.toEntity(
                      arch.copy(caraceterizacion_id = Some(caractId)))
                    documentosCaracterizacionDao.crearDocumentoCaracterizacion(
                      archivo)
                }
              })

              Future.sequence(archivosEntidad)
            })
        case None =>
          val caractEntidad =
            Caracterizacion.toEntity(x).copy(proceso_Id = Some(procesoId))

          caracterizacionService
            .crearCaracterizacion(caractEntidad)
            .flatMap(caracterizacionId => {
              val archivos = x.documentosCaracterizacion
              val archivosEntidad = archivos.map(arch => {
                val archivo = DocumentoCaracterizacion.toEntity(
                  arch.copy(caraceterizacion_id = Some(caracterizacionId)))
                documentosCaracterizacionDao.crearDocumentoCaracterizacion(
                  archivo)
              })

              Future.sequence(archivosEntidad)
            })
      }
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
  private def crearActualizarListaProductosServicios(
      list: List[ProductoServicio],
      procesoId: Long): Future[List[Long]] = {
    val futureList = list.map(x => {

      val prodServicio =
        ProductoServicio.toEntity(x.copy(proceso_Id = Some(procesoId)))
      prodServicio.producto_Servicio_Id match {
        case Some(id) =>
          productosServiciosDao
            .actualizarProductoServicios(id, prodServicio)
            .map(if (_) 1l else 0l)
        case None => productosServiciosDao.guardarProductoServicio(prodServicio)
      }
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
              productoServicios <- productosServiciosDao
                .getProductoServiciosPorProcesoId(pr.proceso_Id.getOrElse(0l))
              caract <- caracterizacionService.darCaracterizacionesPorProcesoId(
                pr.proceso_Id.getOrElse(0l))
              caractArchivos <- crearListaCaracterizacionesArchivosJson(caract)
            } yield {
              Some(
                ProcesoProcedimientoJson(
                  proceso = Proceso.fromEntity(pr),
                  productoServicios = productoServicios
                    .map(ProductoServicio.fromEntity(_))
                    .toList,
                  caracterizaciones = caractArchivos
                ))
            }
          case None =>
            Future.successful(None)
        }
      })
  }

  /**
    * Consulta todos los procesos y subprocesos
    * @return
    */
  override def traerProcesos(): Future[Seq[Proceso]] = {
    procesosDao
      .getProcesos()
      .map(list => {
        list.map(Proceso.fromEntity(_))
      })
  }

  /**
    * Borra el proceso por su id y toda la información relacionada a el tal como productos servicios, caracterizaciones
    * y archivos
    *
    * @param procesoId
    */
  override def borrarProceso(procesoId: Long): Future[(Boolean, Boolean, Boolean)] = {
    for {
      borrarProceso <- procesosDao.borrarProceso(procesoId)
      borrarCaracterizaciones <- caracterizacionService.borrarCaracterizacionesPorProcesoId(procesoId)
      borrarProductosServicios <- productosServiciosDao.borrarProductosServiciosPorProcesoId(procesoId)
    } yield {
      (borrarCaracterizaciones, borrarProductosServicios, borrarProceso)
    }
  }
}
