package com.ttrpgapi

import com.ttrpgapi.models.PlayerCharacters
import io.ktor.application.*
import com.zaxxer.hikari.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*


fun createTables() = transaction {
    SchemaUtils.create(PlayerCharacters)
}

fun Application.initDB() {
    val config: HikariConfig = HikariConfig("resources/hikari.properties")
    val ds: HikariDataSource = HikariDataSource(config)
    Database.connect(ds)
    createTables()
}