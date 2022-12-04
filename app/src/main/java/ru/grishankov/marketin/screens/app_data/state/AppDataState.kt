package ru.grishankov.marketin.screens.app_data.state

import ru.grishankov.marketin.data.dto.AppDataDto
import ru.grishankov.marketin.util.network.ErrorApp

data class AppDataState(
    val isLoading: Boolean = false,
    val content: AppDataDto? = null,
    val error: ErrorApp? = null
)