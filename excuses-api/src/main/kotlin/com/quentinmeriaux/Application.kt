package com.quentinmeriaux

import com.quentinmeriaux.plugins.configureDatabases
import com.quentinmeriaux.plugins.configureMonitoring
import com.quentinmeriaux.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureMonitoring()
}
