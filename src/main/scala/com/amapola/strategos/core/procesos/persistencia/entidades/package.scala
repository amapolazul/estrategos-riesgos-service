package com.amapola.strategos.core.procesos.persistencia

package object entidades {

  case class ProcesosEntidad(
      proceso_Id: Option[Long],
      proceso_Padre_Id: Option[Long],
      proceso_Nombre: String,
      proceso_Codigo: String,
      proceso_Tipo: String,
      proceso_Responsable_Id: String,
      proceso_Documento: String
  ) {
    require(proceso_Nombre.isEmpty, "El campo proceso_Nombre es requerido")
    require(proceso_Codigo.isEmpty, "El campo proceso_Codigo es requerido")
    require(proceso_Tipo.isEmpty,"El campo proceso_Tipo es requerido")
    require(proceso_Responsable_Id.isEmpty,"El campo proceso_Responsable_Id es requerido")
    require(proceso_Documento.isEmpty,"El campo proceso_Documento es requerido")
  }

  case class ProductosServiciosEntidad(
      producto_Servicio_Id: Option[Long],
      proceso_Id: Long,
      producto_Servicio_Nombre: String,
      producto_Caracteristicas: String
  ) {
    require(proceso_Id == null || proceso_Id <= 0, "El campo proceso_Id es requerido")
    require(producto_Servicio_Nombre.isEmpty, "El campo producto_Servicio_Nombre es requerido")
    require(producto_Caracteristicas.isEmpty, "El campo producto_Caracteristicas es requerido")
  }

  case class ProcesoCaracterizacionesEntidad(
    caraceterizacion_id: Option[Long],
    proceso_Id: Long,
    procedimiento_Nombre: String,
    procedimiento_Codigo: String
  ) {
    require(proceso_Id == null || proceso_Id <= 0, "El campo proceso_Id es requerido")
    require(procedimiento_Nombre.isEmpty, "El campo procedimiento_Nombre es requerido")
    require(procedimiento_Codigo.isEmpty, "El campo procedimiento_Codigo es requerido")
  }

  case class ProcesoDocumentosEntidad (
    procedimiento_Documento_Id: Option[Long],
    caraceterizacion_id: Long,
    procedimiento_Documento_Nombre: String,
    procedimiento_Documento_Descripcion: String,
    procedimiento_Documento_Codigo: String,
    procedimiento_Documento_Arch: String
  ) {
    require(caraceterizacion_id == null || caraceterizacion_id <= 0, "El campo caraceterizacion_id es requerido")
    require(procedimiento_Documento_Nombre.isEmpty, "El campo procedimiento_Documento_Nombre es requerido")
    require(procedimiento_Documento_Descripcion.isEmpty, "El campo procedimiento_Documento_Descripcion es requerido")
    require(procedimiento_Documento_Codigo.isEmpty,"El campo procedimiento_Documento_Codigo es requerido")
    require(procedimiento_Documento_Arch.isEmpty, "El campo procedimiento_Documento_Arch es requerido")
  }

}
