package ru.grishankov.marketin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Навигационный контроллер
 */
class NavController(
    private val startDestination: ScreenRoute,
    private var backStackScreens: MutableSet<NavBackData> = mutableSetOf(),
) {

    var currentScreen: MutableState<ScreenRoute> = mutableStateOf(startDestination)
    var currentBundle: MutableState<Bundle> = mutableStateOf(Bundle())

    /**
     * Перейти на след экран
     */
    fun navigate(route: ScreenRoute, bundle: Bundle = Bundle()) {
        if (route != currentScreen.value) {

            if (backStackScreens.map { it.screenRoute }.contains(currentScreen.value) && currentScreen.value != startDestination) {
                val index = backStackScreens.indexOfFirst { it.screenRoute == currentScreen.value }
                backStackScreens.remove(backStackScreens.elementAt(index))
            }

            if (route == startDestination) {
                backStackScreens = mutableSetOf()
            } else {
                val navBackData = NavBackData(currentScreen.value, currentBundle.value)
                backStackScreens += navBackData
            }

            currentScreen.value = route
            currentBundle.value = bundle
        }
    }

    /**
     * Вернутся обратно
     */
    fun navigateBack() {
        if (backStackScreens.isNotEmpty()) {
            val last = backStackScreens.last()
            currentScreen.value = last.screenRoute
            currentBundle.value = last.bundle
            backStackScreens.remove(last)
        }
    }

    /**
     * Очистить историю переходов
     */
    fun clearBackStack() {
        backStackScreens.clear()
    }
}

/**
 * Данные навигации для обратного перехода
 */
data class NavBackData(
    val screenRoute: ScreenRoute,
    val bundle: Bundle
)

/**
 * Аргументы навигации
 */
val NavController.args
    get() = currentBundle.value

/**
 * Получить контроллер навигации
 */
@Composable
fun rememberNavController(
    startDestination: ScreenRoute,
    backStackScreens: MutableSet<NavBackData> = mutableSetOf(),
): MutableState<NavController> = remember {
    mutableStateOf(NavController(startDestination, backStackScreens))
}
