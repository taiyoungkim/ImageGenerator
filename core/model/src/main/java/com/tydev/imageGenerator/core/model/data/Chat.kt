package com.tydev.imagegenerator.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
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
