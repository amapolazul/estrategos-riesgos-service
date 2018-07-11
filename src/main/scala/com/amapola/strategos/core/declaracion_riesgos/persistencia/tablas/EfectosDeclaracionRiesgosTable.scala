package com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.{DeclaracionRiesgosEntidad, EfectosDeclaracionRiesgosEntidad}
import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.ImpactoRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.ImpactoRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector
import slick.lifted.ForeignKeyQuery

/**
  * Mapea la tabla efectos_declaracion_riesgos
  */
private[declaracion_riesgos] trait EfectosDeclaracionRiesgosTable
    extends ImpactoRiesgosTable
    with DeclaracionRiesgosTable {
  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class EfectosDeclaracionRiesgos(tag: Tag)
    extends Table[EfectosDeclaracionRiesgosEntidad](
      tag,
      "efectos_declaracion_riesgos") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def impacto_riesgos_id = column[Long]("impacto_riesgos_id")
    def declaracion_riesgo_id = column[Long]("declaracion_riesgo_id")
    def impacto = column[String]("impacto")
    def descripcion = column[String]("descripcion")

    def efectosImpactoRiesgosFk
    : ForeignKeyQuery[ImpactoRiesgos, ImpactoRiesgosEntidad] =
      foreignKey("EFECTOS_IMPACTO_RIESGO_FK",
        impacto_riesgos_id,
        impactoRiesgos)(_.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    def causasDeclaracionRiesgosFk
    : ForeignKeyQuery[DeclaracionRiesgos, DeclaracionRiesgosEntidad] =
      foreignKey("EFECTOS_DECLARACION_RIESGO_FK",
        declaracion_riesgo_id,
        declaracionesRiesgos)(_.id,
        onUpdate = ForeignKeyAction.Restrict,
        onDelete = ForeignKeyAction.Cascade)

    override def * =
      (
        id.?,
        impacto_riesgos_id,
        declaracion_riesgo_id,
        impacto,
        descripcion
      ) <> ((EfectosDeclaracionRiesgosEntidad.apply _).tupled, EfectosDeclaracionRiesgosEntidad.unapply)
  }

  val efectosDeclaracionRiesgos = TableQuery[EfectosDeclaracionRiesgos]
}
