package com.amapola.strategos.core.declaracion_riesgos.servicios

import com.amapola.strategos.core.declaracion_riesgos.http.json._
import com.amapola.strategos.core.declaracion_riesgos.persistencia.daos.DeclaracionRiesgosDao

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
      declaracionRiesgo: DeclaracionRiesgosJson): Future[Boolean]

  /**
    * Borra la declaracion de riesgo por su id
    * @param id
    * @return
    */
  def borrarDeclaracionRiesgos(id: Long): Future[Boolean]

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
}

class DeclaracionRiesgoServiceImpl(
    declaracionRiesgosDao: DeclaracionRiesgosDao,
    causasDeclaracionService: CausasDeclaracionService,
    efectosDeclaracionService: EfectosDeclaracionService,
    controlesDeclaracionService: ControlesDeclaracionService)(
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
      _ <- crearCausasDeclaracion(declaracionRiesgoId,
                                  request.causasDeclaracionRiesgo)
      _ <- crearEfectosDeclaracion(declaracionRiesgoId,
                                   request.efectosDeclaracionRiesgo)
      _ <- crearControlesDeclaracion(declaracionRiesgoId,
                                     request.controlesDeclaracionRiesgo)
    } yield declaracionRiesgoId
  }

  /**
    * Crea las causas de declaracion de riesgo
    * @param declaracionRiesgoId
    * @param causasList
    * @return
    */
  private def crearCausasDeclaracion(
      declaracionRiesgoId: Long,
      causasList: List[CausasDeclaracionRiesgosJson]): Future[List[Long]] = {
    val causasFuture = causasList.map(causa => {
      val causaConRiesgoId =
        causa.copy(declaracion_riesgo_id = Some(declaracionRiesgoId))
      causasDeclaracionService.crearCausaDeclaracionService(causaConRiesgoId)
    })

    Future.sequence(causasFuture)
  }

  /**
    * Crea las efectos de declaracion de riesgo
    * @param declaracionRiesgoId
    * @param efectosList
    * @return
    */
  private def crearEfectosDeclaracion(
      declaracionRiesgoId: Long,
      efectosList: List[EfectosDeclaracionRiesgosJson]): Future[List[Long]] = {
    val efectosFuture = efectosList.map(efecto => {
      val efectosConRiesgoId =
        efecto.copy(declaracion_riesgo_id = Some(declaracionRiesgoId))
      efectosDeclaracionService.crearEfectoDeclaracionService(
        efectosConRiesgoId)
    })

    Future.sequence(efectosFuture)
  }

  /**
    * Crea las efectos de declaracion de riesgo
    * @param declaracionRiesgoId
    * @param controlesList
    * @return
    */
  private def crearControlesDeclaracion(
      declaracionRiesgoId: Long,
      controlesList: List[ControlesDeclaracionRiesgosJson])
    : Future[List[Long]] = {
    val controlesFuture = controlesList.map(control => {
      val controlConRiesgoId =
        control.copy(declaracion_riesgo_id = Some(declaracionRiesgoId))
      controlesDeclaracionService.crearControlDeclaracionService(
        controlConRiesgoId)
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
      declaracionRiesgo: DeclaracionRiesgosJson): Future[Boolean] = {
    val entity = DeclaracionRiesgosJson.toEntity(declaracionRiesgo)
    declaracionRiesgosDao.actualizarDeclaracionRiesgo(id, entity)
  }

  /**
    * Borra la declaracion de riesgo por su id
    *
    * @param id
    * @return
    */
  override def borrarDeclaracionRiesgos(id: Long): Future[Boolean] = {
    declaracionRiesgosDao.eliminarDeclaracionRiesgo(id)
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
      .map(list => {
        list.map(DeclaracionRiesgosJson.fromEntity(_)).toList
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
      .map(list => {
        list.map(DeclaracionRiesgosJson.fromEntity(_)).toList
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
}
