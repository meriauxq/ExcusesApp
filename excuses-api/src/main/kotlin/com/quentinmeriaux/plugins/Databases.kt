package com.quentinmeriaux.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
    val excusesService = ExcusesService(database)
    routing {
        // Create a new excuse
        post("/excuses") {
            val user = call.receive<Excuse>()
            val excuse = excusesService.create(user)
            if (excuse != null) {
                call.respond(HttpStatusCode.Created, excuse)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        // Read all excuses
        get("/excuses") {
            val excuses = excusesService.readAll()
            call.respond(HttpStatusCode.OK, excuses)
        }

        // Read an excuse
        get("/{http_code}") {
            val id = call.parameters["http_code"]?.toInt() ?: throw IllegalArgumentException("Invalid HTTP code")
            val excuse = excusesService.read(id)
            if (excuse != null) {
                call.respond(HttpStatusCode.OK, excuse)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
