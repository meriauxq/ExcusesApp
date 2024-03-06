package com.quentinmeriaux.excusesapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Excuse(
    val httpCode: Int,
    val tag: String,
    val message: String
)