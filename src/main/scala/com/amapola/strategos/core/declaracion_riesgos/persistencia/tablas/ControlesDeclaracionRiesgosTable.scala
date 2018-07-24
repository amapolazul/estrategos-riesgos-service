package com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.{ControlesDeclaracionRiesgosEntidad, DeclaracionRiesgosEntidad}
import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.EfectividadRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.EfectividadRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector
import slick.lifted.ForeignKeyQuery

private[declaracion_riesgos] trait ControlesDeclaracionRiesgosTable
    extends EfectividadRiesgosTable
    with DeclaracionRiesgosTable {
  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class ControlesDeclaracionRiesgos(tag: Tag)
    extends Table[ControlesDeclaracionRiesgosEntidad](
      tag,
      "controles_declaracion_riesgos") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def efectividad_riesgos_id = column[Long]("efectividad_riesgos_id")
    def declaracion_riesgo_id = column[Long]("declaracion_riesgo_id")
    def control = column[String]("control")
    def descripcion = column[String]("descripcion")

    def controlEfectividadRiesgosFk
    : ForeignKeyQuery[EfectividadRiesgos, EfectividadRiesgosEntidad] =
      foreignKey("CONTROL_EFECTIVIDAD_RIESGO_FK",
        efectividad_riesgos_id,
        efectividadRiesgos)(_.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def causasDeclaracionRiesgosFk
    : ForeignKeyQuery[DeclaracionRiesgos, DeclaracionRiesgosEntidad] =
      foreignKey("CONTROL_DECLARACION_RIESGO_FK",
        declaracion_riesgo_id,
        declaracionesRiesgos)(_.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    override def * =
      (
        id.?,
        efectividad_riesgos_id,
        declaracion_riesgo_id.?,
        control,
        descripcion
      ) <> ((ControlesDeclaracionRiesgosEntidad.apply _).tupled, ControlesDeclaracionRiesgosEntidad.unapply)
  }

  val controlDeclaracionRiesgos = TableQuery[ControlesDeclaracionRiesgos]
}
