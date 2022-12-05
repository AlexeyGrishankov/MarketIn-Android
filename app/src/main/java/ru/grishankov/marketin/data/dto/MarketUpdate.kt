package ru.grishankov.marketin.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MarketUpdate(
    val lastVersion: String,
    val urlDownload: String,
    val isNeedUpdate: Boolean,
)