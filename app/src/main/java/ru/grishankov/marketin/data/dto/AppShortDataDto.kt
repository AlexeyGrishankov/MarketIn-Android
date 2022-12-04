package ru.grishankov.marketin.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppShortDataDto(
    val id: Int,
    val label: String,
    val tags: List<String>,
    val logoUrl: String,
)