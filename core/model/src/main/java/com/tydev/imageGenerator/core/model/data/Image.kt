package com.tydev.imagegenerator.core.model.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("created")
    val created: Int,

    @SerialName("data")
    val data: List<DataItem>
)
