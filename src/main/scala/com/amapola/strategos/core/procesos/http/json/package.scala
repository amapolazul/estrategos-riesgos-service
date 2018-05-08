package com.amapola.strategos.core.procesos.http

import com.amapola.strategos.core.procesos.persistencia.entidades.ProcesosEntidad

package object json {

  case class Proceso(
      procesoSubProceso: String,
      codigoProceso: String,
      descripcion: String,
      tipoProceso: Long,
      responsable: Long,
      nombreDocumento: String,
      procesoPadre: Long
  )

  object Proceso {
    def asProcesoEntitidad(proceso: Proceso): ProcesosEntidad = {
      ProcesosEntidad(
        proceso_Padre_Id = Some(proceso.procesoPadre),
        proceso_Nombre = proceso.procesoSubProceso,
        proceso_Codigo = proceso.codigoProceso,
        proceso_Tipo = proceso.tipoProceso,
        proceso_Responsable_Id = proceso.responsable,
        proceso_Documento = proceso.nombreDocumento
      )
    }
  }

  case class ProductoServicio(
      proceso: String,
      productoServicio: String,
      caracteristicas: String
  )


  case class Caracterizacion(
      proceso: String,
      procedimiento: String,
      codigoProcedimiento: String,
      documentoSoporte: String,
      documentosCaracterizacion: List[DocumentoCaracterizacion]
  )

  case class DocumentoCaracterizacion(
      nombreDocumento: String,
      codigoDocumento: String,
      descripcion: String,
      anexo: String
  )

  case class ProcesoProcedimientoRequest(
      proceso: Proceso,
      productoServicios: List[ProductoServicio],
      caracterizaciones: List[Caracterizacion]
  )

}
