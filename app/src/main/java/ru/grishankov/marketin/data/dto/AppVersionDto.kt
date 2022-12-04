package ru.grishankov.marketin.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AppVersionDto(
    val id: Int,
    val idApp: Int,
    val title: String,
    val description: String,
    val url: String,
    val versionCode: Int,
)