package ru.grishankov.marketin.navigation

import androidx.compose.runtime.Composable
import ru.grishankov.marketin.screens.app_data.ScreenAppData
import ru.grishankov.marketin.screens.app_list.ScreenAppList
import ru.grishankov.marketin.screens.launch.ScreenLaunch

/**
 * Граф навигации
 */
@Composable
fun CustomNavigationHost(
    navController: NavController,
) {
    NavigationHost(navController) {
        composable(ScreenRoute.ScreenLaunch) {
            ScreenLaunch(navController).OnStart()
        }
        composable(ScreenRoute.ScreenAppData) {
            ScreenAppData(navController).OnStart()
        }
        composable(ScreenRoute.ScreenAppList) {
            ScreenAppList(navController).OnStart()
        }
    }.build()
}