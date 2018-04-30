package com.amapola.strategos.core.procesos.http

package object json {

  case class Proceso(
      procesoSubProceso: String,
      codigoProceso: String,
      descripcion: String,
      tipoProceso: Int,
      responsable: String,
      nombreDocumento: String
  )

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
      documentoAnexo: String,
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
