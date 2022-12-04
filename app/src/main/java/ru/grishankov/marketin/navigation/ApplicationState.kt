package ru.icomplex.dentistry.navigation

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Состояние - Верхний бара
 */
data class TopState(
    val title: String = "",
    val subtitle: String? = null,
    val actions: (@Composable RowScope.() -> Unit)? = null
)

/**
 * Состояние - передний план
 */
data class ForegroundState(
    val content: (@Composable BoxScope.() -> Unit)? = null,
    var toast: MutableState<String?> = mutableStateOf(null),
)

/**
 * Глобальная композиция
 */
data class GlobalComposingState(
    val top: TopState = TopState(),
    val foreground: ForegroundState = ForegroundState(),
)