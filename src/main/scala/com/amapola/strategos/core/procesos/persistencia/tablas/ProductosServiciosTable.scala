package com.amapola.strategos.core.procesos.persistencia.tablas

import com.amapola.strategos.core.procesos.persistencia.entidades.{
  ProcesosEntidad,
  ProductosServiciosEntidad
}
import com.amapola.strategos.utils.db.DatabaseConnector
import slick.lifted.ForeignKeyQuery

/**
  * Definicion de la tabla Productos_Servicios. Este trait se encarga de mapear la entidad de base de datos
  * a una clase para su uso
  */
private[procesos] trait ProductosServiciosTable extends ProcesosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class ProductosServicios(tag: Tag)
      extends Table[ProductosServiciosEntidad](tag, "productos_servicios") {

    def productoServicioId =
      column[Long]("producto_servicio_id", O.PrimaryKey, O.AutoInc)
    def procesoId = column[Long]("proceso_id")
    def productoServicioNombre = column[String]("producto_servicio_nombre")
    def productServicioCodigo = column[String]("producto_servicio_codigo")
    def productoCaracteristicas = column[String]("producto_caracteristicas")

    def procesoProductoFk: ForeignKeyQuery[Procesos, ProcesosEntidad] =
      foreignKey("PROCESO_FK", procesoId, procesos)(
        _.procesoId,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def * =
      (productoServicioId.?,
       procesoId.?,
       productoServicioNombre,
       productServicioCodigo,
       productoCaracteristicas) <> ((ProductosServiciosEntidad.apply _).tupled, ProductosServiciosEntidad.unapply)
  }

  protected val productosServicios = TableQuery[ProductosServicios]

}
