package com.amapola.strategos.core.procesos.persistencia

package object entidades {

  case class ProcesosEntidad(
      proceso_Id: Option[Long] = None,
      proceso_Padre_Id: Option[Long] = None,
      proceso_Nombre: String,
      proceso_Codigo: String,
      proceso_Tipo: Long,
      proceso_Responsable_Id: Long,
      proceso_Documento: String
  ) {
    require(proceso_Nombre.isEmpty, "El campo proceso_Nombre es requerido")
    require(proceso_Codigo.isEmpty, "El campo proceso_Codigo es requerido")
    require(proceso_Tipo < 0, "El campo proceso_Tipo es requerido")
    require(proceso_Responsable_Id == null,
            "El campo proceso_Responsable_Id es requerido")
    require(proceso_Documento.isEmpty,
            "El campo proceso_Documento es requerido")

    def merge(porActualizar: ProcesosEntidad) = {
      porActualizar.copy(
        proceso_Nombre = this.proceso_Nombre,
        proceso_Codigo = this.proceso_Codigo,
        proceso_Tipo = this.proceso_Tipo,
        proceso_Responsable_Id = this.proceso_Responsable_Id,
        proceso_Documento = this.proceso_Documento
      )
    }
  }

  case class ProductosServiciosEntidad(
      producto_Servicio_Id: Option[Long] = None,
      proceso_Id: Long,
      producto_Servicio_nombre: String,
      producto_Caracteristicas: String
  ) {
    require(proceso_Id == null || proceso_Id <= 0,
            "El campo proceso_Id es requerido")
    require(producto_Servicio_nombre.isEmpty,
            "El campo producto_Servicio_Nombre es requerido")
    require(producto_Caracteristicas.isEmpty,
            "El campo producto_Caracteristicas es requerido")

    def merge(porActualizar: ProductosServiciosEntidad) = {
      porActualizar.copy(
        producto_Servicio_nombre = this.producto_Servicio_nombre,
        producto_Caracteristicas = this.producto_Caracteristicas
      )
    }
  }

  case class ProcesoCaracterizacionesEntidad(
      caraceterizacion_id: Option[Long] = None,
      proceso_Id: Long,
      procedimiento_Nombre: String,
      procedimiento_Codigo: String
  ) {

    require(proceso_Id == null || proceso_Id <= 0,
            "El campo proceso_Id es requerido")
    require(procedimiento_Nombre.isEmpty,
            "El campo procedimiento_Nombre es requerido")
    require(procedimiento_Codigo.isEmpty,
            "El campo procedimiento_Codigo es requerido")

    def merge(porActualizar: ProcesoCaracterizacionesEntidad)
      : ProcesoCaracterizacionesEntidad = {
      porActualizar.copy(
        proceso_Id = this.proceso_Id,
        procedimiento_Nombre = this.procedimiento_Nombre,
        procedimiento_Codigo = this.procedimiento_Codigo
      )
    }
  }

  case class ProcesoDocumentosEntidad(
      procedimiento_Documento_Id: Option[Long] = None,
      caraceterizacion_id: Long,
      procedimiento_Documento_Nombre: String,
      procedimiento_Documento_Descripcion: String,
      procedimiento_Documento_Codigo: String,
      procedimiento_Documento_Arch: String
  ) {
    require(caraceterizacion_id == null || caraceterizacion_id <= 0,
            "El campo caraceterizacion_id es requerido")
    require(procedimiento_Documento_Nombre.isEmpty,
            "El campo procedimiento_Documento_Nombre es requerido")
    require(procedimiento_Documento_Descripcion.isEmpty,
            "El campo procedimiento_Documento_Descripcion es requerido")
    require(procedimiento_Documento_Codigo.isEmpty,
            "El campo procedimiento_Documento_Codigo es requerido")
    require(procedimiento_Documento_Arch.isEmpty,
            "El campo procedimiento_Documento_Arch es requerido")

    def merge(porActualizar: ProcesoDocumentosEntidad) = {
      porActualizar.copy(
        procedimiento_Documento_Nombre = this.procedimiento_Documento_Nombre,
        procedimiento_Documento_Descripcion =
          this.procedimiento_Documento_Descripcion,
        procedimiento_Documento_Codigo = this.procedimiento_Documento_Codigo,
        procedimiento_Documento_Arch = this.procedimiento_Documento_Arch
      )
    }
  }
}
