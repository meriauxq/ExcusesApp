package com.quentinmeriaux.excusesapp.data.remote

import com.quentinmeriaux.excusesapp.data.remote.dto.Excuse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class ExcusesApiImpl(
    private val client: HttpClient
) : ExcusesApi {
    override suspend fun getExcuses(): List<Excuse>? {
        return try {
            client.get(HttpRoutes.EXCUSES).body()
        } catch (e: Exception) {
            println("Error while trying to get Excuses : ${e.message}")
            null
        }
    }

    override suspend fun getExcuse(httpCode: Int): Excuse? {
        return try {
            val response = client.get("/$httpCode")
            if (response.status.isSuccess()) {
                response.body()
            } else if (response.status == HttpStatusCode.NotFound) {
                println("404 Error while trying to get Excuse")
                // Return a "special" Excuse for 404 status code
                Excuse(404, "404", "Not found")
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error while trying to get Excuse ($httpCode) : ${e.message}")
            null
        }
    }

    override suspend fun postExcuse(excuse: Excuse): Excuse? {
        return try {
            client.post(HttpRoutes.EXCUSES) {
                setBody(excuse)
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            println("Error while trying to post Excuse ($excuse) : ${e.message}")
            null
        }
    }
}