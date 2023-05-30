package com.tydev.imageGenerator.core.model.data

import kotlinx.serialization.SerialName

data class Image(
    @SerialName("created")
    val created: Int,

    @SerialName("data")
    val data: List<DataItem>
)
