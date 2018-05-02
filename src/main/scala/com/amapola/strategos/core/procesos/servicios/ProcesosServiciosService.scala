package com.amapola.strategos.core.procesos.servicios

import com.amapola.strategos.core.procesos.http.json.ProcesoProcedimientoRequest

sealed trait ProcesosServiciosService {

  def crearProcesos(request: ProcesoProcedimientoRequest)

}
