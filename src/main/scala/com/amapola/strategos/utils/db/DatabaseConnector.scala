package com.amapola.strategos.utils.db

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

class DatabaseConnector(jdbcUrl: String, dbUser: String, dbPassword: String) {

  private val hikariDataSource = {
    val hikariConfig = new HikariConfig()
    hikariConfig.setJdbcUrl(jdbcUrl)
    hikariConfig.setUsername(dbUser)
    hikariConfig.setPassword(dbPassword)

    new HikariDataSource(hikariConfig)
  }

  val profile = slick.jdbc.SQLServerProfile
  import profile.api._

  val db = Database.forDataSource(hikariDataSource, None)
  db.createSession()

}
