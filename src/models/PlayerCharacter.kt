package com.ttrpgapi.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*


object PlayerCharacters: Table() {
    val id = varchar("id", 12)
    val name = varchar("name", 50)
    val str = varchar("str", 3)
    val dex = varchar("dex", 2)
    val int = varchar("int", 2)
    val cha = varchar("cha", 2)
    val maxHp = integer("maxHp")
    val currentHp = integer("currentHp")

    override val primaryKey = PrimaryKey(id, name = "PK_PC_ID")

    fun toPlayerCharacter(row: ResultRow): PlayerCharacter =
        PlayerCharacter(
            id = row[PlayerCharacters.id],
            name = row[PlayerCharacters.name],
            str = row[PlayerCharacters.str],
            dex = row[PlayerCharacters.dex],
            int = row[PlayerCharacters.int],
            cha = row[PlayerCharacters.cha],
            maxHp = row[PlayerCharacters.maxHp],
            currentHp = row[PlayerCharacters.currentHp]
        )

}


@Serializable
data class PlayerCharacter(
    val id: String? = null,
    val name: String,
    val str: String,
    val dex: String,
    val int: String,
    val cha: String,
    val maxHp: Int,
    val currentHp: Int? = null
)
