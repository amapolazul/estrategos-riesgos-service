package com.amapola.strategos.core.declaracion_riesgos.http.rutas

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{
  pathEndOrSingleSlash,
  pathPrefix,
  _
}
import akka.http.scaladsl.server.PathMatchers.LongNumber
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import com.amapola.strategos.core.declaracion_riesgos.http.json.DeclaracionRiesgosRequestJson
import com.amapola.strategos.core.declaracion_riesgos.servicios.DeclaracionRiesgoService
import com.amapola.strategos.utils.http.StrategosCorsSettings
import com.amapola.strategos.utils.logs_auditoria.servicios.LogsAuditoriaService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class DeclaracionRiesgosRoute(
    declaracionRiesgoService: DeclaracionRiesgoService)(
    implicit executionContext: ExecutionContext,
    logsAuditoriaService: LogsAuditoriaService)
    extends FailFastCirceSupport
    with StrategosCorsSettings {

  /**
    * Devuelve las rutas asociadas al dominio ejercicio estados
    * @return
    */
  def getPaths = cors(settings) {
    pathPrefix("declaracion-riesgos") {
      traerRiesgosPorEjercicioId() ~
        traerRiesgosPendientesPorProcesoId() ~
        traerRiesgoCompletoPorId() ~
        crearDeclaracionRiesgo() ~
        actualizarDeclaracionRiesgo() ~
        borrarDeclaracionRiesgo()

    }
  }

  /**
    * Consulta los riesgos por id de ejercicio enviado por parametro
    * @return
    */
  private def traerRiesgosPorEjercicioId() = pathEndOrSingleSlash {
    get {
      parameter('ejercicioId) { ejercicioId =>
        onComplete(
          declaracionRiesgoService.listarDeclaracionesRiesgoPorEjercicioId(
            ejercicioId.toLong)) {
          case Success(result) =>
            complete(StatusCodes.OK, result.asJson)
          case Failure(ex) =>
            logsAuditoriaService.error(
              "Ha ocurrido un error en traerRiesgosPorEjercicioId",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError,
                     s"Ha ocurrido un error. ${ex.getMessage}")
        }
      }
    }
  }

  /**
    * Trae los riegos pendientes asociados a un proceso
    * @return
    */
  private def traerRiesgosPendientesPorProcesoId() = pathEndOrSingleSlash {
    get {
      parameter('procesoId) { procesoId =>
        onComplete(declaracionRiesgoService
          .listarDeclaracionesRiesgoPendientesPorProcesoId(procesoId.toLong)) {
          case Success(result) =>
            complete(StatusCodes.OK, result.asJson)
          case Failure(ex) =>
            logsAuditoriaService.error(
              "Ha ocurrido un error en traerRiesgosPendientesPorProcesoId",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError,
                     s"Ha ocurrido un error. ${ex.getMessage}")
        }
      }
    }
  }

  /**
    * Trae un riesgo con su informacion completa dado su id
    * @return
    */
  private def traerRiesgoCompletoPorId() = pathPrefix(LongNumber) { riesgoId =>
    pathEndOrSingleSlash {
      get {
        onComplete(
          declaracionRiesgoService.traerDeclaracionRiesgoPorId(riesgoId)) {
          case Success(result) =>
            result
              .map(x => complete(StatusCodes.OK, x.asJson))
              .getOrElse(
                complete(StatusCodes.NotFound, "Registro no encontrado"))

          case Failure(ex) =>
            logsAuditoriaService.error(
              "Ha ocurrido un error en traerRiesgoCompletoPorId",
              this.getClass.toString,
              ex)
            complete(StatusCodes.InternalServerError,
                     s"Ha ocurrido un error. ${ex.getMessage}")
        }
      }
    }
  }

  /**
    * Cre una declaracion riesgo completa con causas, efectos y controles
    * @return
    */
  private def crearDeclaracionRiesgo() = {
    pathEndOrSingleSlash {
      post {
        entity(as[DeclaracionRiesgosRequestJson]) { entity =>
          onComplete(declaracionRiesgoService.crearDeclaracionRiesgo(entity)) {
            case Success(result) =>
              complete(StatusCodes.OK,
                       s"Registro creado correctamente: ${result}")
            case Failure(ex) =>
              logsAuditoriaService.error(
                "Ha ocurrido un error guardando la declaracion de riesgo",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError,
                       s"Ha ocurrido un error: ${ex.getMessage}")
          }
        }
      }
    }
  }

  /**
    * Actualiza una declaracion riesgo dado su id y la información completa
    * @return
    */
  private def actualizarDeclaracionRiesgo() = {
    pathPrefix(LongNumber) { riesgoId =>
      pathEndOrSingleSlash {
        put {
          entity(as[DeclaracionRiesgosRequestJson]) { entity =>
            onComplete(
              declaracionRiesgoService.editarDeclaracionRiesgo(riesgoId,
                                                               entity)) {
              case Success(result) =>
                if (result) {
                  complete(
                    StatusCodes.OK,
                    s"Registro actualizado correctamente correctamente: ${result}")
                } else complete(StatusCodes.NotFound, "Registro no encontrado")
              case Failure(ex) =>
                logsAuditoriaService.error(
                  "Ha ocurrido un error en actualizarDeclaracionRiesgo",
                  this.getClass.toString,
                  ex)
                complete(StatusCodes.InternalServerError,
                         s"Ha ocurrido un error: ${ex.getMessage}")
            }
          }
        }
      }
    }
  }

  /**
    * Borra un registro de declaracion riesgo dado su id
    * @return
    */
  private def borrarDeclaracionRiesgo() = {
    pathPrefix(LongNumber) { riesgoId =>
      pathEndOrSingleSlash {
        delete {
          onComplete(
            declaracionRiesgoService.borrarDeclaracionRiesgos(riesgoId)) {
            case Success(result) =>
              val (elimCausas, elimEfectos, elimControles, elimRiesgo) = result
              val stringBuilder = new StringBuilder()

              if (!elimCausas)
                stringBuilder.append(
                  "Error al eliminar las causas del riesgo\n")
              if (!elimEfectos)
                stringBuilder.append(
                  "Error al eliminar los efectos del riesgo\n")
              if (!elimControles)
                stringBuilder.append(
                  "Error al eliminar los controles del riesgo\n")
              if (!elimRiesgo)
                stringBuilder.append("Error al eliminar el riesgo")
              if (elimCausas && elimEfectos && elimControles && elimRiesgo) {
                complete(
                  StatusCodes.OK,
                  s"Registro borrado correctamente correctamente: ${result}")
              } else {
                complete(StatusCodes.InternalServerError,
                         stringBuilder.toString())
              }
            case Failure(ex) =>
              logsAuditoriaService.error(
                "Ha ocurrido un error en borrarDeclaracionRiesgo()",
                this.getClass.toString,
                ex)
              complete(StatusCodes.InternalServerError,
                       s"Ha ocurrido un error: ${ex.getMessage}")
          }
        }
      }
    }
  }
}
