package com.amapola.strategos.core.procesos.persistencia.tablas

import com.amapola.strategos.core.procesos.persistencia.entidades.{ProcesosEntidad, ProductosServiciosEntidad}
import com.amapola.strategos.utils.db.DatabaseConnector
import slick.lifted.ForeignKeyQuery

/**
  * Definicion de la tabla Productos_Servicios. Este trait se encarga de mapear la entidad de base de datos
  * a una clase para su uso
  */
private[procesos] trait ProductosServiciosTable extends ProcesosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class ProductosServicios(tag: Tag) extends Table[ProductosServiciosEntidad](tag, "Procesos") {

    def productoServicioId = column[Long]("Producto_Servicio_Id", O.PrimaryKey, O.AutoInc)
    def procesoId = column[Long]("Proceso_Id")
    def productoServicioNombre = column[String]("Producto_Servicio_Nombre")
    def productoCaracteristicas = column[String]("Producto_Caracteristicas")

    def procesoProductoFk: ForeignKeyQuery[Procesos, ProcesosEntidad] =
      foreignKey("PROCESO_FK", procesoId, procesos)(
        _.procesoId,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def * =
      (productoServicioId.?,
       procesoId,
       productoServicioNombre,
       productoCaracteristicas) <> ((ProductosServiciosEntidad.apply _).tupled, ProductosServiciosEntidad.unapply)
  }

  protected val productosServicios = TableQuery[ProductosServicios]

}
