package ru.grishankov.marketin.screens.launch.state

import ru.grishankov.marketin.data.dto.MarketUpdate
import ru.grishankov.marketin.util.network.ErrorApp

data class MarketUpdateState(
    val isLoading: Boolean = false,
    val data: MarketUpdate? = null,
    val error: ErrorApp? = null,
)
