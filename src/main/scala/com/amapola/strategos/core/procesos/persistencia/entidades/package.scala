package com.amapola.strategos.core.procesos.persistencia

package object entidades {

  case class ProcesosEntidad(
      proceso_Id: Option[Long] = None,
      proceso_Padre_Id: Option[Long] = None,
      proceso_Nombre: String,
      proceso_Codigo: Option[String] = None,
      proceso_Descripcion: Option[String] = None,
      proceso_Tipo: Option[Long] = None,
      proceso_Responsable_Id: Option[Long] = None,
      proceso_Documento: String
  ) {
    require(!proceso_Nombre.isEmpty, "El campo proceso_Nombre es requerido")
    require(!proceso_Codigo.isEmpty, "El campo proceso_Codigo es requerido")
    require(!proceso_Documento.isEmpty,
            "El campo proceso_Documento es requerido")

    def merge(porActualizar: ProcesosEntidad) = {
      porActualizar.copy(
        proceso_Nombre = this.proceso_Nombre,
        proceso_Codigo = this.proceso_Codigo,
        proceso_Descripcion = this.proceso_Descripcion,
        proceso_Tipo = this.proceso_Tipo,
        proceso_Responsable_Id = this.proceso_Responsable_Id,
        proceso_Documento = this.proceso_Documento
      )
    }
  }

  case class ProductosServiciosEntidad(
      producto_Servicio_Id: Option[Long] = None,
      proceso_Id: Option[Long] = None,
      product_Servicio_Codigo: String,
      producto_Servicio_nombre: String,
      producto_Caracteristicas: Option[String] = None
  ) {
    require(!producto_Servicio_nombre.isEmpty,
            "El campo producto_Servicio_Nombre es requerido")

    def merge(porActualizar: ProductosServiciosEntidad) = {
      porActualizar.copy(
        producto_Servicio_nombre = this.producto_Servicio_nombre,
        product_Servicio_Codigo = this.product_Servicio_Codigo,
        producto_Caracteristicas = this.producto_Caracteristicas
      )
    }
  }

  case class ProcesoCaracterizacionesEntidad(
      caraceterizacion_id: Option[Long] = None,
      proceso_Id: Option[Long] = None,
      procedimiento_Nombre: String,
      procedimiento_Codigo: String,
      procedimiento_Objetivo: Option[String] = None
  ) {

    require(!procedimiento_Nombre.isEmpty,
            "El campo procedimiento_Nombre es requerido")
    require(!procedimiento_Codigo.isEmpty,
            "El campo procedimiento_Codigo es requerido")

    def merge(porActualizar: ProcesoCaracterizacionesEntidad)
      : ProcesoCaracterizacionesEntidad = {
      porActualizar.copy(
        procedimiento_Nombre = this.procedimiento_Nombre,
        procedimiento_Codigo = this.procedimiento_Codigo,
        procedimiento_Objetivo = this.procedimiento_Objetivo
      )
    }
  }

  case class ProcesoDocumentosEntidad(
      procedimiento_Documento_Id: Option[Long] = None,
      caraceterizacion_id: Option[Long] = None,
      procedimiento_Documento_Nombre: String,
      procedimiento_Documento_Descripcion: String,
      procedimiento_Documento_Codigo: String,
      procedimiento_Documento_Arch: String
  ) {
    require(!procedimiento_Documento_Nombre.isEmpty,
            "El campo procedimiento_Documento_Nombre es requerido")
    require(!procedimiento_Documento_Descripcion.isEmpty,
            "El campo procedimiento_Documento_Descripcion es requerido")
    require(!procedimiento_Documento_Codigo.isEmpty,
            "El campo procedimiento_Documento_Codigo es requerido")
    require(!procedimiento_Documento_Arch.isEmpty,
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
