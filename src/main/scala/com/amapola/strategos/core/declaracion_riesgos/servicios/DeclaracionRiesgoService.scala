package com.amapola.strategos.core.declaracion_riesgos.servicios

import com.amapola.strategos.core.declaracion_riesgos.http.json._
import com.amapola.strategos.core.declaracion_riesgos.persistencia.daos.DeclaracionRiesgosDao
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.servicios.EjerciciosEvaluacionesRiesgosService
import com.amapola.strategos.core.tablas_sistema.servicios.{
  CalificacionRiesgosService,
  CausasRiesgosService
}

import scala.concurrent.{ExecutionContext, Future}

trait DeclaracionRiesgoService {

  /**
    * Crea una declaracion de riesgo completa. Dentro las causas efectos y controles para la declaracion de riesgo
    *
    * @param request
    * @return
    */
  def crearDeclaracionRiesgo(
      request: DeclaracionRiesgosRequestJson): Future[Long]

  /**
    * Edita la declaracion de riesgo
    * @param id
    * @param declaracionRiesgo
    * @return
    */
  def editarDeclaracionRiesgo(
      id: Long,
      declaracionRiesgo: DeclaracionRiesgosRequestJson): Future[Boolean]

  /**
    * Borra la declaracion de riesgo por su id
    * @param id
    * @return
    */
  def borrarDeclaracionRiesgos(
      id: Long): Future[(Boolean, Boolean, Boolean, Boolean)]

  /**
    * Lista todas las declaraciones de riesgo por un ejercicio de evaluacion de riesgo
    * @param ejercicioId
    * @return
    */
  def listarDeclaracionesRiesgoPorEjercicioId(
      ejercicioId: Long): Future[List[DeclaracionRiesgosJson]]

  /**
    * Lista las declaraciones de riesgo por el proceso asociado
    * @param procesoId
    * @return
    */
  def listarDeclaracionesRiesgoPendientesPorProcesoId(
      procesoId: Long): Future[List[DeclaracionRiesgosJson]]

  /**
    *
    * @param declaracionRiesgoId
    * @return
    */
  def traerDeclaracionRiesgoPorId(
      declaracionRiesgoId: Long): Future[Option[DeclaracionRiesgosRequestJson]]

  /**
    * Consulta todas las declaraciones de riesgo de un ejercicio y las clasifica por la causa
    * @param ejercicioId
    */
  def traerDeclaracionesPorCausasDeRiesgo(
      ejercicioId: Long): Future[List[DeclaracionRiesgosCausa]]
}

class DeclaracionRiesgoServiceImpl(
    declaracionRiesgosDao: DeclaracionRiesgosDao,
    causasDeclaracionService: CausasDeclaracionService,
    efectosDeclaracionService: EfectosDeclaracionService,
    controlesDeclaracionService: ControlesDeclaracionService,
    ejerciciosEvaluacionesRiesgosService: EjerciciosEvaluacionesRiesgosService,
    calificacionRiesgosService: CalificacionRiesgosService,
    causasRiesgosService: CausasRiesgosService)(
    implicit executionContext: ExecutionContext)
    extends DeclaracionRiesgoService {

  /**
    * Crea una declaracion de riesgo completa. Dentro las causas efectos y controles para la declaracion de riesgo
    *
    * @param request
    * @return
    */
  override def crearDeclaracionRiesgo(
      request: DeclaracionRiesgosRequestJson): Future[Long] = {
    val declaracionEntity =
      DeclaracionRiesgosJson.toEntity(request.declaracionRiesgo)

    for {
      declaracionRiesgoId <- declaracionRiesgosDao.crearDeclaracionRiesgo(
        declaracionEntity)
      _ <- crearActualizarCausasDeclaracion(declaracionRiesgoId,
                                            request.causasDeclaracionRiesgo)
      _ <- crearActualizarEfectosDeclaracion(declaracionRiesgoId,
                                             request.efectosDeclaracionRiesgo)
      _ <- crearActualizarControlesDeclaracion(
        declaracionRiesgoId,
        request.controlesDeclaracionRiesgo)
    } yield declaracionRiesgoId
  }

  /**
    * Crea las causas de declaracion de riesgo
    * @param declaracionRiesgoId
    * @param causasList
    * @return
    */
  private def crearActualizarCausasDeclaracion(
      declaracionRiesgoId: Long,
      causasList: List[CausasDeclaracionRiesgosJson]): Future[List[Long]] = {
    val causasFuture = causasList.map(causa => {
      causa.id match {
        case Some(id) =>
          causasDeclaracionService
            .actualizarCausaDeclaracion(id, causa)
            .map(x => if (x) 1l else 0l)
        case None =>
          val causaConRiesgoId =
            causa.copy(declaracion_riesgo_id = Some(declaracionRiesgoId))
          causasDeclaracionService.crearCausaDeclaracionService(
            causaConRiesgoId)
      }
    })

    Future.sequence(causasFuture)
  }

  /**
    * Crea las efectos de declaracion de riesgo
    * @param declaracionRiesgoId
    * @param efectosList
    * @return
    */
  private def crearActualizarEfectosDeclaracion(
      declaracionRiesgoId: Long,
      efectosList: List[EfectosDeclaracionRiesgosJson]): Future[List[Long]] = {
    val efectosFuture = efectosList.map(efecto => {
      efecto.id match {
        case Some(id) =>
          efectosDeclaracionService
            .actualizarEfectoDeclaracion(id, efecto)
            .map(x => if (x) 1l else 0l)
        case None =>
          val efectosConRiesgoId =
            efecto.copy(declaracion_riesgo_id = Some(declaracionRiesgoId))
          efectosDeclaracionService.crearEfectoDeclaracionService(
            efectosConRiesgoId)
      }
    })

    Future.sequence(efectosFuture)
  }

  /**
    * Crea las efectos de declaracion de riesgo
    * @param declaracionRiesgoId
    * @param controlesList
    * @return
    */
  private def crearActualizarControlesDeclaracion(
      declaracionRiesgoId: Long,
      controlesList: List[ControlesDeclaracionRiesgosJson])
    : Future[List[Long]] = {
    val controlesFuture = controlesList.map(control => {
      control.id match {
        case Some(id) =>
          controlesDeclaracionService
            .actualizarControlDeclaracion(id, control)
            .map(x => if (x) 1l else 0l)
        case None =>
          val controlConRiesgoId =
            control.copy(declaracion_riesgo_id = Some(declaracionRiesgoId))
          controlesDeclaracionService.crearControlDeclaracionService(
            controlConRiesgoId)
      }
    })

    Future.sequence(controlesFuture)
  }

  /**
    * Edita la declaracion de riesgo
    *
    * @param id
    * @param declaracionRiesgo
    * @return
    */
  override def editarDeclaracionRiesgo(
      id: Long,
      declaracionRiesgo: DeclaracionRiesgosRequestJson): Future[Boolean] = {
    val declaracionEntity =
      DeclaracionRiesgosJson.toEntity(declaracionRiesgo.declaracionRiesgo)

    for {
      riesgoActualizacion <- declaracionRiesgosDao.actualizarDeclaracionRiesgo(
        id,
        declaracionEntity)
      causaAct <- crearActualizarCausasDeclaracion(
        id,
        declaracionRiesgo.causasDeclaracionRiesgo)
      efectpAct <- crearActualizarEfectosDeclaracion(
        id,
        declaracionRiesgo.efectosDeclaracionRiesgo)
      controlAct <- crearActualizarControlesDeclaracion(
        id,
        declaracionRiesgo.controlesDeclaracionRiesgo)
      efectoBor <- eliminarEfectosDeclaracion(declaracionRiesgo.efectosEliminar)
      causasBor <- eliminarCausasDeclaracion(declaracionRiesgo.causasEliminar)
      controlBor <- eliminarControlesDeclaracion(
        declaracionRiesgo.controlesEliminar)
    } yield {
      riesgoActualizacion && !causaAct.exists(_ == 0) && !efectpAct.exists(
        _ == 0l) && !controlAct.exists(_ == 0l) && efectoBor && causasBor && controlBor
    }
  }

  /**
    * Elimina los efectos de una declaracion de riesgo
    * @param listOpt
    * @return
    */
  private def eliminarEfectosDeclaracion(listOpt: Option[List[Long]]) = {
    listOpt match {
      case Some(efectos) =>
        val borrarFuture =
          efectos.map(x => efectosDeclaracionService.borrarEfectoDeclaracion(x))
        Future.sequence(borrarFuture).map(x => x.reduce(_ && _))
      case None =>
        Future.successful(true)
    }
  }

  /**
    * Elimina las causas de una declaracion de riesgo
    * @param listOpt
    * @return
    */
  private def eliminarCausasDeclaracion(listOpt: Option[List[Long]]) = {
    listOpt match {
      case Some(causas) =>
        val borrarFuture =
          causas.map(x => causasDeclaracionService.borrarCausaDeclaracion(x))
        Future.sequence(borrarFuture).map(x => x.reduce(_ && _))
      case None =>
        Future.successful(true)
    }
  }

  /**
    * Elimina las controles de una declaracion de riesgo
    * @param listOpt
    * @return
    */
  private def eliminarControlesDeclaracion(listOpt: Option[List[Long]]) = {
    listOpt match {
      case Some(controles) =>
        val borrarFuture = controles.map(x =>
          controlesDeclaracionService.borrarControlDeclaracion(x))
        Future.sequence(borrarFuture).map(x => x.reduce(_ && _))
      case None =>
        Future.successful(true)
    }
  }

  /**
    * Borra la declaracion de riesgo por su id
    *
    * @param id
    * @return
    */
  override def borrarDeclaracionRiesgos(
      id: Long): Future[(Boolean, Boolean, Boolean, Boolean)] = {
    for {
      elimCausas <- causasDeclaracionService.borrarCausasDeclaracionPorRiesgoId(
        id)
      elimEfectos <- efectosDeclaracionService
        .borrarEfectosDeclaracionPorRiesgoId(id)
      elimControles <- controlesDeclaracionService
        .borrarControlesDeclaracionPorRiesgoId(id)
      elimRiesgo <- declaracionRiesgosDao.eliminarDeclaracionRiesgo(id)
    } yield (elimCausas, elimEfectos, elimControles, elimRiesgo)
  }

  /**
    * Lista todas las declaraciones de riesgo por un ejercicio de evaluacion de riesgo
    *
    * @param ejercicioId
    * @return
    */
  override def listarDeclaracionesRiesgoPorEjercicioId(
      ejercicioId: Long): Future[List[DeclaracionRiesgosJson]] = {
    declaracionRiesgosDao
      .traerDeclaracionesRiesgoPorEjercicioEvaluacionId(ejercicioId)
      .flatMap(list => {
        val futureList = list
          .map(x => {
            for {
              calificacion <- calificacionRiesgosService
                .consultarCalifiacionRiesgoPorSeveridad(x.severidad.toLong)
              ejercicio <- ejerciciosEvaluacionesRiesgosService
                .traerEjercicioEvaluacionPorId(x.ejercicio_riesgo_id)
            } yield {
              val declaracion = DeclaracionRiesgosJson.fromEntity(x)
              val fechaEjercicio = ejercicio.map(_.fecha_creacion_ejercicio)
              val color = calificacion match {
                case Some(e) => Some(e.color)
                case None    => Some("Rojo")
              }
              declaracion.copy(fecha_ejercicio = fechaEjercicio,
                               calificacion_riesgo = color)
            }
          })
          .toList
        Future.sequence(futureList)
      })
  }

  /**
    * Lista las declaraciones de riesgo por el proceso asociado
    *
    * @param procesoId
    * @return
    */
  override def listarDeclaracionesRiesgoPendientesPorProcesoId(
      procesoId: Long): Future[List[DeclaracionRiesgosJson]] = {

    declaracionRiesgosDao
      .traerDeclaracionesRiesgoPendientesPorProcesoId(procesoId)
      .flatMap(list => {
        val futureList = list
          .map(x => {
            for {
              calificacion <- calificacionRiesgosService
                .consultarCalifiacionRiesgoPorSeveridad(x.severidad.toLong)
              ejercicio <- ejerciciosEvaluacionesRiesgosService
                .traerEjercicioEvaluacionPorId(x.ejercicio_riesgo_id)
            } yield {
              val declaracion = DeclaracionRiesgosJson.fromEntity(x)
              val fechaEjercicio = ejercicio.map(_.fecha_creacion_ejercicio)
              val color = calificacion match {
                case Some(e) => Some(e.color)
                case None    => Some("Rojo")
              }
              declaracion.copy(fecha_ejercicio = fechaEjercicio,
                               calificacion_riesgo = color)
            }
          })
          .toList
        Future.sequence(futureList)
      })

  }

  /**
    * Consulta la declaracion riesgo por un id
    * @param declaracionRiesgoId
    * @return
    */
  override def traerDeclaracionRiesgoPorId(declaracionRiesgoId: Long)
    : Future[Option[DeclaracionRiesgosRequestJson]] = {
    for {
      declaracionRiesgo <- declaracionRiesgosDao.traerDeclaracionRiesgoPorId(
        declaracionRiesgoId)
      causasRiesgo <- causasDeclaracionService
        .listarCausasDeclaracionPorRiesgoId(declaracionRiesgoId)
      efectosRiesgo <- efectosDeclaracionService
        .listarEfectosDeclaracionPorRiesgoId(declaracionRiesgoId)
      controlesRiesgo <- controlesDeclaracionService
        .listarControlesDeclaracionPorRiesgoId(declaracionRiesgoId)
    } yield {
      declaracionRiesgo.map(declaracion => {
        DeclaracionRiesgosRequestJson(
          DeclaracionRiesgosJson.fromEntity(declaracion),
          causasRiesgo,
          efectosRiesgo,
          controlesRiesgo
        )
      })
    }
  }

  /**
    * Consulta todas las declaraciones de riesgo de un ejercicio y las clasifica por la causa
    *
    * @param ejercicioId
    */
  override def traerDeclaracionesPorCausasDeRiesgo(
      ejercicioId: Long): Future[List[DeclaracionRiesgosCausa]] = {
    val result = for {
      causas <- causasRiesgosService.traerCausasRiesgo()
      riesgosEjercicio <- listarDeclaracionesRiesgoPorEjercicioId(ejercicioId)
    } yield {
      val results = causas.map(causa => {
        val riesgosTotal = riesgosEjercicio.map(riesgo => {
          val riesgoId = riesgo.id.getOrElse(
            throw new Exception("Id de riesgo no encontrado"))
          causasDeclaracionService
            .listarCausasDeclaracionPorRiesgoId(riesgoId)
            .map(declaraciones => {
              declaraciones
                .count(_.causa == causa.id.getOrElse(
                  throw new Exception("Causa no encontrada")))
                .toLong
            })
        })

        Future.sequence(riesgosTotal).map(x => DeclaracionRiesgosCausa(causa.causa_riesgo, x.reduce(_ + _)))
      })
      Future.sequence(results)
    }
    result.flatten
  }
}
