package com.tydev.imageGenerator.core.model.data

import kotlinx.serialization.SerialName

data class Message(
    @SerialName("role")
    val role: String,

    @SerialName("content")
    val content: String
)
