package ru.grishankov.marketin.screens

import androidx.compose.runtime.Composable

abstract class BaseScreen {

    /**
     * Инициализация экрана
     */
    @Composable
    abstract fun OnStart()

}