package ru.grishankov.marketin.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.grishankov.marketin.screens.app_list.ScreenAppListViewModel
import ru.grishankov.marketin.screens.app_data.ScreenAppDataViewModel
import ru.grishankov.marketin.screens.launch.ScreenLaunchViewModel

object DiViewModel : DiModule {
    override val module: Module
        get() = module {
            viewModelOf(::ScreenAppListViewModel)
            viewModelOf(::ScreenAppDataViewModel)
            viewModelOf(::ScreenLaunchViewModel)
        }
}