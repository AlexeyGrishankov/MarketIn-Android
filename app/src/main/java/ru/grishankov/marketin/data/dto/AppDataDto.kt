package ru.grishankov.marketin.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppDataDto(
    val id: Int,
    val label: String,
    val desc: String,
    val tags: List<String>,
    val logoUrl: String,
    val lastVersion: AppVersionDto?,
    val versions: List<AppVersionDto>,
    val packageName: String,
)