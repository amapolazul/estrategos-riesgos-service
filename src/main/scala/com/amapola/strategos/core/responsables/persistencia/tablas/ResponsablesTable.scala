package com.amapola.strategos.core.responsables.persistencia.tablas

import com.amapola.strategos.core.responsables.persistencia.entidades.ResponsablesEntidad
import com.amapola.strategos.utils.db.DatabaseConnector

private[responsables] trait ResponsablesTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Responsables(tag: Tag)
      extends Table[ResponsablesEntidad](tag, "responsables") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("email")

    def * =
      (id.?, email) <> ((ResponsablesEntidad.apply _).tupled, ResponsablesEntidad.unapply)
  }

  val responsables = TableQuery[Responsables]

}
