package ru.grishankov.marketin.screens.app_list.state

import ru.grishankov.marketin.data.dto.AppShortDataListDto
import ru.grishankov.marketin.util.network.ErrorApp

data class AppListState(
    val isLoading: Boolean = false,
    val content: AppShortDataListDto? = null,
    val error: ErrorApp? = null
)
