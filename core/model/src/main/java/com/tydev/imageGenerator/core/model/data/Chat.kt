package com.tydev.imageGenerator.core.model.data

import kotlinx.serialization.SerialName

data class Chat(
    @SerialName("id")
    val id: String,

    @SerialName("object")
    val objectValue: String,

    @SerialName("created")
    val created: Int,

    @SerialName("choices")
    val choices: List<Choice>,

    @SerialName("usage")
    val usage: Usage
)
