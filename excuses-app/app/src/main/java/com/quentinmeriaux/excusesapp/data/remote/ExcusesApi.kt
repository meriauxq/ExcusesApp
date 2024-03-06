package com.quentinmeriaux.excusesapp.data.remote

import com.quentinmeriaux.excusesapp.data.remote.dto.Excuse
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

interface ExcusesApi {

    /**
     * Get all the excuses
     */
    suspend fun getExcuses(): List<Excuse>?

    /**
     * Get the excuse corresponding to [httpCode]
     */
    suspend fun getExcuse(httpCode: Int): Excuse?

    /**
     * Post the given [excuse]
     */
    suspend fun postExcuse(excuse: Excuse): Excuse?

    companion object {
        fun create(): ExcusesApi {
            return ExcusesApiImpl(
                client = HttpClient(OkHttp) {
                    // Init the default request URL
                    defaultRequest { url(HttpRoutes.BASE_URL) }

                    // Install plugin for logging network calls
                    install(Logging) {
                        level = LogLevel.ALL
                    }

                    // Install plugin for JSON serialization
                    install(ContentNegotiation) {
                        json()
                    }
                }
            )
        }
    }
}