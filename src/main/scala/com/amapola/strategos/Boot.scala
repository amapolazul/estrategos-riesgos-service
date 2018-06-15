package me.archdev.restapi

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.amapola.strategos.core.procesos.http.rutas.ProcesosRutas
import com.amapola.strategos.core.procesos.persistencia.daos.{
  CaracterizacionDaoImpl,
  DocumentosCaracterizacionDaoImpl,
  ProcesosDaoImpl,
  ProductosServiciosDaoImpl
}
import com.amapola.strategos.core.procesos.servicios.ProcesosServiciosImpl
import com.amapola.strategos.core.tablas_sistema.http.rutas.{
  CausasRiesgosRoute,
  ImpactoRiesgosRoute,
  ProbabilidadRiesgosRoute,
  TiposRiesgosRoute
}
import com.amapola.strategos.core.tablas_sistema.persistencia.daos.{
  CausasRiesgosDaoImpl,
  ImpactoRiesgosDaoImpl,
  ProbabilidadRiesgosDaoImpl,
  TipoRiesgosDaoImpl
}
import com.amapola.strategos.core.tablas_sistema.servicios._
import com.amapola.strategos.utils.db.DatabaseConnector
import me.archdev.restapi.utils.Config
import me.archdev.restapi.utils.db.DatabaseMigrationManager
import akka.http.scaladsl.server.Directives._
import com.amapola.strategos.core.responsables.http.rutas.ResponsablesRutas
import com.amapola.strategos.core.responsables.persistencia.daos.ResponsablesDaoImpl
import com.amapola.strategos.core.responsables.servicios.ResponsablesServiceImpl

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

    val procesosService = new ProcesosServiciosImpl(
      caracterizacionDao,
      procesosDao,
      productosServiciosDao,
      documentosCaracterizacionDao)

    val causasRiesgosServiceImpl = new CausasRiesgosServiceImpl(
      causasRiesgosDao)

    val impactoRiesgoService = new ImpactoRiesgosServiceImpl(impactoRiesgoDao)
    val probabildadRiesgoService = new ProbabilidadRiesgoServiceImpl(
      probabilidadRiesgosDao)

    val tipoRiesgosService = new TipoRiesgoServiceImpl(tipoRiesgosDao)

    val procesosRutes =
      new ProcesosRutas(procesosService, config.archivos.directorio)

    val responsablesService = new ResponsablesServiceImpl(responsablesDao)

    val causasRiesgosRoute = new CausasRiesgosRoute(causasRiesgosServiceImpl)

    val impactoRiesgosRoute = new ImpactoRiesgosRoute(impactoRiesgoService)

    val probabilidadRiesgoRoute = new ProbabilidadRiesgosRoute(
      probabildadRiesgoService)

    val responsablesRoute = new ResponsablesRutas(responsablesService)

    val tiposRiesgosRoute = new TiposRiesgosRoute(tipoRiesgosService)

    val routes = causasRiesgosRoute.getPaths ~
      impactoRiesgosRoute.getPaths ~
      probabilidadRiesgoRoute.getPaths ~
      tiposRiesgosRoute.getPaths ~
      procesosRutes.getPaths ~
      responsablesRoute.getPaths

    Http().bindAndHandle(routes, config.http.host, config.http.port)
  }

  startApplication()

}
