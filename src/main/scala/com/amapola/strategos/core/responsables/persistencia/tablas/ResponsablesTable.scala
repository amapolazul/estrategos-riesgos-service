package com.amapola.strategos.core.responsables.persistencia.tablas

import com.amapola.strategos.core.responsables.persistencia.entidades.ResponsablesEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

private[responsables] trait ResponsablesTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Responsables(tag: Tag)
      extends Table[ResponsablesEntidad](tag, "responsable") {

    def responsable_id = column[Long]("responsable_id", O.PrimaryKey, O.AutoInc)
    def usuario_id = column[Long]("usuario_id")
    def nombre = column[String]("nombre")
    def cargo = column[String]("cargo")
    def ubicacion = column[String]("ubicacion")
    def email = column[String]("email")
    def notas = column[String]("notas")
    def children_count  = column[Long]("children_count")
    def tipo  = column[Long]("tipo")
    def grupo  = column[Long]("grupo")
    def organizacion_id  = column[Long]("organizacion_id")

    def * =
      (responsable_id,
        usuario_id.?,
        nombre,
        cargo,
        ubicacion.?,
        email.?,
        notas.?,
        children_count.?,
        tipo,
        grupo,
        organizacion_id.? ) <> ((ResponsablesEntidad.apply _).tupled, ResponsablesEntidad.unapply)
  }

  val responsables = TableQuery[Responsables]

}
