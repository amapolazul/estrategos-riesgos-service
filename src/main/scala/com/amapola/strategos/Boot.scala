package com.amapola.strategos

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.amapola.strategos.core.declaracion_riesgos.http.rutas._
import com.amapola.strategos.core.declaracion_riesgos.persistencia.daos._
import com.amapola.strategos.core.declaracion_riesgos.servicios._
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.http.rutas.{
  EjerciciosEvaluacionesEstatusRutas,
  EjerciciosEvaluacionesRiesgosRutas
}
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.daos.{
  EjerciciosEvaluacionEstatusDaoImpl,
  EjerciciosEvaluacionRiesgosDaoImpl
}
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.servicios.{
  EjerciciosEvaluacionesEstatusServiceImpl,
  EjerciciosEvaluacionesRiesgosServiceImpl
}
import com.amapola.strategos.core.procesos.http.rutas.ProcesosRutas
import com.amapola.strategos.core.procesos.persistencia.daos.{
  CaracterizacionDaoImpl,
  DocumentosCaracterizacionDaoImpl,
  ProcesosDaoImpl,
  ProductosServiciosDaoImpl
}
import com.amapola.strategos.core.procesos.servicios.ProcesosServiciosImpl
import com.amapola.strategos.core.responsables.http.rutas.ResponsablesRutas
import com.amapola.strategos.core.responsables.persistencia.daos.ResponsablesDaoImpl
import com.amapola.strategos.core.responsables.servicios.ResponsablesServiceImpl
import com.amapola.strategos.core.tablas_sistema.http.rutas._
import com.amapola.strategos.core.tablas_sistema.persistencia.daos._
import com.amapola.strategos.core.tablas_sistema.servicios._
import com.amapola.strategos.utils.db.DatabaseConnector
import me.archdev.restapi.utils.Config
import me.archdev.restapi.utils.db.DatabaseMigrationManager

import scala.concurrent.ExecutionContext

object Boot extends App {

  def startApplication() = {
    implicit val actorSystem = ActorSystem()
    implicit val executor: ExecutionContext = actorSystem.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    val config = Config.load()

    new DatabaseMigrationManager(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    ).migrateDatabaseSchema()

    val databaseConnector = new DatabaseConnector(
      config.database.jdbcUrl,
      config.database.username,
      config.database.password
    )

    //Daos
    val caracterizacionDao = new CaracterizacionDaoImpl(databaseConnector)
    val procesosDao = new ProcesosDaoImpl(databaseConnector)
    val productosServiciosDao = new ProductosServiciosDaoImpl(databaseConnector)
    val documentosCaracterizacionDao = new DocumentosCaracterizacionDaoImpl(
      databaseConnector)
    val impactoRiesgoDao = new ImpactoRiesgosDaoImpl(databaseConnector)
    val causasRiesgosDao = new CausasRiesgosDaoImpl(databaseConnector)
    val probabilidadRiesgosDao = new ProbabilidadRiesgosDaoImpl(
      databaseConnector)
    val tipoRiesgosDao = new TipoRiesgosDaoImpl(databaseConnector)

    val responsablesDao = new ResponsablesDaoImpl(databaseConnector)

    val calificacionRiesgosDao = new CalificacionesRiesgosDaoImpl(
      databaseConnector)

    val causasRiesgosServiceImpl = new CausasRiesgosServiceImpl(
      causasRiesgosDao)

    val ejercicioEvaluacionEstadosDao = new EjerciciosEvaluacionEstatusDaoImpl(
      databaseConnector)
    val ejercicioEvaluacionRiesgosDao = new EjerciciosEvaluacionRiesgosDaoImpl(
      databaseConnector)

    val efectividadRiesgosDao = new EfectividadRiesgosDaoImpl(databaseConnector)

    val respuestaRiesgosDao = new RespuestaRiesgosDaoImpl(databaseConnector)

    val causasDeclaracionDao = new CausasDeclaracionDaoImpl(databaseConnector)
    val controlesDeclaracionDao = new ControlesDeclaracionDaoImpl(
      databaseConnector)
    val efectosDeclaracionDao = new EfectosDeclaracionDaoImpl(databaseConnector)
    val declaracionRiesgosEstatusDao = new DeclaracionRiesgosEstatusDaoImpl(
      databaseConnector)
    val declaracionRiesgosDao = new DeclaracionRiesgosDaoImpl(databaseConnector)

    //Servicios
    val impactoRiesgoService = new ImpactoRiesgosServiceImpl(impactoRiesgoDao)
    val probabildadRiesgoService = new ProbabilidadRiesgoServiceImpl(
      probabilidadRiesgosDao)

    val tipoRiesgosService = new TipoRiesgoServiceImpl(tipoRiesgosDao)

    val responsablesService = new ResponsablesServiceImpl(responsablesDao)

    val calificacionRiesgoService = new CalificacionRiesgosServiceImpl(
      calificacionRiesgosDao)

    val procesosService = new ProcesosServiciosImpl(
      caracterizacionDao,
      procesosDao,
      productosServiciosDao,
      documentosCaracterizacionDao)

    val ejerciciosEstadosService = new EjerciciosEvaluacionesEstatusServiceImpl(
      ejercicioEvaluacionEstadosDao)

    val ejerciciosRiesgosService = new EjerciciosEvaluacionesRiesgosServiceImpl(
      ejercicioEvaluacionRiesgosDao)

    val efectividadRiesgosService = new EfectividadRiesgosServiceImpl(
      efectividadRiesgosDao)

    val respuestasRiesgosService = new RespuestasRiesgosServiceImpl(
      respuestaRiesgosDao)

    val causasDeclaracionService = new CausasDeclaracionServiceImpl(
      causasDeclaracionDao)
    val controlesDeclaracionService = new ControlesDeclaracionServiceImpl(
      controlesDeclaracionDao)
    val efectosDeclaracionService = new EfectosDeclaracionServiceImpl(
      efectosDeclaracionDao)
    val declaracionRiesgosEstatusService =
      new DeclaracionRiesgosEstatusServiceImpl(declaracionRiesgosEstatusDao)
    val declaracionRiesgosService = new DeclaracionRiesgoServiceImpl(
      declaracionRiesgosDao,
      causasDeclaracionService,
      efectosDeclaracionService,
      controlesDeclaracionService,
      ejerciciosRiesgosService,
      calificacionRiesgoService)

    //Rutas
    val procesosRutes =
      new ProcesosRutas(procesosService, config.archivos.directorio)

    val responsablesRoute = new ResponsablesRutas(responsablesService)

    val tiposRiesgosRoute = new TiposRiesgosRoute(tipoRiesgosService)

    val causasRiesgosRoute = new CausasRiesgosRoute(causasRiesgosServiceImpl)

    val impactoRiesgosRoute = new ImpactoRiesgosRoute(impactoRiesgoService)

    val probabilidadRiesgoRoute = new ProbabilidadRiesgosRoute(
      probabildadRiesgoService)

    val calificacionRiesgoRoute = new CalificacionRiesgosRoute(
      calificacionRiesgoService)

    val ejerciciosEstadosRoute = new EjerciciosEvaluacionesEstatusRutas(
      ejerciciosEstadosService)
    val ejerciciosRutasRoute = new EjerciciosEvaluacionesRiesgosRutas(
      ejerciciosRiesgosService)

    val efectividadRiesgoRoute = new EfectividadRiesgosRoute(
      efectividadRiesgosService)

    val respuestasRiesgoRoute = new RespuestasRiesgoRoute(
      respuestasRiesgosService)

    val causasDeclaracionRoute = new CausasDeclaracionRoute(
      causasDeclaracionService)
    val controlesDeclaracionRoute = new ControlesDeclaracionRoute(
      controlesDeclaracionService)
    val efectosDeclaracionRoute = new EfectosDeclaracionRoute(
      efectosDeclaracionService)
    val declaracionRiesgosEstatusRoute = new DeclaracionRiesgoEstatusRoute(
      declaracionRiesgosEstatusService)
    val declaracionRiesgosRoute = new DeclaracionRiesgosRoute(
      declaracionRiesgosService)

    val routes = causasRiesgosRoute.getPaths ~
      impactoRiesgosRoute.getPaths ~
      probabilidadRiesgoRoute.getPaths ~
      tiposRiesgosRoute.getPaths ~
      procesosRutes.getPaths ~
      responsablesRoute.getPaths ~
      calificacionRiesgoRoute.getPaths ~
      ejerciciosEstadosRoute.getPaths ~
      ejerciciosRutasRoute.getPaths ~
      efectividadRiesgoRoute.getPaths ~
      respuestasRiesgoRoute.getPaths ~
      causasDeclaracionRoute.getPaths ~
      controlesDeclaracionRoute.getPaths ~
      efectosDeclaracionRoute.getPaths ~
      declaracionRiesgosEstatusRoute.getPaths ~
      declaracionRiesgosRoute.getPaths

    Http().bindAndHandle(routes, config.http.host, config.http.port)
  }

  startApplication()

}
