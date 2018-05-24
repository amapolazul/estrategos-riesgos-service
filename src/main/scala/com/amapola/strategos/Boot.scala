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
import com.amapola.strategos.core.tablas_sistema.http.rutas.CausasRiesgosRoute
import com.amapola.strategos.core.tablas_sistema.persistencia.daos.CausasRiesgosDaoImpl
import com.amapola.strategos.core.tablas_sistema.servicios.CausasRiesgosServiceImpl
import com.amapola.strategos.utils.db.DatabaseConnector
import me.archdev.restapi.utils.Config
import me.archdev.restapi.utils.db.DatabaseMigrationManager

import akka.http.scaladsl.server.Directives._

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

    val causasRiesgosDao = new CausasRiesgosDaoImpl(databaseConnector)

    val procesosService = new ProcesosServiciosImpl(
      caracterizacionDao,
      procesosDao,
      productosServiciosDao,
      documentosCaracterizacionDao)

    val causasRiesgosServiceImpl = new CausasRiesgosServiceImpl(
      causasRiesgosDao)

    val procesosRutes =
      new ProcesosRutas(procesosService, config.archivos.directorio)

    val causasRiesgosRoute = new CausasRiesgosRoute(causasRiesgosServiceImpl)

    val routes = causasRiesgosRoute.getPaths

    Http().bindAndHandle(routes,
                         config.http.host,
                         config.http.port)
  }

  startApplication()

}
