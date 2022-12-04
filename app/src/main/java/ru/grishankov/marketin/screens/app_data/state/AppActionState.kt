package ru.grishankov.marketin.screens.app_data.state

data class AppActionState(
    val isDeleting: Boolean = false,
    val isInstalling: Boolean = false,
    val isDone: Boolean = true
)
