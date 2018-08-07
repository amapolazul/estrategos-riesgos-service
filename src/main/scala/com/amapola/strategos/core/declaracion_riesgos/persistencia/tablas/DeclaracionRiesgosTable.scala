package com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.{DeclaracionRiesgosEntidad, EstatusRiesgosEntidad}
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.entidades.EjerciciosEvaluacionRiesgosEntidad
import com.amapola.strategos.core.ejercicios_evaluacion_riesgos.persistencia.tablas.EjerciciosEvaluacionRiesgosTable
import com.amapola.strategos.core.procesos.persistencia.entidades.ProcesosEntidad
import com.amapola.strategos.core.procesos.persistencia.tablas.ProcesosTable
import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.{RespuestasRiesgosEntidad, TipoRiesgosEntidad}
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.{RespuestasRiesgosTable, TipoRiesgosTable}
import com.amapola.strategos.utils.db.DatabaseConnector
import slick.lifted.ForeignKeyQuery

/**
  * Clase que mapea la tabla declaracion_riegos a la base de datos
  */
private[declaracion_riesgos] trait DeclaracionRiesgosTable extends ProcesosTable
  with EjerciciosEvaluacionRiesgosTable
  with TipoRiesgosTable
  with DeclaracionRiesgosEstatusTable
  with RespuestasRiesgosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class DeclaracionRiesgos(tag: Tag) extends Table[DeclaracionRiesgosEntidad](tag, "declaracion_riesgos") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def proceso_id = column[Long]("proceso_id")
    def ejercicio_riesgo_id = column[Long]("ejercicio_riesgo_id")
    def tipo_riesgo_id = column[Long]("tipo_riesgo_id")
    def respuesta_riesgo_id = column[Long]("respuesta_riesgo_id")
    def estatus_riesgo_id = column[Long]("estatus_riesgo_id")
    def factor_riesgo = column[String]("factor_riesgo")
    def descripcion = column[String]("descripcion")
    def efectividad_controles = column[String]("efectividad_controles")
    def probabilidad = column[String]("probabilidad")
    def historico = column[Boolean]("historico")
    def impacto = column[String]("impacto")
    def severidad = column[String]("severidad")
    def riesgo_residual = column[String]("riesgo_residual")
    def fecha_creacion = column[Long]("fecha_creacion")
    def fecha_actualizacion = column[Long]("fecha_actualizacion")

    def procesoDeclaracionRiesgosFk: ForeignKeyQuery[Procesos, ProcesosEntidad] =
      foreignKey("PROCESO_DECLARACION_RIESGO_FK", proceso_id, procesos)(
        _.procesoId,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def ejercicioRiesgoIdFk: ForeignKeyQuery[EjerciciosEvaluacionRiesgos, EjerciciosEvaluacionRiesgosEntidad] =
      foreignKey("EJERCICIO_DECLARACION_RIESGO_FK", ejercicio_riesgo_id, ejerciciosEvaluacionRiesgos)(
        _.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def tipoRiesgoIdFk: ForeignKeyQuery[TiposRiesgos, TipoRiesgosEntidad] =
      foreignKey("TIPO_RIESGO_DECLARACION_RIESGO_FK", tipo_riesgo_id, tiposRiesgos)(
        _.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def respuestaRiesgoIdFk: ForeignKeyQuery[RespuestasRiesgos, RespuestasRiesgosEntidad] =
      foreignKey("RESPUESTA_RIESGO_FK", respuesta_riesgo_id, respuestasRiesgos)(
        _.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def estatusRiesgoFk: ForeignKeyQuery[DeclaracionRiesgosEstatus, EstatusRiesgosEntidad] =
      foreignKey("ESTATUS_RIESGO_FK", estatus_riesgo_id, estatusRiesgo)(
        _.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    override def * = (
      id.?,
      proceso_id,
      ejercicio_riesgo_id,
      tipo_riesgo_id,
      respuesta_riesgo_id,
      estatus_riesgo_id,
      factor_riesgo,
      descripcion.?,
      efectividad_controles,
      probabilidad,
      historico,
      impacto,
      severidad,
      riesgo_residual,
      fecha_creacion,
      fecha_actualizacion) <> ((DeclaracionRiesgosEntidad.apply _).tupled, DeclaracionRiesgosEntidad.unapply)
  }

  val declaracionesRiesgos = TableQuery[DeclaracionRiesgos]
}
