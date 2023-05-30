package com.tydev.imageGenerator.core.model.data

import kotlinx.serialization.SerialName

data class Choice(
    @SerialName("index")
    val index: Int,

    @SerialName("message")
    val message: Message,

    @SerialName("finish_reason")
    val finishReason: String
)
