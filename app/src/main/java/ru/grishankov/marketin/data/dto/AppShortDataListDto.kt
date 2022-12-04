package ru.grishankov.marketin.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppShortDataListDto(
    @SerialName("data") val list: List<AppShortDataDto>
)