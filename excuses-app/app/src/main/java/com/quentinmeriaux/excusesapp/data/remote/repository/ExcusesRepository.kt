package com.quentinmeriaux.excusesapp.data.remote.repository

import com.quentinmeriaux.excusesapp.data.remote.ExcusesApi
import com.quentinmeriaux.excusesapp.data.remote.dto.Excuse

class ExcusesRepository(private val api: ExcusesApi) {
    /**
     * Get excuses from [api]
     */
    suspend fun getExcuses(): List<Excuse>? = api.getExcuses()

    /**
     * Get excuse corresponding to [httpCode] from [api]
     */
    suspend fun getExcuse(httpCode: Int): Excuse? = api.getExcuse(httpCode)

    /**
     * Post [excuse] to [api]
     */
    suspend fun postExcuse(excuse: Excuse): Excuse? = api.postExcuse(excuse)
}