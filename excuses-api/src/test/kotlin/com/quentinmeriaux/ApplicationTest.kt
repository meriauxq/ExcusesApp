package com.quentinmeriaux

import com.quentinmeriaux.plugins.Excuse
import com.quentinmeriaux.plugins.configureDatabases
import com.quentinmeriaux.plugins.configureMonitoring
import com.quentinmeriaux.plugins.configureSerialization
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testApp() = testApplication {
        application {
            configureSerialization()
            configureDatabases()
            configureMonitoring()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        // Test POST /excuses
        val excuseToAdd = Excuse(httpCode = 800, tag = "Testing", message = "Test Excuse")
        client.post("/excuses") {
            setBody(excuseToAdd)
            contentType(ContentType.Application.Json)
        }
            .apply {
                assertEquals(HttpStatusCode.Created, status)
                assertEquals(excuseToAdd, this.body<Excuse>())
            }

        // Test GET /excuses
        val baseListSize = 76
        client.get("/excuses").apply {
            assertEquals(HttpStatusCode.OK, status)
            val excusesList = this.body<ArrayList<Excuse>>()
            assertEquals(baseListSize + 1, excusesList.size)
            assertEquals(excuseToAdd, excusesList.last())
        }

        // Test GET /{http_code}
        client.get("/800").apply {
            assertEquals(HttpStatusCode.OK, status)
            val foundExcuse = this.body<Excuse>()
            assertEquals(excuseToAdd, foundExcuse)
        }

        // Test GET /{http_code} unknown
        client.get("/999").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }
}
