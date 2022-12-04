package ru.grishankov.marketin.navigation

import androidx.compose.runtime.Composable

/**
 * Хост навигации
 */
class NavigationHost(
    val navController: NavController,
    val contents: @Composable NavigationGraphBuilder.() -> Unit
) {

    /**
     * Собрать
     */
    @Composable
    fun build() {
        NavigationGraphBuilder().renderContents()
    }

    /**
     * Билдер для хоста навигации
     */
    inner class NavigationGraphBuilder(
        val navController: NavController = this@NavigationHost.navController
    ) {
        /**
         * Рендер компонента навигации
         */
        @Composable
        fun renderContents() {
            this@NavigationHost.contents(this)
        }
    }
}

/**
 * Composabe контент навигации
 */
@Composable
fun NavigationHost.NavigationGraphBuilder.composable(
    route: ScreenRoute,
    content: @Composable () -> Unit
) {
    if (navController.currentScreen.value == route) {
        content()
    }
}