package com.amapola.strategos.core.declaracion_riesgos.persistencia.tablas

import com.amapola.strategos.core.declaracion_riesgos.persistencia.entidades.{
  CausasDeclaracionRiesgosEntidad,
  DeclaracionRiesgosEntidad
}
import com.amapola.strategos.core.tablas_sistema.persistencia.entidades.ProbabilidadRiesgosEntidad
import com.amapola.strategos.core.tablas_sistema.persistencia.tablas.ProbabilidadRiesgosTable
import com.amapola.strategos.utils.db.DatabaseConnector
import slick.lifted.ForeignKeyQuery

/**
  * clase que mapea la tabla causas_declaracion_riesgos en la base de datos
  */
private[declaracion_riesgos] trait CausasDeclaracionRiesgosTable
    extends ProbabilidadRiesgosTable
    with DeclaracionRiesgosTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class CausasDeclaracionRiesgos(tag: Tag)
      extends Table[CausasDeclaracionRiesgosEntidad](tag, "causas_declaracion_riesgos") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def probabilidad_riesgo_id = column[Long]("probabilidad_riesgo_id")
    def declaracion_riesgo_id = column[Long]("declaracion_riesgo_id")
    def causa = column[String]("causa")
    def descripcion = column[String]("descripcion")

    def causasProbabilidadRiesgosFk
      : ForeignKeyQuery[ProbabilidadRiesgos, ProbabilidadRiesgosEntidad] =
      foreignKey("CAUSAS_PROBABILIDAD_RIESGO_FK",
                 probabilidad_riesgo_id,
                 probabilidadRiesgos)(_.id,
                                      onUpdate = ForeignKeyAction.Restrict,
                                      onDelete = ForeignKeyAction.Cascade)

    def causasDeclaracionRiesgosFk
      : ForeignKeyQuery[DeclaracionRiesgos, DeclaracionRiesgosEntidad] =
      foreignKey("CAUSAS_DECLARACION_RIESGO_FK",
                 declaracion_riesgo_id,
                 declaracionesRiesgos)(_.id,
                                       onUpdate = ForeignKeyAction.Restrict,
                                       onDelete = ForeignKeyAction.Cascade)

    override def * =
      (
        id.?,
        probabilidad_riesgo_id,
        declaracion_riesgo_id,
        causa,
        descripcion
      ) <> ((CausasDeclaracionRiesgosEntidad.apply _).tupled, CausasDeclaracionRiesgosEntidad.unapply)
  }

  val causasDeclaracionRiesgos = TableQuery[CausasDeclaracionRiesgos]

}
