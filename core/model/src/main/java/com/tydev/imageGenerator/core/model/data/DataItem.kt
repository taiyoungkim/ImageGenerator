package com.tydev.imagegenerator.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataItem(
    @SerialName("url")
    val url: String
)
