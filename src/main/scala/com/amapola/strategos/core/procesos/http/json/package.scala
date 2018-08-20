package com.amapola.strategos.core.procesos.http

import com.amapola.strategos.core.procesos.persistencia.entidades.{ProcesoCaracterizacionesEntidad, ProcesoDocumentosEntidad, ProcesosEntidad, ProductosServiciosEntidad}

package object json {

  case class Proceso(
      proceso_Id: Option[Long] = None,
      proceso_Padre_Id: Option[Long] = None,
      proceso_Nombre: String,
      proceso_Descripcion: Option[String] = None,
      proceso_Codigo: Option[String] = None,
      proceso_Tipo: Option[Long] = None,
      proceso_Responsable_Id: Option[Long] = None,
      proceso_Documento: String
  )
  object Proceso {
    def toEntity(proceso: Proceso): ProcesosEntidad = {
      ProcesosEntidad(
        proceso_Padre_Id = proceso.proceso_Padre_Id,
        proceso_Nombre = proceso.proceso_Nombre,
        proceso_Descripcion = proceso.proceso_Descripcion,
        proceso_Codigo = proceso.proceso_Codigo,
        proceso_Tipo = proceso.proceso_Tipo,
        proceso_Responsable_Id = proceso.proceso_Responsable_Id,
        proceso_Documento = proceso.proceso_Documento
      )
    }

    def fromEntity(procesoEntidad: ProcesosEntidad) = {
      Proceso(
        proceso_Id = procesoEntidad.proceso_Id,
        proceso_Padre_Id = procesoEntidad.proceso_Padre_Id,
        proceso_Nombre = procesoEntidad.proceso_Nombre,
        proceso_Descripcion = procesoEntidad.proceso_Descripcion,
        proceso_Codigo = procesoEntidad.proceso_Codigo,
        proceso_Tipo = procesoEntidad.proceso_Tipo,
        proceso_Responsable_Id = procesoEntidad.proceso_Responsable_Id,
        proceso_Documento = procesoEntidad.proceso_Documento
      )
    }
  }

  case class ProductoServicio(
      producto_Servicio_Id: Option[Long] = None,
      proceso_Id: Option[Long] = None,
      producto_Servicio_nombre: String,
      producto_Servicio_Codigo: String,
      producto_Caracteristicas: Option[String] = None
  )

  object ProductoServicio {
    def fromEntity(entity: ProductosServiciosEntidad): ProductoServicio = {
      ProductoServicio(
        producto_Servicio_Id = entity.producto_Servicio_Id,
        proceso_Id = entity.proceso_Id,
        producto_Servicio_Codigo = entity.product_Servicio_Codigo,
        producto_Servicio_nombre = entity.producto_Servicio_nombre,
        producto_Caracteristicas = entity.producto_Caracteristicas
      )
    }

    def toEntity(json: ProductoServicio) = {
      ProductosServiciosEntidad(
        producto_Servicio_Id = json.producto_Servicio_Id,
        proceso_Id = json.proceso_Id,
        product_Servicio_Codigo = json.producto_Servicio_Codigo,
        producto_Servicio_nombre = json.producto_Servicio_nombre,
        producto_Caracteristicas = json.producto_Caracteristicas
      )
    }
  }

  case class Caracterizacion(
      caraceterizacion_id: Option[Long] = None,
      proceso_Id: Option[Long] = None,
      procedimiento_Nombre: String,
      procedimiento_Codigo: String,
      procedimiento_Objetivo: Option[String] = None,
      documentosCaracterizacion: List[DocumentoCaracterizacion]
  )

  object Caracterizacion {
    def fromEntity(entity: ProcesoCaracterizacionesEntidad,
                   list: List[ProcesoDocumentosEntidad]) = {
      Caracterizacion(
        caraceterizacion_id = entity.caraceterizacion_id,
        proceso_Id = entity.proceso_Id,
        procedimiento_Nombre = entity.procedimiento_Nombre,
        procedimiento_Codigo = entity.procedimiento_Codigo,
        procedimiento_Objetivo = entity.procedimiento_Objetivo,
        documentosCaracterizacion =
          list.map(DocumentoCaracterizacion.fromEntity(_))
      )
    }

    def toEntity(json: Caracterizacion) = {
      ProcesoCaracterizacionesEntidad(
        caraceterizacion_id = json.caraceterizacion_id,
        proceso_Id = json.proceso_Id,
        procedimiento_Nombre = json.procedimiento_Nombre,
        procedimiento_Codigo = json.procedimiento_Codigo,
        procedimiento_Objetivo = json.procedimiento_Objetivo
      )
    }
  }

  case class DocumentoCaracterizacion(
      procedimiento_Documento_Id: Option[Long] = None,
      caraceterizacion_id: Option[Long] = None,
      procedimiento_Documento_Nombre: String,
      procedimiento_Documento_Descripcion: String,
      procedimiento_Documento_Codigo: String,
      procedimiento_Documento_Arch: String
  )

  object DocumentoCaracterizacion {
    def fromEntity(
        entity: ProcesoDocumentosEntidad): DocumentoCaracterizacion = {
      DocumentoCaracterizacion(
        procedimiento_Documento_Id = entity.procedimiento_Documento_Id,
        caraceterizacion_id = entity.caraceterizacion_id,
        procedimiento_Documento_Nombre = entity.procedimiento_Documento_Nombre,
        procedimiento_Documento_Descripcion =
          entity.procedimiento_Documento_Descripcion,
        procedimiento_Documento_Codigo = entity.procedimiento_Documento_Codigo,
        procedimiento_Documento_Arch = entity.procedimiento_Documento_Arch
      )
    }

    def toEntity(json: DocumentoCaracterizacion): ProcesoDocumentosEntidad = {
      ProcesoDocumentosEntidad(
        procedimiento_Documento_Id = json.procedimiento_Documento_Id,
        caraceterizacion_id = json.caraceterizacion_id,
        procedimiento_Documento_Nombre = json.procedimiento_Documento_Nombre,
        procedimiento_Documento_Descripcion =
          json.procedimiento_Documento_Descripcion,
        procedimiento_Documento_Codigo = json.procedimiento_Documento_Codigo,
        procedimiento_Documento_Arch = json.procedimiento_Documento_Arch
      )
    }
  }

  case class ProcesoProcedimientoJson(
      proceso: Proceso,
      productoServicios: List[ProductoServicio],
      caracterizaciones: List[Caracterizacion]
  )

}
