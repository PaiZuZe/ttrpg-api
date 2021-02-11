package com.ttrpgapi.routes

import com.ttrpgapi.models.*
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.http.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*


fun generateUUID(): String {
    val STRING_LENGTH: Int = 12
    val char_pool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..STRING_LENGTH)
        .map { _ -> kotlin.random.Random.nextInt(0, char_pool.size) }
        .map(char_pool::get)
        .joinToString("")
}


fun Route.playerCharacterRouting() {
    route("/pcs") {
        post {
            val pc = call.receive<PlayerCharacter>()
            val uuid = generateUUID()
            transaction {
                PlayerCharacters.insert {
                    it[id] = uuid
                    it[name] = pc.name
                    it[max_hp] = pc.max_hp
                    it[current_hp] = pc.current_hp ?: pc.max_hp
                    it[str] = pc.str
                    it[dex] = pc.dex
                    it[int] = pc.int
                    it[cha] = pc.cha
                }
            }
            call.respondText("${uuid}\n", contentType = ContentType.Text.Plain, 
                status = HttpStatusCode.Created
            )
        }
        route("{id}") {
            get {
                val pcID = call.parameters["id"] ?: return@get call.respondText(
                    "Missing id\n",
                    status = HttpStatusCode.BadRequest
                )
                val pc = transaction {
                    PlayerCharacters
                        .select { PlayerCharacters.id eq pcID }
                        .single()
                }
                call.respond(PlayerCharacters.toPlayerCharacter(pc))
            }
            delete {
                val pcID = call.parameters["id"] ?: return@delete call.respondText(
                    "Missing id\n",
                    status = HttpStatusCode.BadRequest
                )
                transaction {
                    PlayerCharacters.deleteWhere { PlayerCharacters.id eq pcID }
                }
                call.respondText("DELETE\n", contentType = ContentType.Text.Plain,
                    status = HttpStatusCode.OK
                )
            }
            patch("/heal") {
                val pcID = call.parameters["id"] ?: return@patch call.respondText(
                    "Missing id\n",
                    status = HttpStatusCode.BadRequest
                )
                val healing = call.parameters["healing"] ?: return@patch call.respondText(
                    "Bad healing amount given\n",
                    status = HttpStatusCode.BadRequest
                )
                val pc = PlayerCharacters.toPlayerCharacter(transaction {
                    PlayerCharacters
                        .select { PlayerCharacters.id eq pcID }
                        .single()
                })
                var tmp: Int = pc.current_hp!!.toIntOrNull() ?: 0
                tmp += healing.toIntOrNull() ?: 0
                val max_hp: Int = pc.max_hp.toIntOrNull() ?: 0
                val new_hp: Int = if (tmp < max_hp) tmp else max_hp
                transaction {
                    PlayerCharacters.update({ PlayerCharacters.id eq pcID}) {
                        it[PlayerCharacters.current_hp] = new_hp.toString()
                    }
                }
                call.respondText("Healing DONE\n", contentType = ContentType.Text.Plain,
                    status = HttpStatusCode.OK
                )
            }
            patch("damage") {
                val pcID = call.parameters["id"] ?: return@patch call.respondText(
                    "Missing id\n",
                    status = HttpStatusCode.BadRequest
                )
                val dmg = call.parameters["dmg"] ?: return@patch call.respondText(
                    "Bad dmg amount given\n",
                    status = HttpStatusCode.BadRequest
                )
                val pc = PlayerCharacters.toPlayerCharacter(transaction {
                    PlayerCharacters
                        .select { PlayerCharacters.id eq pcID }
                        .single()
                })
                var tmp: Int = pc.current_hp!!.toIntOrNull() ?: 0
                tmp -= dmg.toIntOrNull() ?: 0
                val max_hp: Int = pc.max_hp.toIntOrNull() ?: 0
                val new_hp: Int = if (tmp > 0) tmp else 0
                transaction {
                    PlayerCharacters.update({ PlayerCharacters.id eq pcID}) {
                        it[PlayerCharacters.current_hp] = new_hp.toString()
                    }
                }
                call.respondText("Damaging DONE\n", contentType = ContentType.Text.Plain,
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}


fun Application.registerPlayerCharacterRoutes() {
    routing {
        playerCharacterRouting()
    }
}