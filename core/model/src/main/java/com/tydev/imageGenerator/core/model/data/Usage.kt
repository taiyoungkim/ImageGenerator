package com.tydev.imageGenerator.core.model.data

import kotlinx.serialization.SerialName

data class Usage(
    @SerialName("prompt_tokens")
    val promptTokens: Int,

    @SerialName("completion_tokens")
    val completionTokens: Int,

    @SerialName("total_tokens")
    val totalTokens: Int
)
