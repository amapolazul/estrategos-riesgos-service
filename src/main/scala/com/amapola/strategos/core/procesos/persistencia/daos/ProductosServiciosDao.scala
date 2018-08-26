package com.amapola.strategos.core.procesos.persistencia.daos

import com.amapola.strategos.core.procesos.persistencia.entidades.ProductosServiciosEntidad
import com.amapola.strategos.core.procesos.persistencia.tablas.ProductosServiciosTable
import com.amapola.strategos.utils.db.DatabaseConnector

import scala.concurrent.{ExecutionContext, Future}

sealed trait ProductosServiciosDao {

  /**
    * Devuelve todos los registros de Producto_servicios
    * @return
    */
  def getProductosServicios(): Future[Seq[ProductosServiciosEntidad]]

  /**
    * Devuelve un registro Producto_Servicios por id
    * @param productoId
    * @return
    */
  def getProductoServicioPorId(
      productoId: Long): Future[Option[ProductosServiciosEntidad]]

  /**
    * Agrega un nuevo registro en la tabla Producto_Servicios
    * @param entidad
    * @return
    */
  def guardarProductoServicio(entidad: ProductosServiciosEntidad): Future[Long]

  /**
    * Retorna los ProductoServicios de un proceso dado un ProcesoId
    * @param procesoId
    * @return
    */
  def getProductoServiciosPorProcesoId(
      procesoId: Long): Future[Seq[ProductosServiciosEntidad]]

  /**
    * Actualiza un registro en ProductoServicios dado el ProductoServicio_id
    * @param prodServicioId
    * @param entidad
    * @return
    */
  def actualizarProductoServicios(
      prodServicioId: Long,
      entidad: ProductosServiciosEntidad): Future[Boolean]

  /**
    * Borra un registro de ProductoServicio dado un prodServicioId
    * @param prodServicioId
    * @return
    */
  def borrarProductoServicios(prodServicioId: Long): Future[Boolean]

  /**
    * Borra todos los Productos servicios de un proceso
    * @param procesoId
    * @return
    */
  def borrarProductosServiciosPorProcesoId(procesoId: Long): Future[Boolean]

}

class ProductosServiciosDaoImpl(val databaseConnector: DatabaseConnector)(
    implicit executionContext: ExecutionContext)
    extends ProductosServiciosDao
    with ProductosServiciosTable {

  import databaseConnector._
  import databaseConnector.profile.api._

  /**
    * Devuelve todos los registros de Producto_servicios
    *
    * @return
    */
  override def getProductosServicios(): Future[Seq[ProductosServiciosEntidad]] =
    db.run(productosServicios.result)

  /**
    * Devuelve un registro Producto_Servicios por id
    *
    * @param productoId
    * @return
    */
  override def getProductoServicioPorId(
      productoId: Long): Future[Option[ProductosServiciosEntidad]] =
    db.run(
      productosServicios
        .filter(_.productoServicioId === productoId)
        .result
        .headOption)

  /**
    * Agrega un nuevo registro en la tabla Producto_Servicios
    *
    * @param entidad
    * @return
    */
  override def guardarProductoServicio(
      entidad: ProductosServiciosEntidad): Future[Long] =
    db.run(
      productosServicios returning productosServicios
        .map(_.productoServicioId) += entidad)

  /**
    * Retorna los ProductoServicios de un proceso dado un ProcesoId
    *
    * @param procesoId
    * @return
    */
  override def getProductoServiciosPorProcesoId(
      procesoId: Long): Future[Seq[ProductosServiciosEntidad]] =
    db.run(productosServicios.filter(_.procesoId === procesoId).result)

  /**
    * Actualiza un registro en ProductoServicios dado el ProductoServicio_id
    *
    * @param prodServicioId
    * @param entidad
    * @return
    */
  override def actualizarProductoServicios(
      prodServicioId: Long,
      entidad: ProductosServiciosEntidad): Future[Boolean] = {
    getProductoServicioPorId(prodServicioId) flatMap {
      case Some(porActualizar) =>
        val actualizado = entidad.merge(porActualizar)
        db.run(
            productosServicios
              .filter(_.productoServicioId === prodServicioId)
              .map(x => {
                (
                  x.productoServicioNombre,
                  x.productServicioCodigo,
                  x.productoCaracteristicas
                )
              })
              .update(
                (
                  actualizado.producto_Servicio_nombre,
                  actualizado.product_Servicio_Codigo,
                  actualizado.producto_Caracteristicas.getOrElse("")
                )))
          .map(_ == 1)
      case None => Future.successful(false)
    }
  }

  /**
    * Borra un registro de ProductoServicio dado un prodServicioId
    * @param prodServicioId
    * @return
    */
  override def borrarProductoServicios(prodServicioId: Long): Future[Boolean] =
    db.run(
        productosServicios
          .filter(_.productoServicioId === prodServicioId)
          .delete)
      .map(_ == 1)

  /**
    * Borra todos los Productos servicios de un proceso
    *
    * @param procesoId
    * @return
    */
  override def borrarProductosServiciosPorProcesoId(
      procesoId: Long): Future[Boolean] = {
    db.run(
        productosServicios
          .filter(_.procesoId === procesoId)
          .delete)
      .map(_ >= 0)
  }
}
