package ru.grishankov.marketin.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MarketUpdateReq(
    val currentVersion: String,
)