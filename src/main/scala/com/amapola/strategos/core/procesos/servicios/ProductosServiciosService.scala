package com.amapola.strategos.core.procesos.servicios

import com.amapola.strategos.core.procesos.persistencia.daos.ProductosServiciosDao

import scala.concurrent.{ExecutionContext, Future}

trait ProductosServiciosService {

  /**
    * Borra un producto servicio de la base de datos
    * @param prodServicioId
    * @return
    */
  def borrarProductoServicios(prodServicioId: Long): Future[Boolean]

  /**
    * Borra todos los productos servicios de un proceso
    * @param procesoId
    * @return
    */
  def borrarProductosServiciosPorProcesoId(procesoId: Long): Future[Boolean]

}

class ProductosServiciosServiceImpl(
    productosServiciosDao: ProductosServiciosDao)(
    implicit executionContext: ExecutionContext)
    extends ProductosServiciosService {

  /**
    * Borra un producto servicio de la base de datos
    *
    * @param prodServicioId
    * @return
    */
  override def borrarProductoServicios(prodServicioId: Long): Future[Boolean] = {
    productosServiciosDao.borrarProductoServicios(prodServicioId)
  }

  /**
    * Borra todos los productos servicios de un proceso
    *
    * @param procesoId
    * @return
    */
  override def borrarProductosServiciosPorProcesoId(procesoId: Long): Future[Boolean] = {
    productosServiciosDao.borrarProductosServiciosPorProcesoId(procesoId)
  }
}
